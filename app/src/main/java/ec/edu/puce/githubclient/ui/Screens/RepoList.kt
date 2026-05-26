package ec.edu.puce.githubclient.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.ui.Components.RepoItem
import ec.edu.puce.githubclient.models.Owner
import ec.edu.puce.githubclient.models.Repo
import ec.edu.puce.githubclient.ViewModels.RepoUiState
import ec.edu.puce.githubclient.ViewModels.RepoListViewModel

@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    repoViewModel: RepoListViewModel = viewModel(),
    onNavigateToForm: () -> Unit ={}
) {
    val uiState by repoViewModel.uiState.collectAsState()
    var showForm by remember { mutableStateOf(false) }
    var localRepos by remember { mutableStateOf(listOf<Repo>()) }

    if (showForm) {
        RepoForm(
            onDismiss = { showForm = false },
            onConfirm = { nombre, descripcion ->
                val nuevoRepo = Repo(
                    id = System.currentTimeMillis(),
                    name = nombre,
                    description = descripcion,
                    language = null,
                    owner = Owner(
                        login = "BryanTaco",
                        avatarUrl = "https://github.com/BryanTaco.png"
                    ),
                    updatedAt = ""
                )
                localRepos = listOf(nuevoRepo) + localRepos
                showForm = false
            }
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showForm = true },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar repositorio",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Repositorios",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(
                    start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp
                )
            )

            Text(
                text = "BryanTaco · últimos repos",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
            )

            when (uiState) {
                is RepoUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is RepoUiState.Success -> {
                    val apiRepos = (uiState as RepoUiState.Success).repos
                    val allRepos = localRepos + apiRepos
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(allRepos) { repo ->
                            RepoItem(repo = repo)
                        }
                    }
                }

                is RepoUiState.Error -> {
                    val message = (uiState as RepoUiState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}