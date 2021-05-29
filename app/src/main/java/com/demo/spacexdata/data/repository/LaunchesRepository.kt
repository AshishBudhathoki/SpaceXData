package com.demo.spacexdata.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.spacexdata.api.SpaceXApi
import com.demo.spacexdata.data.SpaceXDao
import com.demo.spacexdata.data.model.Launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class LaunchesRepository @Inject constructor(
    private val spaceXApi: SpaceXApi,
    private val spaceXLaunchDao: SpaceXDao
) {
    // Variables for showing/hiding loading indicators
    private val areLaunchesLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    // Set value to message to be shown in snackbar
    private val launchesSnackBar = MutableLiveData<String>()

    // Wrapper for getting all launches from Db
    fun getAllLaunchesFlow(): Flow<List<Launch>> = spaceXLaunchDao.getAllSpaceXLaunches()

    suspend fun deleteAllLaunches() =
        withContext(Dispatchers.IO) { spaceXLaunchDao.deleteAllSpaceXLaunches() }

    fun getLaunchesLoadingStatus(): LiveData<Boolean> = areLaunchesLoading

    fun getLaunchesSnackbar(): MutableLiveData<String> = launchesSnackBar

    suspend fun performDataRefresh() {
        // Start loading process
        areLaunchesLoading.postValue(true)
        withContext(Dispatchers.IO) {
            try {
                fetchLaunchesAndSaveToDb()
            } catch (exception: Exception) {
                when (exception) {
                    is IOException -> launchesSnackBar.postValue("Network problem occurred")
                    else -> {
                        launchesSnackBar.postValue("Unexpected problem occurred")
                        Timber.d("Exception: $exception")
                    }
                }
            }
            areLaunchesLoading.postValue(false)
        }
    }


    private suspend fun fetchLaunchesAndSaveToDb() {
        val response = spaceXApi.getAllLaunches()
        if (response.isSuccessful) {
            response.body()?.let {
                spaceXLaunchDao.replaceAllLaunches(it)
            }
        } else Timber.d("Error: ${response.errorBody()}")
    }
}
