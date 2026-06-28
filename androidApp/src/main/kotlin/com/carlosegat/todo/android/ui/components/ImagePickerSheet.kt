package com.carlosegat.todo.android.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerSheet(
    onDismiss: () -> Unit,
    onGallery: () -> Unit,
    onCamera: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        ListItem(
            headlineContent = { Text("Gallery") },
            leadingContent = { Icon(Icons.Default.PhotoLibrary, contentDescription = null) },
            modifier = Modifier.clickable(onClick = onGallery),
        )
        ListItem(
            headlineContent = { Text("Camera") },
            leadingContent = { Icon(Icons.Default.PhotoCamera, contentDescription = null) },
            modifier = Modifier.clickable(onClick = onCamera),
        )
    }
}
