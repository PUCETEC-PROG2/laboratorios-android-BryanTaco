package ec.edu.puce.githubclient.ui.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ec.edu.puce.githubclient.models.Repo

@Composable
fun RepoItem(
    repo: Repo,
    onEditClick: (Repo) -> Unit,
    onDeleteClick: (Repo) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Columna Izquierda: Imagen + Botones
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(100.dp)
            ) {
                AsyncImage(
                    model = repo.owner?.avatarUrl ?: "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { onEditClick(repo) }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar",
                            tint = Color(0xFF64B5F6) // Color celeste/azul claro
                        )
                    }
                    IconButton(onClick = { onDeleteClick(repo) }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Eliminar",
                            tint = Color(0xFFE57373) // Color rojizo
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Columna Derecha: Información
            Column(modifier = Modifier.weight(1f)) {
                RepoInfoRow(label = "Nombre del repositorio", value = repo.name)
                Spacer(modifier = Modifier.height(12.dp))
                RepoInfoRow(label = "Descripción del repositorio", value = repo.description ?: "Sin descripción")
                Spacer(modifier = Modifier.height(12.dp))
                RepoInfoRow(label = "Lenguaje", value = repo.language ?: "No especificado")
            }
        }
    }
}

@Composable
fun RepoInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.width(100.dp),
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f),
            lineHeight = 16.sp
        )
    }
}
