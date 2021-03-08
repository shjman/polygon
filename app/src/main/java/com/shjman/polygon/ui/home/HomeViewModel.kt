package com.shjman.polygon.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shjman.polygon.R
import com.shjman.polygon.common.viewmodel.validation.Validators
import com.shjman.polygon.common.viewmodel.validation.validation
import com.shjman.polygon.data.repository.CarDescriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val carDescriptionRepository: CarDescriptionRepository,
) : AndroidViewModel(application) {

    val carModelValidation by validation(Validators.NotEmptyValidator(R.string.field_is_empty))

    suspend fun onAddModelClicked() = withContext(Dispatchers.IO) {
        carDescriptionRepository.addCarDescription(carModelValidation.value)
    }
}

object AddModelUnit {

    /**
     * the model is not valid if it is empty
     * or contains "lada"
     */

    fun validateModelInput(
        model: String,
    ): Boolean {
        return model.isNotBlank() && !model.contains("lada")
    }
}
