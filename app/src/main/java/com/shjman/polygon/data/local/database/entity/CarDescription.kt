package com.shjman.polygon.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_description")
data class CarDescription(
    val model: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}