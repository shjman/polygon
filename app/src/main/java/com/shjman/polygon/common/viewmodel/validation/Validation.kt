package com.shjman.polygon.common.viewmodel.validation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Validation<T>(
    scope: CoroutineScope,
    private vararg val validators: Validators<T>
) : ReadOnlyProperty<ViewModel, Validation<T>> {
    private val flow = MutableStateFlow<T?>(null)
    val value: T get() = flow.value ?: throw IllegalStateException("Validated value is not initialized!")

    val result = flow.filterNotNull()
        .map { validate(it) }
        .shareIn(scope, SharingStarted.Lazily, 1)

    override fun getValue(thisRef: ViewModel, property: KProperty<*>): Validation<T> = this

    fun valueChanged(value: T) {
        flow.value = value
    }

    private fun validate(value: T): ValidationResult {
        validators.forEach {
            if (!it.prediction(value)) return ValidationResult.NotValid(it.errorMessageRes)
        }
        return ValidationResult.Valid
    }
}

sealed class ValidationResult(@StringRes val errorMessageRes: Int? = null) {
    object Valid : ValidationResult()
    class NotValid(@StringRes val messageRes: Int) : ValidationResult(messageRes)
}

fun <T> ViewModel.validation(vararg validators: Validators<T>): Validation<T> = Validation(viewModelScope, *validators)

fun TextInputLayout.showValidationError(result: ValidationResult) {
    val errorMessage = result.errorMessageRes?.let { context.resources.getString(it) }
    error = errorMessage
}
