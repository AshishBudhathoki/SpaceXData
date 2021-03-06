package com.demo.spacexdata.data.model.typeConverter

import androidx.room.TypeConverter
import com.demo.spacexdata.data.model.Rocket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RocketConverter {

    private val gson = Gson()
    private val type = object : TypeToken<Rocket>() {}.type

    @TypeConverter
    fun rocketToString(rocket: Rocket): String = gson.toJson(rocket, type)

    @TypeConverter
    fun stringToRocket(rocketString: String): Rocket = gson.fromJson(rocketString, type)
}
