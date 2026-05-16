package ec.edu.puce.githubclient.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.ui.theme.model.Repo
import ec.edu.puce.githubclient.ui.theme.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RepoUiState {
    object Loading : RepoUiState()
    data class Success(val repos: List<Repo>) : RepoUiState()
    data class Error(val message: String) : RepoUiState()
}

class RepoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<RepoUiState>(RepoUiState.Loading)
    val uiState: StateFlow<RepoUiState> = _uiState

    init {
        fetchRepos()
    }

    private fun fetchRepos() {
        viewModelScope.launch {
            _uiState.value = RepoUiState.Loading
            try {
                val repos = RetrofitInstance.api.getRepos("BryanTaco")
                _uiState.value = RepoUiState.Success(repos)
            } catch (e: Exception) {
                _uiState.value = RepoUiState.Error("Error al cargar repositorios: ${e.message}")
            }
        }
    }
}