package com.demo.spacexdata.api

import com.demo.spacexdata.data.model.Launch
import retrofit2.Response
import retrofit2.http.GET

interface SpaceXApi {
    companion object {
        const val BASE_URL = "https://api.spacexdata.com/v3/"
    }

    @GET("launches/?limit=100")
    suspend fun getAllLaunches(): Response<List<Launch>>
}