package com.shjman.polygon.data.repository

import android.content.Context
import com.shjman.polygon.data.local.database.AppDatabase
import com.shjman.polygon.data.local.database.entity.Refueling
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant

class RefuelingRepository(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase,
) {
    fun addRefueling(date: Instant, mileage: Int, fuelType: String, numberOfLiters: Double, price: Double, photoTakenUri: String?) {
        database.refuelingDao().insert(Refueling(date, mileage, fuelType, numberOfLiters, price, photoTakenUri))
    }
}