package com.kus.appkit.community.write.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.interop.LocalUIViewController
import com.kus.feature.community.ui.write.image.PlatformImagePicker
import com.kus.feature.community.ui.write.image.PlatformImagePickerFactory
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUUID
import platform.Foundation.writeToFile
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue


class IosImagePickerFactory : PlatformImagePickerFactory {
    @Composable
    override fun rememberPicker(): PlatformImagePicker {
        val presenter = LocalUIViewController.current

        var callback: ((String?) -> Unit)? by remember { mutableStateOf(null) }
        val delegateRef = remember { mutableStateOf<NSObject?>(null) }

        return remember(presenter) {
            PlatformImagePicker { onPicked ->
                callback = onPicked

                val config = PHPickerConfiguration(photoLibrary = PHPhotoLibrary.sharedPhotoLibrary()).apply {
                    setSelectionLimit(1)
                    setFilter(PHPickerFilter.imagesFilter())
                }

                val picker = PHPickerViewController(configuration = config)
                val delegate  = object : NSObject(), PHPickerViewControllerDelegateProtocol {
                    override fun picker(
                        picker: PHPickerViewController,
                        didFinishPicking: List<*>
                    ) {
                        picker.dismissViewControllerAnimated(true, completion = null)

                        val result = didFinishPicking.firstOrNull() as? PHPickerResult
                        val provider = result?.itemProvider
                        if (provider == null) {
                            dispatch_async(dispatch_get_main_queue()) { callback?.invoke(null) }
                            return
                        }

                        val typeId = "public.image"
                        if (!provider.hasItemConformingToTypeIdentifier(typeId)) {
                            dispatch_async(dispatch_get_main_queue()) { callback?.invoke(null) }
                            return
                        }

                        provider.loadDataRepresentationForTypeIdentifier(typeId) { data, error ->
                            if (data == null || error != null) {
                                dispatch_async(dispatch_get_main_queue()) { callback?.invoke(null) }
                                return@loadDataRepresentationForTypeIdentifier
                            }

                            val image = UIImage(data = data)
                            val jpeg = UIImageJPEGRepresentation(image, 0.9)
                            if (jpeg == null) {
                                dispatch_async(dispatch_get_main_queue()) { callback?.invoke(null) }
                                return@loadDataRepresentationForTypeIdentifier
                            }

                            val path = saveToTempFile(jpeg, "jpg")
                            dispatch_async(dispatch_get_main_queue()) { callback?.invoke(path) }
                        }
                    }
                }
                delegateRef.value = delegate
                picker.delegate = delegate

                presenter.presentViewController(picker, animated = true, completion = null)
            }
        }
    }
}

private fun saveToTempFile(data: NSData, ext: String): String? {
    val tempDir = NSTemporaryDirectory()
    val fileName = "picked_${NSUUID.UUID().UUIDString}.$ext"
    val fullPath = tempDir + fileName
    val ok = data.writeToFile(fullPath, atomically = true)
    return if (ok) fullPath else null
}