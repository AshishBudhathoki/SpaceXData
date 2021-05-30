package com.demo.spacexdata.features.launch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.repository.LaunchesRepository
import com.demo.spacexdata.features.LaunchesViewModel
import com.demo.spacexdata.testUtils.getValue
import com.demo.spacexdata.testUtils.testLaunch1
import com.demo.spacexdata.testUtils.testLaunch2
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
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LaunchesViewModelTest {

    private val testLaunchesList = listOf(testLaunch2, testLaunch1)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var launchesRepository: LaunchesRepository

    // class under test
    private lateinit var launchesViewModel: LaunchesViewModel

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
    fun getLaunchesTest() {
        val launchesFlow = flowOf(testLaunchesList)
        Mockito.`when`(launchesRepository.getAllLaunchesFlow()).thenAnswer {
            return@thenAnswer launchesFlow
        }

        launchesViewModel = LaunchesViewModel(launchesRepository)

        val result: List<Launch> = getValue(launchesViewModel.getAllLaunches())

        assertEquals(testLaunchesList.sortedBy { it.launch_year }, result)
    }

    @Test
    fun refreshLaunchesTest_verifyCalls() = runBlockingTest {
        val launchesFlow = flowOf(testLaunchesList)
        Mockito.`when`(launchesRepository.getAllLaunchesFlow()).thenAnswer {
            return@thenAnswer launchesFlow
        }

        launchesViewModel = LaunchesViewModel(launchesRepository)

        launchesViewModel.refreshAllLaunches()

        verify(launchesRepository, times(1)).performDataRefresh()
    }


    @Test
    fun getLaunchesSortingOrderTest() = runBlockingTest {
        val launchesFlow = flowOf(testLaunchesList)
        Mockito.`when`(launchesRepository.getAllLaunchesFlow()).thenAnswer {
            return@thenAnswer launchesFlow
        }

        launchesViewModel = LaunchesViewModel(launchesRepository)

        LaunchesViewModel.LaunchesSortingOrder.values().forEach { sortingOrder ->
            launchesViewModel.setLaunchesSortingOrder(sortingOrder)

            val result = getValue(launchesViewModel.getLaunchesOrder())

            assertEquals(sortingOrder, result)
        }
    }
}
