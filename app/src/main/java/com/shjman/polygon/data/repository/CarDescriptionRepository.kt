package com.shjman.polygon.data.repository

import android.content.Context
import com.shjman.polygon.data.local.database.AppDatabase
import com.shjman.polygon.data.local.database.entity.CarDescription
import dagger.hilt.android.qualifiers.ApplicationContext

class CarDescriptionRepository(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase
) {
    fun addCarDescription(carModel: String) {
        database.carDescriptionDao().insert(CarDescription(carModel))
    }
}