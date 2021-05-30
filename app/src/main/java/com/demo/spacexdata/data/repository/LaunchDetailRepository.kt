package com.demo.spacexdata.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.spacexdata.api.SpaceXApi
import com.demo.spacexdata.data.SpaceXDao
import com.demo.spacexdata.data.model.LaunchDetail
import com.demo.spacexdata.data.model.RocketDetail
import com.demo.spacexdata.utils.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchDetailRepository @Inject constructor(
    private val spaceXApi: SpaceXApi,
    private val spaceXLaunchDao: SpaceXDao
) {
    // Variables for showing/hiding loading indicators
    private val areLaunchDetailLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    // Set value to message to be shown in snackbar
    private val launchDetailSnackBar = MutableLiveData<String>()

    fun getLaunchDetailLoadingStatus(): LiveData<Boolean> = areLaunchDetailLoading

    fun getLaunchDetailSnackbar(): MutableLiveData<String> = launchDetailSnackBar

    suspend fun getLaunchDetailFromApi(flightNumber: Int): ResultWrapper<LaunchDetail> {
        return CustomNetworkCall.safeApiCall {
            spaceXApi.getLaunchDetails(flightNumber).body()!!
        }
    }

    suspend fun getRocketDetailFromApi(rocketID: String): ResultWrapper<RocketDetail> {
        return CustomNetworkCall.safeApiCall {
            spaceXApi.getRocketDetails(rocketID).body()!!
        }
    }

    object CustomNetworkCall {
        suspend fun <T> safeApiCall(
            apiCall: suspend () -> T
        ): ResultWrapper<T> {
            return withContext(Dispatchers.IO) {
                try {
                    var result = apiCall.invoke()
                    ResultWrapper.Success(result)
                } catch (throwable: Exception) {
                    ResultWrapper.GenericError(throwable)
                }
            }
        }
    }
}

