package com.shjman.polygon.common.viewmodel.validation

import androidx.annotation.StringRes

sealed class Validators<T>(@StringRes val errorMessageRes: Int, val prediction: (T) -> Boolean) {
    class NotEmptyValidator(@StringRes val messageRes: Int) : Validators<String>(messageRes, { it.isNotEmpty() })
    class IsAtLeastOrEqualValidator(@StringRes val messageRes: Int, countOfCharacters: Int) : Validators<String>(messageRes, { it.length >= countOfCharacters })
    class IsAtMaxOrEqualValidator(@StringRes val messageRes: Int, countOfCharacters: Int) : Validators<String>(messageRes, { it.length <= countOfCharacters })
}
