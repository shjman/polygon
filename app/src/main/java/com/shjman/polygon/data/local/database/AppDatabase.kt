package com.shjman.polygon.data.local.database

import androidx.room.*
import com.shjman.polygon.data.InstantTimeConverter
import com.shjman.polygon.data.local.database.dao.RefuelingDao
import com.shjman.polygon.data.local.database.entity.CarDescription
import com.shjman.polygon.data.local.database.entity.Refueling

@Database(
    entities = [
        Refueling::class,
        CarDescription::class
    ],
    exportSchema = true,
    version = 1
)
@TypeConverters(
    InstantTimeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun refuelingDao(): RefuelingDao
}

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: T)
}