package ec.edu.puce.githubclient.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.GithubUser
import ec.edu.puce.githubclient.models.Repo
import kotlinx.coroutines.delay
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

    // Datos estáticos (hardcoded) iniciales como pide el laboratorio
    private val staticUser = GithubUser(
        login = "EstudiantePUCE",
        avatarUrl = "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"
    )

    private val initialRepos = mutableListOf(
        Repo(
            id = 1,
            name = "examen-parcial-django-vistas-templates-y-modelos-jonathantello-star",
            description = "desarrollo-web-2026-1-examen-parcial-django-vistas-templates-y-modelos-pucetec-prog2-202402-examen-p created by GitHub Classroom",
            language = "Python",
            owner = staticUser
        ),
        Repo(
            id = 2,
            name = "examen-parcial-django-vistas-templates-y-modelos-Alan0qwe",
            description = "desarrollo-web-2026-1-examen-parcial-django-vistas-templates-y-modelos-pucetec-prog2-202402-examen-p created by GitHub Classroom",
            language = "Python",
            owner = staticUser
        ),
        Repo(
            id = 3,
            name = "laboratorio-django-Alan0qwe",
            description = "pucetec-desarrollo-movil-2026-1-laboratorio-django-lab4-template created by GitHub Classroom",
            language = "No especificado",
            owner = staticUser
        )
    )

    init {
        loadStaticData()
    }

    private fun loadStaticData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1000) // Simulamos una pequeña carga
            _uiState.value = _uiState.value.copy(
                repos = initialRepos.toList(),
                user = staticUser,
                isLoading = false
            )
        }
    }

    fun createRepo(name: String, description: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1000) // Simulación de red
            
            val newRepo = Repo(
                id = (initialRepos.maxOfOrNull { it.id } ?: 0) + 1,
                name = name,
                description = description,
                language = "Kotlin",
                owner = staticUser
            )
            
            initialRepos.add(0, newRepo) // Añadir al inicio
            _uiState.value = _uiState.value.copy(
                repos = initialRepos.toList(),
                isLoading = false,
                successMessage = "Repositorio '$name' creado (Simulado)"
            )
        }
    }

    fun updateRepo(repoName: String, description: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(800)
            
            val index = initialRepos.indexOfFirst { it.name == repoName }
            if (index != -1) {
                val updatedRepo = initialRepos[index].copy(description = description)
                initialRepos[index] = updatedRepo
                
                _uiState.value = _uiState.value.copy(
                    repos = initialRepos.toList(),
                    isLoading = false,
                    successMessage = "Repositorio '$repoName' actualizado (Simulado)"
                )
            }
        }
    }

    fun deleteRepo(repoName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(800)
            
            initialRepos.removeAll { it.name == repoName }
            _uiState.value = _uiState.value.copy(
                repos = initialRepos.toList(),
                isLoading = false,
                successMessage = "Repositorio '$repoName' eliminado (Simulado)"
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
