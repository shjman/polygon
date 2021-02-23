package com.shjman.polygon.ui.home.refueling

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shjman.polygon.data.repository.RefuelingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class RefuelingViewModel @Inject constructor(
    application: Application,
    private val refuelingRepository: RefuelingRepository,
) : AndroidViewModel(application) {

    private val photoTakenUriSharedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    private val refuelingDateFlow = MutableSharedFlow<ZonedDateTime>(extraBufferCapacity = 1)
    val takenPhotoUri = photoTakenUriSharedFlow.asSharedFlow()
    val refuelingDate = refuelingDateFlow.asSharedFlow()

    fun photoTakenUri(takenPhotoUri: String) {
        photoTakenUriSharedFlow.tryEmit(takenPhotoUri)
    }

    fun refuelingDate(zonedDateTime: ZonedDateTime) {
        refuelingDateFlow.tryEmit(zonedDateTime)
    }

    suspend fun saveData(date: Instant, mileage: Int, fuelType: String, numberOfLiters: Double, price: Double, photoTakenUri: String?) = withContext(Dispatchers.IO) {
        refuelingRepository.addRefueling(date, mileage, fuelType, numberOfLiters, price, photoTakenUri)
    }
}