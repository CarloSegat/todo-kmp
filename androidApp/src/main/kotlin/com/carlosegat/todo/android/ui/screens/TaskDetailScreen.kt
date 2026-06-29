package com.carlosegat.todo.android.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.carlosegat.todo.android.TaskViewModel
import com.carlosegat.todo.android.ui.components.ImagePickerSheet
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    todoId: String,
    viewModel: TaskViewModel,
    onBack: () -> Unit,
) {
    val todos by viewModel.todos.collectAsState()
    val todo = todos.find { it.id == todoId }
    val clipboard = LocalClipboardManager.current
    // LocalContext.current = the current Android Context (the Activity), pulled from a
    // CompositionLocal; needed because Toast.makeText() requires a Context.
    val context = LocalContext.current

    var showImageSheet by remember { mutableStateOf(false) }
    // the camera writes the photo into a file we create; remember it so the result callback can read it back
    var pendingCameraFile by remember { mutableStateOf<File?>(null) }

    // Activity Result launchers: each is a handle you .launch() to start another app/activity;
    // the lambda is the result callback. remember keeps the registration alive across
    // recompositions so the result is routed back here (even after rotation/process death).

    // Gallery: PickVisualMedia opens the system photo picker. Its result IS the picked image's
    // content:// Uri (or null if the user cancelled), which we save into state.
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) viewModel.setImagePath(todoId, uri.toString())
    }
    // Camera: TakePicture opens the camera app, which writes the photo into the Uri we pass to
    // .launch(). Its result is only a Boolean (was it saved?), NOT the image — so on success we
    // store the file:// path of the file we created ourselves (pendingCameraFile).
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) pendingCameraFile?.let { viewModel.setImagePath(todoId, Uri.fromFile(it).toString()) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        if (todo == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                Text("Task not found")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            // The description is edited into a local draft and flushed to the backend ONCE, when we
            // leave this screen
            var draftDescription by remember { mutableStateOf(todo.description) }

            // Flush once on leave
            DisposableEffect(todoId) {
                onDispose {
                    // if we wrote a bare "return" we would have been returning
                    // from the TaskDetailScreen composable function
                    // which is potentially problematic because it would interrupt
                    // the execution of its tear-down code
                    val current = todos.find { it.id == todoId } ?: return@onDispose
                    if (current.description != draftDescription) {
                        viewModel.update(current.copy(description = draftDescription))
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = todo.done, onCheckedChange = { viewModel.toggle(todo.id) })
                Text(todo.title, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(16.dp))
            Text("Created: ${formatTimestamp(todo.createdAt)}")
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = draftDescription,
                onValueChange = { draftDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))
            if (todo.imagePath != null) {
                AsyncImage(
                    model = todo.imagePath,
                    contentDescription = "Task image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )
                TextButton(onClick = { viewModel.setImagePath(todo.id, null) }) {
                    Text("Remove picture")
                }
            }
            Button(onClick = { showImageSheet = true }) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add picture")
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    clipboard.setText(AnnotatedString("todoapp://task/${todo.id}"))
                    Toast.makeText(context, "Link copied", Toast.LENGTH_SHORT).show()
                },
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Share (deeplink)")
            }
        }
    }

    if (showImageSheet) {
        ImagePickerSheet(
            onDismiss = { showImageSheet = false },
            onGallery = {
                showImageSheet = false
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            },
            onCamera = {
                showImageSheet = false
                val file = createImageFile(context)
                pendingCameraFile = file
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                cameraLauncher.launch(uri)
            },
        )
    }
}

private fun createImageFile(context: Context): File {
    val dir = File(context.cacheDir, "images").apply { mkdirs() }
    return File.createTempFile("todo_", ".jpg", dir)
}

private fun formatTimestamp(epochMillis: Long): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(epochMillis))
