package com.demo.spacexdata.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.spacexdata.utils.testLaunch1
import com.demo.spacexdata.utils.testLaunch2
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LaunchesDaoTest {

    private lateinit var database: SpaceXDatabase
    private lateinit var launchesDao: SpaceXDao

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testLaunchesData = listOf(testLaunch1, testLaunch2)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room
            .inMemoryDatabaseBuilder(context, SpaceXDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        launchesDao = database.spaceXLaunchDao()
    }

    @After
    fun cleanup() {
        database.close()

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testInsertAndGetAllLaunches() = runBlocking {
        launchesDao.insertSpaceXLaunches(testLaunchesData)

        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {
            launchesDao.getAllSpaceXLaunches().collect { launches ->
                MatcherAssert.assertThat(launches.size, Matchers.equalTo(testLaunchesData.size))
                latch.countDown()
            }
        }

        latch.await()
        job.cancel()
    }

    @Test
    fun testReplaceLaunchesData() = runBlocking {
        // Perform action double to check if data is properly erased and there is no duplicates
        launchesDao.replaceAllLaunches(testLaunchesData)
        launchesDao.replaceAllLaunches(testLaunchesData)

        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {
            launchesDao.getAllSpaceXLaunches().collect { launches ->
                MatcherAssert.assertThat(launches.size, Matchers.equalTo(testLaunchesData.size))
                latch.countDown()
            }
        }

        latch.await()
        job.cancel()
    }

    @Test
    fun testDeleteUpcomingLaunches() = runBlocking {
        launchesDao.insertSpaceXLaunches(testLaunchesData)
        launchesDao.deleteAllSpaceXLaunches()

        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {
            launchesDao.getAllSpaceXLaunches().collect { launches ->
                MatcherAssert.assertThat(launches.size, Matchers.equalTo(0))
                latch.countDown()
            }
        }

        latch.await()
        job.cancel()
    }
}