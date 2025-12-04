package com.devmasterteam.mybooks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonListViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = PokemonRepository.getInstance(application)
    
    private val _pokemonList = MutableLiveData<List<PokemonEntity>>()
    val pokemonList: LiveData<List<PokemonEntity>> = _pokemonList
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage
    
    private var currentFilterType = 0 // 0=All, 1=Generation, 2=Version
    private var currentGeneration = 1
    private var currentVersion = ""
    
    /**
     * Carrega todos os Pokémon (primeira geração por padrão)
     */
    fun loadAllPokemon() {
        _isLoading.value = true
        currentFilterType = 0
        
        viewModelScope.launch {
            val result = repository.fetchAndCachePokemonList(151, 0)
            
            result.onSuccess { pokemons ->
                _pokemonList.value = pokemons
                _isLoading.value = false
            }.onFailure { exception ->
                _errorMessage.value = "Erro ao carregar Pokémon: ${exception.message}"
                _isLoading.value = false
                
                // Tentar carregar do cache local
                loadFromCache()
            }
        }
    }
    
    /**
     * Carrega Pokémon por geração
     */
    fun loadPokemonByGeneration(generationId: Int) {
        Log.d("PokemonListViewModel", "loadPokemonByGeneration chamado: $generationId")
        _isLoading.value = true
        currentFilterType = 1
        currentGeneration = generationId
        
        viewModelScope.launch {
            Log.d("PokemonListViewModel", "Iniciando fetch da geração $generationId")
            val result = repository.fetchPokemonByGeneration(generationId)
            
            result.onSuccess { pokemons ->
                Log.d("PokemonListViewModel", "Sucesso! ${pokemons.size} pokémons recebidos")
                _pokemonList.value = pokemons
                _isLoading.value = false
                _successMessage.value = "Geração $generationId carregada!"
            }.onFailure { exception ->
                _errorMessage.value = "Erro ao carregar geração: ${exception.message}"
                _isLoading.value = false
                
                // Tentar do cache
                loadFromCacheByGeneration(generationId)
            }
        }
    }
    
    /**
     * Carrega Pokémon por versão de jogo
     */
    fun loadPokemonByVersion(versionName: String) {
        _isLoading.value = true
        currentFilterType = 2
        currentVersion = versionName
        
        viewModelScope.launch {
            val result = repository.fetchPokemonByVersion(versionName)
            
            result.onSuccess { pokemons ->
                _pokemonList.value = pokemons
                _isLoading.value = false
                _successMessage.value = "Versão $versionName carregada!"
            }.onFailure { exception ->
                _errorMessage.value = "Erro ao carregar versão: ${exception.message}"
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Adiciona um Pokémon ao time
     */
    fun addPokemonToTeam(pokemon: PokemonEntity) {
        viewModelScope.launch {
            val result = repository.addToTeam(pokemon)
            
            result.onSuccess {
                _successMessage.value = "${pokemon.name.replaceFirstChar { it.uppercase() }} adicionado ao time!"
            }.onFailure { exception ->
                _errorMessage.value = exception.message ?: "Erro ao adicionar ao time"
            }
        }
    }
    
    /**
     * Remove um Pokémon do time
     */
    fun removePokemonFromTeam(pokemonId: Int) {
        viewModelScope.launch {
            val result = repository.removeFromTeam(pokemonId)
            
            result.onSuccess {
                _successMessage.value = "Pokémon removido do time!"
            }.onFailure { exception ->
                _errorMessage.value = exception.message ?: "Erro ao remover do time"
            }
        }
    }
    
    /**
     * Verifica se um Pokémon está no time
     */
    suspend fun isPokemonInTeam(pokemonId: Int): Boolean {
        return repository.isPokemonInTeam(pokemonId)
    }
    
    /**
     * Carrega Pokémon do cache local
     */
    private fun loadFromCache() {
        repository.getAllPokemonFromDB().observeForever { pokemons ->
            if (pokemons.isNotEmpty()) {
                _pokemonList.value = pokemons
                _successMessage.value = "Carregado do cache local"
            }
        }
    }
    
    /**
     * Carrega Pokémon do cache por geração
     */
    private fun loadFromCacheByGeneration(generation: Int) {
        repository.getPokemonByGenerationFromDB(generation).observeForever { pokemons ->
            if (pokemons.isNotEmpty()) {
                _pokemonList.value = pokemons
                _successMessage.value = "Carregado do cache local"
            }
        }
    }
    
    /**
     * Limpa mensagem de erro
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
    
    /**
     * Limpa mensagem de sucesso
     */
    fun clearSuccessMessage() {
        _successMessage.value = null
    }
}
