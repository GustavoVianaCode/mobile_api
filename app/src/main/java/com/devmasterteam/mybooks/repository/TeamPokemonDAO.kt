package com.devmasterteam.mybooks.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmasterteam.mybooks.entity.TeamPokemonEntity

@Dao
interface TeamPokemonDAO {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teamPokemon: TeamPokemonEntity): Long
    
    @Delete
    suspend fun delete(teamPokemon: TeamPokemonEntity)
    
    @Query("SELECT * FROM TeamPokemon ORDER BY added_date DESC")
    fun getAllTeamPokemon(): LiveData<List<TeamPokemonEntity>>
    
    @Query("SELECT * FROM TeamPokemon WHERE pokemon_id = :pokemonId LIMIT 1")
    suspend fun getTeamPokemonByPokemonId(pokemonId: Int): TeamPokemonEntity?
    
    @Query("SELECT * FROM TeamPokemon WHERE team_id = :teamId")
    suspend fun getTeamPokemonById(teamId: Int): TeamPokemonEntity?
    
    @Query("DELETE FROM TeamPokemon WHERE pokemon_id = :pokemonId")
    suspend fun deleteByPokemonId(pokemonId: Int)
    
    @Query("SELECT COUNT(*) FROM TeamPokemon")
    suspend fun getTeamCount(): Int
    
    @Query("SELECT EXISTS(SELECT 1 FROM TeamPokemon WHERE pokemon_id = :pokemonId)")
    suspend fun isPokemonInTeam(pokemonId: Int): Boolean
    
    @Query("DELETE FROM TeamPokemon")
    suspend fun deleteAll()
}
