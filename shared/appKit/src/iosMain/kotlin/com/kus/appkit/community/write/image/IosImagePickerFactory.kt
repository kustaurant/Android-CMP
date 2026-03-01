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


class IosImagePickerFactory : PlatformImagePickerFactory {
    @Composable
    override fun rememberPicker(): PlatformImagePicker {
        val presenter = LocalUIViewController.current
        var callback by remember { mutableStateOf<(String?) -> Unit>({}) }

        return remember(presenter) {
            PlatformImagePicker { onPicked ->
                callback = onPicked

                val config = PHPickerConfiguration(photoLibrary = PHPhotoLibrary.sharedPhotoLibrary()).apply {
                    setSelectionLimit(1)
                    setFilter(PHPickerFilter.imagesFilter())
                }

                val picker = PHPickerViewController(configuration = config)
                picker.delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
                    override fun picker(
                        picker: PHPickerViewController,
                        didFinishPicking: List<*>
                    ) {
                        picker.dismissViewControllerAnimated(true, completion = null)

                        val result = didFinishPicking.firstOrNull() as? PHPickerResult
                        val provider = result?.itemProvider
                        if (provider == null) {
                            callback(null); return
                        }

                        // ✅ iOS 14+: UTType.image.identifier 로 data 로드
                        val typeId = "public.image"

                        if (!provider.hasItemConformingToTypeIdentifier(typeId)) {
                            callback(null)
                            return
                        }

                        provider.loadDataRepresentationForTypeIdentifier(typeId) { data, error ->
                            if (data == null || error != null) {
                                callback(null)
                                return@loadDataRepresentationForTypeIdentifier
                            }

                            val image = UIImage(data = data)
                            val jpeg = UIImageJPEGRepresentation(image, 0.9)

                            if (jpeg == null) {
                                callback(null)
                                return@loadDataRepresentationForTypeIdentifier
                            }

                            val path = saveToTempFile(jpeg, "jpg")
                            callback(path)
                        }
                    }
                }

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