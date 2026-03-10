package com.kus.feature.community.ui.write.image

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class AndroidImagePickerFactory : PlatformImagePickerFactory {
    @Composable
    override fun rememberPicker(): PlatformImagePicker {
        var pendingPicked by remember { mutableStateOf<((String?) -> Unit)?>(null) }
        var pendingAfterPermission by remember { mutableStateOf<(() -> Unit)?>(null) }

        val pickVisualLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            pendingPicked?.invoke(uri?.toString())
            pendingPicked = null
        }

        val getContentLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            pendingPicked?.invoke(uri?.toString())
            pendingPicked = null
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) pendingAfterPermission?.invoke()
            else pendingPicked?.invoke(null)
            pendingAfterPermission = null
            pendingPicked = null
        }

        val context = LocalContext.current

        return remember {
            PlatformImagePicker { onPicked ->
                pendingPicked = onPicked

                if (Build.VERSION.SDK_INT >= 33) {
                    pickVisualLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                    return@PlatformImagePicker
                }

                val perm = android.Manifest.permission.READ_EXTERNAL_STORAGE
                val granted = ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
                val doPick = { getContentLauncher.launch("image/*") }

                if (granted) doPick()
                else {
                    pendingAfterPermission = doPick
                    permissionLauncher.launch(perm)
                }
            }
        }
    }
}