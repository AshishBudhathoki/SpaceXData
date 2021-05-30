package com.demo.spacexdata.features.launchDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.repository.LaunchDetailRepository
import com.demo.spacexdata.data.repository.LaunchesRepository
import com.demo.spacexdata.features.launch.LaunchesViewModel
import com.demo.spacexdata.testUtils.*
import com.demo.spacexdata.utils.ResultWrapper
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LaunchDetailViewModelTest {

    private val testLaunchDetail = testLaunchDetail1
    private val rocketDetail = testRocketDetail1
    private val flightNumber = 15
    private val rocketId = "falcon9"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var launchDetailRepository: LaunchDetailRepository

    // class under test
    private lateinit var launchDetailViewModel: LaunchDetailViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getLaunchesDetailTest() = runBlockingTest {
        val launchDetailResponse = ResultWrapper.Success(testLaunchDetail1)
        Mockito.`when`(launchDetailRepository.getLaunchDetailFromApi(flightNumber)).thenAnswer {
            return@thenAnswer launchDetailResponse
        }

        launchDetailViewModel = LaunchDetailViewModel(launchDetailRepository)

        launchDetailViewModel.loadLaunchDetail(flightNumber)

        verify(launchDetailRepository, times(1))
    }

    @Test
    fun getRocketDetailTest() = runBlockingTest {
        val rocketDetailResponse = ResultWrapper.Success(rocketDetail)
        Mockito.`when`(launchDetailRepository.getRocketDetailFromApi(rocketId)).thenAnswer {
            return@thenAnswer rocketDetailResponse
        }

        launchDetailViewModel = LaunchDetailViewModel(launchDetailRepository)

        launchDetailViewModel.loadRocketDetail(rocketId)

        verify(launchDetailRepository, times(1))
    }

}
