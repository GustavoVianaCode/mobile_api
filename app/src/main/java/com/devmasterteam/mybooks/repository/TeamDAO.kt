package com.devmasterteam.mybooks.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.entity.TeamEntity
import com.devmasterteam.mybooks.entity.TeamMemberEntity

/**
 * DAO para operações com Times de Pokémon
 */
@Dao
interface TeamDao {
    
    // ==================== OPERAÇÕES DE TIME ====================
    
    /**
     * Insere um novo time
     * @return O ID do time criado
     */
    @Insert
    suspend fun insertTeam(team: TeamEntity): Long
    
    /**
     * Atualiza um time existente
     */
    @Update
    suspend fun updateTeam(team: TeamEntity)
    
    /**
     * Deleta um time
     */
    @Delete
    suspend fun deleteTeam(team: TeamEntity)
    
    /**
     * Busca todos os times ordenados por data de criação
     */
    @Query("SELECT * FROM teams ORDER BY createdAt DESC")
    fun getAllTeams(): LiveData<List<TeamEntity>>
    
    /**
     * Busca todos os times (versão síncrona para debug)
     */
    @Query("SELECT * FROM teams ORDER BY createdAt DESC")
    suspend fun getAllTeamsSync(): List<TeamEntity>
    
    /**
     * Busca um time específico por ID
     */
    @Query("SELECT * FROM teams WHERE teamId = :teamId")
    suspend fun getTeamById(teamId: Long): TeamEntity?
    
    /**
     * Busca times ativos
     */
    @Query("SELECT * FROM teams WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getActiveTeams(): LiveData<List<TeamEntity>>
    
    // ==================== OPERAÇÕES DE MEMBROS ====================
    
    /**
     * Adiciona um Pokémon ao time
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPokemonToTeam(member: TeamMemberEntity): Long
    
    /**
     * Remove um Pokémon do time
     */
    @Query("DELETE FROM team_members WHERE teamId = :teamId AND pokemonId = :pokemonId")
    suspend fun removePokemonFromTeam(teamId: Long, pokemonId: Int)
    
    /**
     * Remove todos os Pokémon de um time
     */
    @Query("DELETE FROM team_members WHERE teamId = :teamId")
    suspend fun clearTeam(teamId: Long)
    
    /**
     * Busca todos os Pokémon de um time ordenados por posição
     */
    @Query("""
        SELECT p.* FROM Pokemon p
        INNER JOIN team_members tm ON p.id = tm.pokemonId
        WHERE tm.teamId = :teamId
        ORDER BY tm.position ASC
    """)
    fun getTeamPokemons(teamId: Long): LiveData<List<PokemonEntity>>
    
    /**
     * Busca todos os Pokémon de um time (versão síncrona para uso em suspend functions)
     */
    @Query("""
        SELECT p.* FROM Pokemon p
        INNER JOIN team_members tm ON p.id = tm.pokemonId
        WHERE tm.teamId = :teamId
        ORDER BY tm.position ASC
    """)
    suspend fun getTeamPokemonsSync(teamId: Long): List<PokemonEntity>
    
    /**
     * Busca a contagem de Pokémon em um time
     */
    @Query("SELECT COUNT(*) FROM team_members WHERE teamId = :teamId")
    suspend fun getTeamSize(teamId: Long): Int
    
    /**
     * Verifica se um Pokémon está em um time específico
     */
    @Query("SELECT EXISTS(SELECT 1 FROM team_members WHERE teamId = :teamId AND pokemonId = :pokemonId)")
    suspend fun isPokemonInTeam(teamId: Long, pokemonId: Int): Boolean
    
    /**
     * Busca todos os times que contêm um Pokémon específico
     */
    @Query("""
        SELECT t.* FROM teams t
        INNER JOIN team_members tm ON t.teamId = tm.teamId
        WHERE tm.pokemonId = :pokemonId
    """)
    suspend fun getTeamsWithPokemon(pokemonId: Int): List<TeamEntity>
    
    /**
     * Busca a próxima posição disponível em um time
     */
    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM team_members WHERE teamId = :teamId")
    suspend fun getNextPosition(teamId: Long): Int
    
    /**
     * Atualiza a posição de um Pokémon no time
     */
    @Query("UPDATE team_members SET position = :newPosition WHERE teamId = :teamId AND pokemonId = :pokemonId")
    suspend fun updateMemberPosition(teamId: Long, pokemonId: Int, newPosition: Int)
}
