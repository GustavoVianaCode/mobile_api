package com.devmasterteam.mybooks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.entity.TeamEntity
import com.devmasterteam.mybooks.entity.TeamMemberEntity
import com.devmasterteam.mybooks.entity.TeamPokemonEntity
import com.devmasterteam.mybooks.repository.PokemonDatabase
import com.devmasterteam.mybooks.repository.PokemonRepository
import com.devmasterteam.mybooks.repository.TeamDao
import kotlinx.coroutines.launch

/**
 * ViewModel para gerenciar times de Pokémon
 * Gerencia tanto o sistema antigo (TeamPokemonEntity) quanto o novo (múltiplos times)
 */
class TeamViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = PokemonRepository.getInstance(application)
    private val teamDao: TeamDao = PokemonDatabase.getDatabase(application).teamDao()
    
    // ==================== SISTEMA ANTIGO ====================
    val teamPokemon: LiveData<List<TeamPokemonEntity>> = repository.getTeamPokemon()
    
    // ==================== SISTEMA NOVO (MÚLTIPLOS TIMES) ====================
    
    // LiveData para lista de todos os times
    val allTeams: LiveData<List<TeamEntity>> = teamDao.getAllTeams()
    
    // LiveData para times ativos
    val activeTeams: LiveData<List<TeamEntity>> = teamDao.getActiveTeams()
    
    // LiveData para mensagens
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    // LiveData para o time atualmente selecionado
    private val _selectedTeamId = MutableLiveData<Long>()
    val selectedTeamId: LiveData<Long> = _selectedTeamId
    
    // LiveData para Pokémon do time selecionado
    private val _teamPokemons = MutableLiveData<LiveData<List<PokemonEntity>>>()
    val teamPokemons: LiveData<LiveData<List<PokemonEntity>>> = _teamPokemons
    
    // ==================== MÉTODOS SISTEMA ANTIGO ====================
    
    /**
     * Remove um Pokémon do time (sistema antigo)
     */
    fun removePokemonFromTeam(pokemonId: Int, pokemonName: String) {
        viewModelScope.launch {
            val result = repository.removeFromTeam(pokemonId)
            
            result.onSuccess {
                _successMessage.value = "$pokemonName removido do time!"
            }.onFailure { exception ->
                _errorMessage.value = exception.message ?: "Erro ao remover do time"
            }
        }
    }
    
    /**
     * Retorna a contagem de Pokémon no time (sistema antigo)
     */
    fun getTeamCount(): LiveData<Int> {
        val count = MutableLiveData<Int>()
        viewModelScope.launch {
            count.value = repository.getTeamCount()
        }
        return count
    }
    
    // ==================== MÉTODOS SISTEMA NOVO ====================
    
    /**
     * Cria um novo time
     */
    fun createTeam(teamName: String) {
        android.util.Log.d("TeamViewModel", "========================================")
        android.util.Log.d("TeamViewModel", "createTeam() chamado com nome: '$teamName'")
        
        if (teamName.isBlank()) {
            android.util.Log.w("TeamViewModel", "Nome vazio - abortando")
            _errorMessage.value = "Nome do time não pode estar vazio"
            return
        }
        
        viewModelScope.launch {
            try {
                val team = TeamEntity(teamName = teamName.trim())
                android.util.Log.d("TeamViewModel", "Entidade criada: $team")
                
                val teamId = teamDao.insertTeam(team)
                android.util.Log.d("TeamViewModel", "✅ Time inserido no banco com ID: $teamId")
                
                _successMessage.value = "Time '$teamName' criado com sucesso!"
                _selectedTeamId.value = teamId
                
                android.util.Log.d("TeamViewModel", "LiveData allTeams deve atualizar automaticamente")
            } catch (e: Exception) {
                android.util.Log.e("TeamViewModel", "❌ ERRO ao criar time", e)
                _errorMessage.value = "Erro ao criar time: ${e.message}"
            }
        }
        android.util.Log.d("TeamViewModel", "========================================")
    }
    
    /**
     * Atualiza um time existente
     */
    fun updateTeam(team: TeamEntity) {
        viewModelScope.launch {
            try {
                teamDao.updateTeam(team)
                _successMessage.value = "Time atualizado!"
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao atualizar time: ${e.message}"
            }
        }
    }
    
    /**
     * Deleta um time
     */
    fun deleteTeam(team: TeamEntity) {
        viewModelScope.launch {
            try {
                teamDao.deleteTeam(team)
                _successMessage.value = "Time '${team.teamName}' deletado"
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao deletar time: ${e.message}"
            }
        }
    }
    
    /**
     * Adiciona um Pokémon ao time
     */
    fun addPokemonToTeam(teamId: Long, pokemonId: Int, teamName: String = "") {
        viewModelScope.launch {
            try {
                // Verificar se o time está cheio
                val currentSize = teamDao.getTeamSize(teamId)
                if (currentSize >= 6) {
                    _errorMessage.value = "Time cheio! Máximo de 6 Pokémon"
                    return@launch
                }
                
                // Verificar se já está no time
                val isInTeam = teamDao.isPokemonInTeam(teamId, pokemonId)
                if (isInTeam) {
                    _errorMessage.value = "Pokémon já está neste time"
                    return@launch
                }
                
                // Obter próxima posição
                val position = teamDao.getNextPosition(teamId)
                
                // Adicionar ao time
                val member = TeamMemberEntity(
                    teamId = teamId,
                    pokemonId = pokemonId,
                    position = position
                )
                
                teamDao.addPokemonToTeam(member)
                
                val message = if (teamName.isNotEmpty()) {
                    "Pokémon adicionado ao time '$teamName'!"
                } else {
                    "Pokémon adicionado ao time!"
                }
                _successMessage.value = message
                
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar Pokémon: ${e.message}"
            }
        }
    }
    
    /**
     * Remove um Pokémon do time específico
     */
    fun removePokemonFromTeam(teamId: Long, pokemonId: Int) {
        viewModelScope.launch {
            try {
                teamDao.removePokemonFromTeam(teamId, pokemonId)
                _successMessage.value = "Pokémon removido do time"
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao remover Pokémon: ${e.message}"
            }
        }
    }
    
    /**
     * Limpa todos os Pokémon de um time
     */
    fun clearTeam(teamId: Long) {
        viewModelScope.launch {
            try {
                teamDao.clearTeam(teamId)
                _successMessage.value = "Time limpo"
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao limpar time: ${e.message}"
            }
        }
    }
    
    /**
     * Seleciona um time e carrega seus Pokémon
     */
    fun selectTeam(teamId: Long) {
        _selectedTeamId.value = teamId
        _teamPokemons.value = teamDao.getTeamPokemons(teamId)
    }
    
    /**
     * Busca o tamanho de um time
     */
    suspend fun getTeamSize(teamId: Long): Int {
        return teamDao.getTeamSize(teamId)
    }
    
    /**
     * Verifica se um Pokémon está em um time
     */
    suspend fun isPokemonInTeam(teamId: Long, pokemonId: Int): Boolean {
        return teamDao.isPokemonInTeam(teamId, pokemonId)
    }
    
    /**
     * Busca todos os times que contêm um Pokémon
     */
    suspend fun getTeamsWithPokemon(pokemonId: Int): List<TeamEntity> {
        return teamDao.getTeamsWithPokemon(pokemonId)
    }
    
    /**
     * Limpa mensagens
     */
    fun clearMessages() {
        _successMessage.value = ""
        _errorMessage.value = ""
    }
}
