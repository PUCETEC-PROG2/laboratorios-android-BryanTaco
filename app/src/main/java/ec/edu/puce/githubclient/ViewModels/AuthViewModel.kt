package ec.edu.puce.githubclient.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.Services.ApiService
import ec.edu.puce.githubclient.models.GithubUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val token: String, val user: GithubUser) : AuthState()
    data class Error(val message: String) : AuthState()
    object GuestMode : AuthState()
}

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val apiService = ApiService.create()

    fun login(token: String) {
        if (token.isBlank()) {
            _authState.value = AuthState.Error("El token no puede estar vacío")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
                val response = apiService.getAuthenticatedUser(formattedToken)
                
                if (response.isSuccessful && response.body() != null) {
                    _authState.value = AuthState.Success(formattedToken, response.body()!!)
                } else {
                    _authState.value = AuthState.Error("Token inválido o error de red (${response.code()})")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun enterAsGuest() {
        _authState.value = AuthState.GuestMode
    }

    fun logout() {
        _authState.value = AuthState.Idle
    }
}
