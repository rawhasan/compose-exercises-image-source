package com.example.imagesource

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ImageViewModel: ViewModel() {
    private var _currentlySelectedImageUri = mutableStateOf<Uri?>(null)
    private var _previousImageUri: Uri? = null

    fun setCurrentlySelectedImageUri(uri: Uri?) {
        _currentlySelectedImageUri.value = uri
    }

    fun getCurrentlySelectedImageUri(): Uri? {
        return _currentlySelectedImageUri.value
    }

    fun setPreviousImageUri(uri: Uri?) {
        _previousImageUri = uri
    }

    fun getPreviousImageUri(): Uri? {
        return _previousImageUri
    }
}