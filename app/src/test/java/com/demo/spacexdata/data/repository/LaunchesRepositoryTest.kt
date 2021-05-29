package com.demo.spacexdata.data.repository

import com.demo.spacexdata.api.SpaceXApi
import com.demo.spacexdata.data.SpaceXDao
import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.testUtils.testLaunch1
import com.demo.spacexdata.data.testUtils.testLaunch2
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class LaunchesRepositoryTest {

    private val testLaunchesList = listOf(
        testLaunch1,
        testLaunch2
    )

    @Mock
    private lateinit var spaceService: SpaceXApi
    @Mock
    private lateinit var launchesDao: SpaceXDao

    // Class under test
    private lateinit var launchesRepository: LaunchesRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        launchesRepository = LaunchesRepository(spaceService, launchesDao)
    }

    @Test
    fun getAllLaunchesFlowTest() = runBlocking {
        val launchesFlow = flowOf(testLaunchesList)

        Mockito.`when`(launchesDao.getAllSpaceXLaunches()).thenAnswer {
            return@thenAnswer launchesFlow
        }
        var result = listOf<Launch>()
        launchesRepository.getAllLaunchesFlow().collect {
            result = it
        }

        verify(launchesDao, times(1)).getAllSpaceXLaunches()
        assertEquals(result, testLaunchesList)
    }

    @Test
    fun performDataRefresh_ApiResponseSuccess_VerifyDataInsertedOnce() = runBlocking {
        val responseSuccess: Response<List<Launch>> = Response.success(testLaunchesList)

        Mockito.`when`(spaceService.getAllLaunches()).thenAnswer {
            return@thenAnswer responseSuccess
        }

        launchesRepository.performDataRefresh()

        verify(spaceService, times(1)).getAllLaunches()
        verify(launchesDao, times(1)).replaceAllLaunches(testLaunchesList)
    }

    @Test
    fun performDataRefresh_ApiResponseError_VerifyDataNotInserted() = runBlocking {
        val responseError: Response<List<Launch>> = Response.error(
            403,
            "Bad Request"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )

        Mockito.`when`(spaceService.getAllLaunches()).thenAnswer {
            return@thenAnswer responseError
        }

        launchesRepository.performDataRefresh()

        verify(spaceService, times(1)).getAllLaunches()
        verify(launchesDao, times(0)).replaceAllLaunches(testLaunchesList)
    }

    @Test
    fun deleteAllLaunchesTest_verifyCalls() = runBlocking {

        launchesRepository.deleteAllLaunches()

        verify(launchesDao, times(1)).deleteAllSpaceXLaunches()
    }
}