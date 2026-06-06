package ec.edu.puce.githubclient.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.ViewModels.RepoViewModel
import ec.edu.puce.githubclient.models.Repo
import ec.edu.puce.githubclient.ui.Components.RepoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoList(viewModel: RepoViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showCreateDialog by remember { mutableStateOf(false) }
    var repoToEdit by remember { mutableStateOf<Repo?>(null) }
    var repoToDelete by remember { mutableStateOf<Repo?>(null) }

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessages() }
        uiState.successMessage?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessages() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitHub Client") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Color(0xFF64B5F6), // Color azul del FAB
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo repositorio")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF5F5F5) // Fondo ligeramente gris/azul
    ) { padding ->

        Box(modifier = Modifier.padding(padding).fillMaxSize()) {

            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.repos.isEmpty() -> Text(
                    "No hay repositorios. Toca + para crear uno.",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.repos, key = { it.id }) { repo ->
                        RepoItem(
                            repo = repo,
                            onEditClick = { repoToEdit = it },
                            onDeleteClick = { repoToDelete = it }
                        )
                    }
                }
            }
        }
    }

    // Pantalla CREAR
    if (showCreateDialog) {
        RepoFormScreen(
            isEdit = false,
            isLoading = uiState.isLoading,
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, desc ->
                viewModel.createRepo(name, desc)
                // No cerramos inmediatamente para permitir que el loading se vea
            }
        )
    }

    // Cerrar diálogos cuando la operación sea exitosa (basado en el successMessage)
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            showCreateDialog = false
            repoToEdit = null
        }
    }

    // Pantalla EDITAR
    repoToEdit?.let { repo ->
        RepoFormScreen(
            isEdit = true,
            initialName = repo.name,
            initialDescription = repo.description ?: "",
            isLoading = uiState.isLoading,
            onDismiss = { repoToEdit = null },
            onConfirm = { newName, desc ->
                viewModel.updateRepo(repo.name, newName, desc)
            }
        )
    }

    // Diálogo CONFIRMAR ELIMINAR
    repoToDelete?.let { repo ->
        AlertDialog(
            onDismissRequest = { repoToDelete = null },
            title = { Text("Eliminar repositorio") },
            text = { Text("¿Seguro que deseas eliminar '${repo.name}'? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteRepo(repo.name); repoToDelete = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { repoToDelete = null }) { Text("Cancelar") }
            }
        )
    }
}