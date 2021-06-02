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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class LaunchDetailRepository @Inject constructor(
    private val spaceXApi: SpaceXApi
) {
    // Variables for showing/hiding loading indicators
    private val areLaunchDetailLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getLaunchDetailLoadingStatus(): LiveData<Boolean> = areLaunchDetailLoading

    suspend fun getLaunchDetailFromApi(flightNumber: Int): ResultWrapper<Flow<Response<LaunchDetail>>> {
        return CustomNetworkCall.safeApiCall {
            flow {
                emit(spaceXApi.getLaunchDetails(flightNumber))
            }
        }
    }

    suspend fun getRocketDetailFromApi(rocketID: String): ResultWrapper<Flow<Response<RocketDetail>>> {
        areLaunchDetailLoading.postValue(true)
        val responseFromApi = CustomNetworkCall.safeApiCall {
            flow {
                emit(spaceXApi.getRocketDetails(rocketID))
            }
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
                    Timber.d("ERROR MESSAGE: ${throwable.message}")
                    ResultWrapper.GenericError(throwable)
                }
            }
        }
    }
}

