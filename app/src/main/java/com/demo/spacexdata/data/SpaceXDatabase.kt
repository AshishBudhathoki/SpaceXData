package com.demo.spacexdata.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.spacexdata.data.model.Launch

@Database(entities = [Launch::class], version = 1)
abstract class SpaceXDatabase : RoomDatabase() {

    abstract fun spaceXLaunchDao(): SpaceXDao
}