package com.devmasterteam.mybooks.api

import com.devmasterteam.mybooks.api.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    
    // Lista Pokémon com paginação
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<PokemonListResponse>
    
    // Detalhes de um Pokémon específico
    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): Response<PokemonDetails>
    
    @GET("pokemon/{name}")
    suspend fun getPokemonDetailsByName(@Path("name") name: String): Response<PokemonDetails>
    
    // Geração de Pokémon (1-9)
    @GET("generation/{id}")
    suspend fun getGeneration(@Path("id") generationId: Int): Response<GenerationResponse>
    
    // Versão de jogo
    @GET("version/{id}")
    suspend fun getVersion(@Path("id") versionId: Int): Response<VersionResponse>
    
    @GET("version/{name}")
    suspend fun getVersionByName(@Path("name") versionName: String): Response<VersionResponse>
    
    // Version Group
    @GET("version-group/{id}")
    suspend fun getVersionGroup(@Path("id") versionGroupId: Int): Response<VersionGroupResponse>
    
    @GET("version-group/{name}")
    suspend fun getVersionGroupByName(@Path("name") versionGroupName: String): Response<VersionGroupResponse>
    
    // Pokedex
    @GET("pokedex/{id}")
    suspend fun getPokedex(@Path("id") pokedexId: Int): Response<PokedexResponse>
    
    @GET("pokedex/{name}")
    suspend fun getPokedexByName(@Path("name") pokedexName: String): Response<PokedexResponse>
}
