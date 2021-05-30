package com.demo.spacexdata.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.demo.spacexdata.data.model.typeConverter.RocketConverter

@Entity(tableName = "space_x_launches")
data class Launch(
    @PrimaryKey
    val flight_number: Int,
    val mission_name: String?,
    val launch_success: Boolean?,
    val launch_year: Int?,
    @TypeConverters(RocketConverter::class)
    @ColumnInfo(name = "rocket")
    val rocket: Rocket
)
