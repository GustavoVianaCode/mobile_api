package com.devmasterteam.mybooks.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devmasterteam.mybooks.entity.PokemonEntity

@Dao
interface PokemonDAO {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)
    
    @Update
    suspend fun update(pokemon: PokemonEntity)
    
    @Delete
    suspend fun delete(pokemon: PokemonEntity)
    
    @Query("SELECT * FROM Pokemon WHERE id = :pokemonId")
    suspend fun getPokemonById(pokemonId: Int): PokemonEntity?
    
    @Query("SELECT * FROM Pokemon ORDER BY id ASC")
    fun getAllPokemon(): LiveData<List<PokemonEntity>>
    
    @Query("SELECT * FROM Pokemon WHERE generation = :generation ORDER BY id ASC")
    fun getPokemonByGeneration(generation: Int): LiveData<List<PokemonEntity>>
    
    @Query("SELECT * FROM Pokemon ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPokemonPaginated(limit: Int, offset: Int): List<PokemonEntity>
    
    @Query("DELETE FROM Pokemon")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM Pokemon")
    suspend fun getCount(): Int
}
