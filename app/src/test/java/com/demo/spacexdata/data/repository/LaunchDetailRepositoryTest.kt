package com.demo.spacexdata.data.repository

import com.demo.spacexdata.api.SpaceXApi
import com.demo.spacexdata.data.SpaceXDao
import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.model.LaunchDetail
import com.demo.spacexdata.data.model.RocketDetail
import com.demo.spacexdata.testUtils.testLaunch1
import com.demo.spacexdata.testUtils.testLaunch2
import com.demo.spacexdata.testUtils.testLaunchDetail1
import com.demo.spacexdata.testUtils.testRocketDetail1
import com.demo.spacexdata.utils.ResultWrapper
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response


@RunWith(JUnit4::class)
class LaunchDetailRepositoryTest {

    private val testLaunchDetail = testLaunchDetail1
    private val testRocketDetail = testRocketDetail1
    private val flightNumber = testLaunchDetail1.flight_number
    private val rocketId = testRocketDetail1.rocket_id

    @Mock
    private lateinit var spaceService: SpaceXApi

    // Class under test
    private lateinit var launchDetailRepository: LaunchDetailRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        launchDetailRepository = LaunchDetailRepository(spaceService)
    }


    @Test
    fun call_getLaunchDetails_ApiResponseSuccess() = runBlocking {
        val responseSuccess: Response<LaunchDetail> =
            (Response.success((testLaunchDetail)))

        Mockito.`when`(spaceService.getLaunchDetails(flightNumber)).thenAnswer {
            return@thenAnswer responseSuccess
        }

        launchDetailRepository.getLaunchDetailFromApi(flightNumber)

        verify(spaceService, times(1)).getLaunchDetails(flightNumber)
    }

    @Test
    fun call_getLaunchDetails_ApiResponseError() = runBlocking {
        val responseError: Response<LaunchDetail> = Response.error(
            403,
            "Bad Request"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )

        Mockito.`when`(spaceService.getLaunchDetails(flightNumber)).thenAnswer {
            return@thenAnswer responseError
        }

        launchDetailRepository.getLaunchDetailFromApi(flightNumber)

        verify(spaceService, times(1)).getLaunchDetails(flightNumber)
    }

    @Test
    fun call_getRocketDetails_ApiResponseSuccess() = runBlocking {
        val responseSuccess: Response<RocketDetail> =
            (Response.success((testRocketDetail)))

        Mockito.`when`(spaceService.getRocketDetails(rocketId)).thenAnswer {
            return@thenAnswer responseSuccess
        }

        launchDetailRepository.getRocketDetailFromApi(rocketId)

        verify(spaceService, times(1)).getRocketDetails(rocketId)
    }

    @Test
    fun call_getRocketDetails_ApiResponseError() = runBlocking {
        val responseError: Response<RocketDetail> = Response.error(
            403,
            "Bad Request"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )

        Mockito.`when`(spaceService.getRocketDetails(rocketId)).thenAnswer {
            return@thenAnswer responseError
        }

        launchDetailRepository.getRocketDetailFromApi(rocketId)

        verify(spaceService, times(1)).getRocketDetails(rocketId)
    }
}