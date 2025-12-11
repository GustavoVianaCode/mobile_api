package com.devmasterteam.mybooks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.mybooks.entity.UserEntity
import com.devmasterteam.mybooks.repository.PokemonDatabase
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userDAO = PokemonDatabase.getDatabase(application).userDAO()
    
    private val _loginSuccess = MutableLiveData<UserEntity?>()
    val loginSuccess: LiveData<UserEntity?> = _loginSuccess
    
    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                if (username.isBlank() || password.isBlank()) {
                    _errorMessage.value = "Preencha todos os campos"
                    _isLoading.value = false
                    return@launch
                }
                
                val user = userDAO.login(username, password)
                if (user != null) {
                    _loginSuccess.value = user
                } else {
                    _errorMessage.value = "Usuário ou senha inválidos"
                }
                
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao fazer login: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun register(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Validações
                if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    _errorMessage.value = "Preencha todos os campos"
                    _isLoading.value = false
                    return@launch
                }
                
                if (username.length < 3) {
                    _errorMessage.value = "Usuário deve ter no mínimo 3 caracteres"
                    _isLoading.value = false
                    return@launch
                }
                
                if (!email.contains("@") || !email.contains(".")) {
                    _errorMessage.value = "Email inválido"
                    _isLoading.value = false
                    return@launch
                }
                
                if (password.length < 6) {
                    _errorMessage.value = "Senha deve ter no mínimo 6 caracteres"
                    _isLoading.value = false
                    return@launch
                }
                
                if (password != confirmPassword) {
                    _errorMessage.value = "As senhas não conferem"
                    _isLoading.value = false
                    return@launch
                }
                
                // Verificar se usuário já existe
                val existingUserByUsername = userDAO.getUserByUsername(username)
                if (existingUserByUsername != null) {
                    _errorMessage.value = "Usuário já existe"
                    _isLoading.value = false
                    return@launch
                }
                
                val existingUserByEmail = userDAO.getUserByEmail(email)
                if (existingUserByEmail != null) {
                    _errorMessage.value = "Email já cadastrado"
                    _isLoading.value = false
                    return@launch
                }
                
                // Criar novo usuário
                val newUser = UserEntity(
                    username = username,
                    email = email,
                    password = password
                )
                
                userDAO.insert(newUser)
                _registerSuccess.value = true
                _isLoading.value = false
                
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao criar usuário: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun resetMessages() {
        _errorMessage.value = ""
        _loginSuccess.value = null
        _registerSuccess.value = false
    }
}
