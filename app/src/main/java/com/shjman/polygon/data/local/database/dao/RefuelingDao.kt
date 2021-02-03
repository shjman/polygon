package com.shjman.polygon.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.shjman.polygon.data.local.database.BaseDao
import com.shjman.polygon.data.local.database.entity.Refueling
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RefuelingDao : BaseDao<Refueling> {

    @Query("SELECT * FROM refueling")
    abstract fun getAllRefueling(): Flow<List<Refueling>>
}