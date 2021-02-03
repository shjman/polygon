package com.shjman.polygon.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.shjman.polygon.data.local.database.BaseDao
import com.shjman.polygon.data.local.database.entity.CarDescription
import com.shjman.polygon.data.local.database.entity.Refueling
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CarDescriptionDao : BaseDao<Refueling> {

    @Query("SELECT * FROM car_description")
    abstract fun getAllInfo(): Flow<CarDescription>
}