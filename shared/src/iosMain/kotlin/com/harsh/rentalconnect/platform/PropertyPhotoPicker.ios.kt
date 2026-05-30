package com.harsh.rentalconnect.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.harsh.rentalconnect.domain.model.PickedPhoto
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSItemProvider
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.posix.memcpy
import platform.darwin.NSObject

@Composable
actual fun rememberPropertyPhotoPicker(
    onPhotosPicked: (List<PickedPhoto>) -> Unit,
    onError: (String) -> Unit,
): () -> Unit {
    return remember(onPhotosPicked, onError) {
        {
            presentPhotoPicker(
                onPhotosPicked = onPhotosPicked,
                onError = onError,
            )
        }
    }
}

private fun presentPhotoPicker(
    onPhotosPicked: (List<PickedPhoto>) -> Unit,
    onError: (String) -> Unit,
) {
    val presenter = topViewController() ?: run {
        onError("Photo picker is unavailable right now.")
        return
    }

    val configuration = PHPickerConfiguration().apply {
        selectionLimit = 10
        filter = PHPickerFilter.imagesFilter
    }
    val picker = PHPickerViewController(configuration)
    val delegate = PropertyPhotoPickerDelegate(
        onPhotosPicked = onPhotosPicked,
        onError = onError,
    )
    PropertyPhotoPickerRetainer.retain(delegate)
    picker.delegate = delegate
    presenter.presentViewController(picker, true, null)
}

private class PropertyPhotoPickerDelegate(
    private val onPhotosPicked: (List<PickedPhoto>) -> Unit,
    private val onError: (String) -> Unit,
) : NSObject(), PHPickerViewControllerDelegateProtocol {
    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>,
    ) {
        picker.dismissViewControllerAnimated(true, null)

        val results = didFinishPicking.filterIsInstance<PHPickerResult>()
        if (results.isEmpty()) {
            PropertyPhotoPickerRetainer.release(this)
            return
        }

        loadPickedPhotos(results)
    }

    private fun loadPickedPhotos(results: List<PHPickerResult>) {
        val photos = mutableListOf<PickedPhoto>()
        var completedCount = 0

        results.forEachIndexed { index, result ->
            val itemProvider = result.itemProvider
            itemProvider.loadDataRepresentationForTypeIdentifier("public.image") { data, error ->
                dispatch_async(dispatch_get_main_queue()) {
                    if (data != null) {
                        photos += PickedPhoto(
                            fileName = resolvedFileName(itemProvider, index),
                            mimeType = resolvedMimeType(itemProvider),
                            bytes = data.toByteArray(),
                        )
                    }

                    completedCount += 1
                    if (completedCount == results.size) {
                        when {
                            photos.isNotEmpty() -> onPhotosPicked(photos)
                            error != null -> onError(error.localizedDescription ?: "We could not load the selected photos.")
                            else -> onError("We could not load the selected photos.")
                        }
                        PropertyPhotoPickerRetainer.release(this)
                    }
                }
            }
        }
    }
}

private object PropertyPhotoPickerRetainer {
    private val delegates = mutableSetOf<Any>()

    fun retain(delegate: Any) {
        delegates += delegate
    }

    fun release(delegate: Any) {
        delegates -= delegate
    }
}

private fun topViewController(): UIViewController? {
    var controller = UIApplication.sharedApplication.keyWindow?.rootViewController
    while (controller?.presentedViewController != null) {
        controller = controller.presentedViewController
    }
    return controller
}

private fun resolvedFileName(
    itemProvider: NSItemProvider,
    index: Int,
): String {
    val suggested = itemProvider.suggestedName?.takeIf { it.isNotBlank() } ?: "property-photo-${index + 1}"
    return if ('.' in suggested) suggested else "$suggested.${resolvedMimeType(itemProvider).defaultExtension()}"
}

private fun resolvedMimeType(itemProvider: NSItemProvider): String {
    val lowerIdentifiers = itemProvider.registeredTypeIdentifiers.map { it.lowercase() }
    return when {
        lowerIdentifiers.any { "png" in it } -> "image/png"
        lowerIdentifiers.any { "webp" in it } -> "image/webp"
        lowerIdentifiers.any { "heic" in it || "heif" in it } -> "image/heic"
        else -> "image/jpeg"
    }
}

private fun String.defaultExtension(): String = when (this) {
    "image/png" -> "png"
    "image/webp" -> "webp"
    "image/heic" -> "heic"
    else -> "jpg"
}

private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    val byteArray = ByteArray(size)
    byteArray.usePinned { pinned ->
        memcpy(pinned.addressOf(0), bytes, length)
    }
    return byteArray
}
