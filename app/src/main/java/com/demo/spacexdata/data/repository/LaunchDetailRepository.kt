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

    fun getLaunchDetailLoadingStatus(): LiveData<Boolean> = areLaunchDetailLoading

    suspend fun getLaunchDetailFromApi(flightNumber: Int): ResultWrapper<LaunchDetail> {
        return CustomNetworkCall.safeApiCall {
            spaceXApi.getLaunchDetails(flightNumber).body()!!
        }
    }

    suspend fun getRocketDetailFromApi(rocketID: String): ResultWrapper<RocketDetail> {
        areLaunchDetailLoading.postValue(true)
        val responseFromApi = CustomNetworkCall.safeApiCall {
            spaceXApi.getRocketDetails(rocketID).body()!!
        }
        areLaunchDetailLoading.postValue(false)
        return responseFromApi
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

