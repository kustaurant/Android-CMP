package com.kus.feature.community.ui.write.image

fun interface PlatformImagePicker {
    fun pickImage(onPicked: (String?) -> Unit)
}