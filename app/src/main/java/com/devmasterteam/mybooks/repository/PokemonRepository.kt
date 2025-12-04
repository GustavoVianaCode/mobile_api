package com.devmasterteam.mybooks.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.devmasterteam.mybooks.api.RetrofitClient
import com.devmasterteam.mybooks.api.model.PokemonDetails
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.entity.TeamPokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class PokemonRepository private constructor(context: Context) {
    
    private val database = PokemonDatabase.getDatabase(context)
    private val pokemonDao = database.pokemonDAO()
    private val teamPokemonDao = database.teamPokemonDAO()
    private val apiService = RetrofitClient.pokeApiService
    
    companion object {
        @Volatile
        private var instance: PokemonRepository? = null
        
        fun getInstance(context: Context): PokemonRepository {
            return instance ?: synchronized(this) {
                val newInstance = PokemonRepository(context.applicationContext)
                instance = newInstance
                newInstance
            }
        }
    }
    
    // ========== POKEMON OPERATIONS ==========
    
    /**
     * Busca Pokémon da API e salva no banco local
     */
    suspend fun fetchAndCachePokemonList(limit: Int = 151, offset: Int = 0): Result<List<PokemonEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonList(limit, offset)
                
                if (response.isSuccessful && response.body() != null) {
                    val pokemonList = mutableListOf<PokemonEntity>()
                    
                    response.body()!!.results.forEach { basicPokemon ->
                        // Extrair ID da URL
                        val id = basicPokemon.url.trimEnd('/').split("/").last().toIntOrNull()
                        
                        if (id != null) {
                            val detailsResponse = apiService.getPokemonDetails(id)
                            
                            if (detailsResponse.isSuccessful && detailsResponse.body() != null) {
                                val details = detailsResponse.body()!!
                                val entity = mapPokemonDetailsToEntity(details)
                                pokemonList.add(entity)
                            }
                        }
                    }
                    
                    // Salvar no banco de dados
                    pokemonDao.insertAll(pokemonList)
                    Result.success(pokemonList)
                } else {
                    Result.failure(Exception("Erro ao buscar Pokémon: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("PokemonRepository", "Erro ao buscar pokémons", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Busca Pokémon por geração
     */
    suspend fun fetchPokemonByGeneration(generationId: Int): Result<List<PokemonEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("PokemonRepository", "Buscando geração $generationId da API")
                val response = apiService.getGeneration(generationId)
                
                Log.d("PokemonRepository", "Resposta recebida: ${response.code()}")
                if (response.isSuccessful && response.body() != null) {
                    val pokemonList = mutableListOf<PokemonEntity>()
                    
                    // OTIMIZAÇÃO: Carregar em paralelo
                    val deferredList = coroutineScope {
                        response.body()!!.pokemonSpecies.mapNotNull { species ->
                            // Extrair ID da URL
                            val id = species.url.trimEnd('/').split("/").last().toIntOrNull()
                            
                            if (id != null) {
                                async(Dispatchers.IO) {
                                    try {
                                        // Verificar cache primeiro
                                        val cachedPokemon = pokemonDao.getPokemonById(id)
                                        if (cachedPokemon != null) {
                                            return@async cachedPokemon
                                        }
                                        
                                        // Buscar da API
                                        val detailsResponse = apiService.getPokemonDetails(id)
                                        
                                        if (detailsResponse.isSuccessful && detailsResponse.body() != null) {
                                            val details = detailsResponse.body()!!
                                            val entity = mapPokemonDetailsToEntity(details, generationId)
                                            pokemonDao.insert(entity)
                                            entity
                                        } else {
                                            null
                                        }
                                    } catch (e: Exception) {
                                        Log.e("PokemonRepository", "Erro ao buscar pokémon $id", e)
                                        null
                                    }
                                }
                            } else {
                                null
                            }
                        }
                    }
                    
                    // Aguardar todos os resultados
                    pokemonList.addAll(deferredList.awaitAll().filterNotNull())
                    
                    // ORDENAR POR ID NUMÉRICO
                    val sortedList = pokemonList.sortedBy { it.id }
                    
                    Log.d("PokemonRepository", "Retornando lista ordenada com ${sortedList.size} pokémons")
                    Result.success(sortedList)
                } else {
                    Log.e("PokemonRepository", "Erro na resposta: ${response.code()}")
                    Result.failure(Exception("Erro ao buscar geração: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("PokemonRepository", "Erro ao buscar por geração", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Busca Pokémon por versão de jogo (usando pokedex)
     */
    suspend fun fetchPokemonByVersion(versionName: String): Result<List<PokemonEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                // Mapear versões para pokedex correspondente
                val pokedexName = mapVersionToPokedex(versionName)
                val response = apiService.getPokedexByName(pokedexName)
                
                if (response.isSuccessful && response.body() != null) {
                    val pokemonList = mutableListOf<PokemonEntity>()
                    
                    response.body()!!.pokemonEntries.forEach { entry ->
                        val id = entry.pokemonSpecies.url.trimEnd('/').split("/").last().toIntOrNull()
                        
                        if (id != null) {
                            val detailsResponse = apiService.getPokemonDetails(id)
                            
                            if (detailsResponse.isSuccessful && detailsResponse.body() != null) {
                                val details = detailsResponse.body()!!
                                val entity = mapPokemonDetailsToEntity(details)
                                pokemonList.add(entity)
                            }
                        }
                    }
                    
                    // Salvar no banco
                    pokemonDao.insertAll(pokemonList)
                    Result.success(pokemonList)
                } else {
                    Result.failure(Exception("Erro ao buscar versão: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("PokemonRepository", "Erro ao buscar por versão", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Busca detalhes de um Pokémon específico
     */
    suspend fun getPokemonDetails(pokemonId: Int): Result<PokemonEntity> {
        return withContext(Dispatchers.IO) {
            try {
                // Primeiro tenta buscar do banco local
                val cachedPokemon = pokemonDao.getPokemonById(pokemonId)
                if (cachedPokemon != null) {
                    return@withContext Result.success(cachedPokemon)
                }
                
                // Se não encontrar, busca da API
                val response = apiService.getPokemonDetails(pokemonId)
                
                if (response.isSuccessful && response.body() != null) {
                    val details = response.body()!!
                    val entity = mapPokemonDetailsToEntity(details)
                    
                    // Salvar no banco
                    pokemonDao.insert(entity)
                    Result.success(entity)
                } else {
                    Result.failure(Exception("Erro ao buscar detalhes: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("PokemonRepository", "Erro ao buscar detalhes do pokémon", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Retorna todos os Pokémon do banco local
     */
    fun getAllPokemonFromDB(): LiveData<List<PokemonEntity>> {
        return pokemonDao.getAllPokemon()
    }
    
    /**
     * Retorna Pokémon de uma geração específica do banco local
     */
    fun getPokemonByGenerationFromDB(generation: Int): LiveData<List<PokemonEntity>> {
        return pokemonDao.getPokemonByGeneration(generation)
    }
    
    // ========== TEAM OPERATIONS ==========
    
    /**
     * Adiciona um Pokémon ao time
     */
    suspend fun addToTeam(pokemon: PokemonEntity): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                // Verificar se já está no time
                if (teamPokemonDao.isPokemonInTeam(pokemon.id)) {
                    return@withContext Result.failure(Exception("Pokémon já está no time!"))
                }
                
                // Verificar limite de 6 Pokémon
                if (teamPokemonDao.getTeamCount() >= 6) {
                    return@withContext Result.failure(Exception("Time completo! Máximo de 6 Pokémon."))
                }
                
                val teamPokemon = TeamPokemonEntity(
                    pokemonId = pokemon.id,
                    pokemonName = pokemon.name,
                    spriteUrl = pokemon.spriteUrl,
                    types = pokemon.types
                )
                
                val id = teamPokemonDao.insert(teamPokemon)
                Result.success(id)
            } catch (e: Exception) {
                Log.e("PokemonRepository", "Erro ao adicionar ao time", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Remove um Pokémon do time
     */
    suspend fun removeFromTeam(pokemonId: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                teamPokemonDao.deleteByPokemonId(pokemonId)
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("PokemonRepository", "Erro ao remover do time", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Verifica se um Pokémon está no time
     */
    suspend fun isPokemonInTeam(pokemonId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            teamPokemonDao.isPokemonInTeam(pokemonId)
        }
    }
    
    /**
     * Retorna todos os Pokémon do time
     */
    fun getTeamPokemon(): LiveData<List<TeamPokemonEntity>> {
        return teamPokemonDao.getAllTeamPokemon()
    }
    
    /**
     * Retorna a contagem de Pokémon no time
     */
    suspend fun getTeamCount(): Int {
        return withContext(Dispatchers.IO) {
            teamPokemonDao.getTeamCount()
        }
    }
    
    // ========== HELPER FUNCTIONS ==========
    
    private fun mapPokemonDetailsToEntity(details: PokemonDetails, generation: Int = 0): PokemonEntity {
        val types = details.types.joinToString(",") { it.type.name }
        val spriteUrl = details.sprites.other?.officialArtwork?.frontDefault 
            ?: details.sprites.frontDefault 
            ?: ""
        val shinyUrl = details.sprites.other?.officialArtwork?.frontShiny 
            ?: details.sprites.frontShiny 
            ?: ""
        
        // Calcular geração baseado no ID se não foi fornecido
        val gen = if (generation > 0) generation else calculateGeneration(details.id)
        
        return PokemonEntity(
            id = details.id,
            name = details.name,
            spriteUrl = spriteUrl,
            shinySpriteUrl = shinyUrl,
            types = types,
            height = details.height,
            weight = details.weight,
            generation = gen
        )
    }
    
    private fun calculateGeneration(pokemonId: Int): Int {
        return when (pokemonId) {
            in 1..151 -> 1
            in 152..251 -> 2
            in 252..386 -> 3
            in 387..493 -> 4
            in 494..649 -> 5
            in 650..721 -> 6
            in 722..809 -> 7
            in 810..905 -> 8
            else -> 9
        }
    }
    
    private fun mapVersionToPokedex(versionName: String): String {
        return when (versionName) {
            "red", "blue", "yellow" -> "kanto"
            "gold", "silver", "crystal" -> "original-johto"
            "ruby", "sapphire", "emerald" -> "hoenn"
            "firered", "leafgreen" -> "kanto"
            "diamond", "pearl", "platinum" -> "original-sinnoh"
            "heartgold", "soulsilver" -> "updated-johto"
            "black", "white", "black-2", "white-2" -> "original-unova"
            "x", "y" -> "kalos-central"
            "omega-ruby", "alpha-sapphire" -> "updated-hoenn"
            "sun", "moon", "ultra-sun", "ultra-moon" -> "original-alola"
            "lets-go-pikachu", "lets-go-eevee" -> "letsgo-kanto"
            "sword", "shield" -> "galar"
            "brilliant-diamond", "shining-pearl" -> "original-sinnoh"
            "legends-arceus" -> "hisui"
            "scarlet", "violet" -> "paldea"
            else -> "national" // Pokedex nacional como fallback
        }
    }
}
