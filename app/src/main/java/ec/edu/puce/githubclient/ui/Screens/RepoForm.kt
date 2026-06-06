package ec.edu.puce.githubclient.ui.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoFormScreen(
    isEdit: Boolean,
    initialName: String = "",
    initialDescription: String = "",
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }
    var nameError by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val primaryColor = Color(0xFF5E5BA7)
    val secondaryTextColor = Color(0xFF757575)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isEdit) "Configuración del Repo" else "Nuevo Repositorio",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss, enabled = !isLoading) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = primaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono de cabecera decorativo
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(20.dp),
                color = primaryColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isEdit) Icons.Outlined.Info else Icons.Outlined.DriveFileRenameOutline,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = primaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isEdit) "Actualiza los detalles de tu proyecto" else "Comienza un nuevo proyecto en GitHub",
                style = TextStyle(fontSize = 14.sp, color = secondaryTextColor),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Sección del Nombre
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nombre del repositorio",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryColor),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = if (it.isBlank()) "El nombre es obligatorio" else null
                    },
                    placeholder = { Text("ej. mi-proyecto-increible", color = Color.LightGray) },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Outlined.DriveFileRenameOutline, contentDescription = null, tint = primaryColor) },
                    isError = nameError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color.LightGray,
                        disabledBorderColor = Color(0xFFF5F5F5),
                        disabledTextColor = Color.Gray,
                        errorBorderColor = Color.Red
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
                
                AnimatedVisibility(
                    visible = nameError != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = nameError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp, start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de la Descripción
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Descripción (Opcional)",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryColor),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { if (it.length <= 150) description = it },
                    placeholder = { Text("¿De qué trata este repositorio?", color = Color.LightGray) },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Outlined.Description, contentDescription = null, tint = primaryColor) },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    supportingText = {
                        Text(
                            text = "${description.length}/150 caracteres",
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(fontSize = 10.sp, color = secondaryTextColor)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botones de Acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    enabled = !isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, Color.LightGray)
                ) {
                    Text("Cancelar", style = TextStyle(fontWeight = FontWeight.SemiBold, color = Color.Gray))
                }

                Button(
                    onClick = {
                        if (!isEdit && name.isBlank()) {
                            nameError = "El nombre es obligatorio"
                            return@Button
                        }
                        onConfirm(name, description)
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = if (isEdit) "Actualizar" else "Crear Repo",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
