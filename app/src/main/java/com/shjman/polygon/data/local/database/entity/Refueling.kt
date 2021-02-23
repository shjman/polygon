package com.shjman.polygon.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "refueling")
data class Refueling(
    val date: Instant,
    val mileage: Int?,
    val fuelType: String?,
    val numberOfLiters: Double?,
    val price: Double?,
    val photoTakenUri: String?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}