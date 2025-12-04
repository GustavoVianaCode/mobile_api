package com.devmasterteam.mybooks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = PokemonRepository.getInstance(application)
    
    private val _pokemon = MutableLiveData<PokemonEntity>()
    val pokemon: LiveData<PokemonEntity> = _pokemon
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage
    
    private val _isInTeam = MutableLiveData<Boolean>()
    val isInTeam: LiveData<Boolean> = _isInTeam
    
    private val _showShiny = MutableLiveData<Boolean>(false)
    val showShiny: LiveData<Boolean> = _showShiny
    
    /**
     * Carrega os detalhes de um Pokémon
     */
    fun loadPokemonDetails(pokemonId: Int) {
        _isLoading.value = true
        
        viewModelScope.launch {
            val result = repository.getPokemonDetails(pokemonId)
            
            result.onSuccess { pokemonEntity ->
                _pokemon.value = pokemonEntity
                _isLoading.value = false
                checkIfInTeam(pokemonId)
            }.onFailure { exception ->
                _errorMessage.value = "Erro ao carregar detalhes: ${exception.message}"
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Adiciona/Remove Pokémon do time
     */
    fun toggleTeam() {
        val currentPokemon = _pokemon.value ?: return
        
        viewModelScope.launch {
            if (_isInTeam.value == true) {
                // Remover do time
                val result = repository.removeFromTeam(currentPokemon.id)
                result.onSuccess {
                    _isInTeam.value = false
                    _successMessage.value = "${currentPokemon.name.replaceFirstChar { it.uppercase() }} removido do time!"
                }.onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            } else {
                // Adicionar ao time
                val result = repository.addToTeam(currentPokemon)
                result.onSuccess {
                    _isInTeam.value = true
                    _successMessage.value = "${currentPokemon.name.replaceFirstChar { it.uppercase() }} adicionado ao time!"
                }.onFailure { exception ->
                    _errorMessage.value = exception.message
                }
            }
        }
    }
    
    /**
     * Alterna entre sprite normal e shiny
     */
    fun toggleShiny() {
        _showShiny.value = !(_showShiny.value ?: false)
    }
    
    /**
     * Mostra versão shiny
     */
    fun showShiny() {
        _showShiny.value = true
    }
    
    /**
     * Mostra versão normal
     */
    fun showNormal() {
        _showShiny.value = false
    }
    
    /**
     * Verifica se o Pokémon está no time
     */
    private fun checkIfInTeam(pokemonId: Int) {
        viewModelScope.launch {
            _isInTeam.value = repository.isPokemonInTeam(pokemonId)
        }
    }
    
    /**
     * Retorna a URL do sprite correto (normal ou shiny)
     */
    fun getCurrentSpriteUrl(): String {
        val currentPokemon = _pokemon.value ?: return ""
        return if (_showShiny.value == true) {
            currentPokemon.shinySpriteUrl
        } else {
            currentPokemon.spriteUrl
        }
    }
    
    /**
     * Formata os tipos do Pokémon
     */
    fun getFormattedTypes(): String {
        return _pokemon.value?.types?.split(",")?.joinToString(" • ") { 
            it.replaceFirstChar { char -> char.uppercase() }
        } ?: ""
    }
    
    /**
     * Formata altura (decímetros para metros)
     */
    fun getFormattedHeight(): String {
        val height = _pokemon.value?.height ?: 0
        return String.format("%.1f m", height / 10.0)
    }
    
    /**
     * Formata peso (hectogramas para kg)
     */
    fun getFormattedWeight(): String {
        val weight = _pokemon.value?.weight ?: 0
        return String.format("%.1f kg", weight / 10.0)
    }
    
    /**
     * Limpa mensagens
     */
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}
