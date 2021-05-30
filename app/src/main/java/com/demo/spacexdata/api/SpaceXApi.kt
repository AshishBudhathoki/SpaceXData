package com.demo.spacexdata.api

import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.model.LaunchDetail
import com.demo.spacexdata.data.model.RocketDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SpaceXApi {
    companion object {
        const val BASE_URL = "https://api.spacexdata.com/v3/"
    }

    @GET("launches/?limit=100")
    suspend fun getAllLaunches(): Response<List<Launch>>

    @GET("launches/{flight_number}")
    suspend fun getLaunchDetails(
        @Path("flight_number") query: Int
    ): Response<LaunchDetail>

    @GET("rockets/{rocket_id}")
    suspend fun getRocketDetails(
        @Path("rocket_id") query: String
    ): Response<RocketDetail>
}