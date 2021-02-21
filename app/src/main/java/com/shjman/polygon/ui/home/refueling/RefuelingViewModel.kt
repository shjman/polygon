package com.shjman.polygon.ui.home.refueling

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class RefuelingViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val photoTakenUriSharedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val photoTakenUri = photoTakenUriSharedFlow.asSharedFlow()

    fun photoTakenUri(takenPhotoUri: String) {
        photoTakenUriSharedFlow.tryEmit(takenPhotoUri)
    }
}