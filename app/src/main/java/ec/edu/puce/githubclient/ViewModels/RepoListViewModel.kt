package ec.edu.puce.githubclient.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.BuildConfig
import ec.edu.puce.githubclient.Services.ApiService
import ec.edu.puce.githubclient.models.GithubUser
import ec.edu.puce.githubclient.models.Repo
import ec.edu.puce.githubclient.models.RepositoryPayload
import ec.edu.puce.githubclient.models.UpdateRepoPayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RepoUiState(
    val repos: List<Repo> = emptyList(),
    val user: GithubUser? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class RepoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RepoUiState())
    val uiState: StateFlow<RepoUiState> = _uiState

    // Configuración de la API real
    private val apiService = ApiService.create()
    private val token = "Bearer ${BuildConfig.GITHUB_TOKEN}"

    init {
        loadData()
    }

    // CARGAR DATOS REALES DE GITHUB
    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // 1. Obtener usuario (para saber quién es el owner)
                val userRes = apiService.getAuthenticatedUser(token)
                if (userRes.isSuccessful) {
                    val user = userRes.body()
                    _uiState.value = _uiState.value.copy(user = user)

                    // 2. Obtener sus repositorios
                    val reposRes = apiService.getUserRepos(token)
                    if (reposRes.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            repos = reposRes.body() ?: emptyList(),
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Error de Token: Verifica que sea válido en local.properties"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Sin conexión: ${e.localizedMessage}"
                )
            }
        }
    }

    // CREAR REAL (POST)
    fun createRepo(name: String, description: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = apiService.createRepo(token, RepositoryPayload(name, description))
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(successMessage = "Repo '$name' creado en GitHub")
                    loadData()
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Error al crear")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.localizedMessage)
            }
        }
    }

    // ACTUALIZAR REAL (PATCH)
    fun updateRepo(oldName: String, newName: String, description: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val owner = _uiState.value.user?.login ?: return@launch
                val response = apiService.updateRepo(token, owner, oldName, UpdateRepoPayload(newName, description))
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(successMessage = "Repo actualizado en GitHub")
                    loadData()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.localizedMessage)
            }
        }
    }

    // ELIMINAR REAL (DELETE)
    fun deleteRepo(repoName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val owner = _uiState.value.user?.login ?: return@launch
                val response = apiService.deleteRepo(token, owner, repoName)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(successMessage = "Repo '$repoName' eliminado de GitHub")
                    loadData()
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Error: ¿El token tiene permiso 'delete_repo'?")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.localizedMessage)
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
