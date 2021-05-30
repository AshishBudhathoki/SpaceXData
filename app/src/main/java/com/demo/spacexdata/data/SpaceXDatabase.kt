package com.demo.spacexdata.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.model.typeConverter.RocketConverter

@Database(entities = [Launch::class], version = 1)

@TypeConverters(
    value = [RocketConverter::class]
)
abstract class SpaceXDatabase : RoomDatabase() {

    abstract fun spaceXLaunchDao(): SpaceXDao
}