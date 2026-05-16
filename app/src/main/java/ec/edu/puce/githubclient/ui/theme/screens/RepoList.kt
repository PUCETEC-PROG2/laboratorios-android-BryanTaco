package ec.edu.puce.githubclient.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.ui.theme.components.RepoItem
import ec.edu.puce.githubclient.ui.theme.viewmodel.RepoUiState
import ec.edu.puce.githubclient.ui.theme.viewmodel.RepoViewModel

@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    repoViewModel: RepoViewModel = viewModel()
) {
    val uiState by repoViewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Repositorios",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 24.dp,
                bottom = 8.dp
            )
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
                val repos = (uiState as RepoUiState.Success).repos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(repos) { repo ->
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
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}