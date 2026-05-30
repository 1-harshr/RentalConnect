package com.harsh.rentalconnect.platform

import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.harsh.rentalconnect.domain.model.PickedPhoto

@Composable
actual fun rememberPropertyPhotoPicker(
    onPhotosPicked: (List<PickedPhoto>) -> Unit,
    onError: (String) -> Unit,
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10),
    ) { uris ->
        val context = AndroidContextHolder.context
        if (context == null) {
            onError("Photo picker is unavailable on this device.")
            return@rememberLauncherForActivityResult
        }
        if (uris.isEmpty()) return@rememberLauncherForActivityResult

        val resolver = context.contentResolver
        val photos = uris.mapNotNull { uri ->
            runCatching {
                val mimeType = resolver.getType(uri)?.takeIf { it.startsWith("image/") } ?: "image/jpeg"
                val fileName = queryDisplayName(resolver, uri)
                    ?: "property-photo-${System.currentTimeMillis()}.${mimeType.toExtension()}"
                val bytes = resolver.openInputStream(uri)?.use { input -> input.readBytes() }
                    ?: return@runCatching null
                PickedPhoto(
                    fileName = fileName,
                    mimeType = mimeType,
                    bytes = bytes,
                )
            }.getOrNull()
        }

        if (photos.isEmpty()) {
            onError("We could not read the selected photos.")
        } else {
            onPhotosPicked(photos)
        }
    }

    return remember(launcher) {
        {
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        }
    }
}

private fun queryDisplayName(
    resolver: android.content.ContentResolver,
    uri: android.net.Uri,
): String? {
    val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
    return resolver.query(uri, projection, null, null, null)?.use { cursor ->
        val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (columnIndex >= 0 && cursor.moveToFirst()) cursor.getString(columnIndex) else null
    }
}

private fun String.toExtension(): String =
    MimeTypeMap.getSingleton().getExtensionFromMimeType(this).orEmpty().ifBlank { "jpg" }
