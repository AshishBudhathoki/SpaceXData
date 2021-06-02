package com.demo.spacexdata.features.launchDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.spacexdata.data.model.LaunchDetail
import com.demo.spacexdata.data.model.RocketDetail
import com.demo.spacexdata.data.repository.LaunchDetailRepository
import com.demo.spacexdata.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LaunchDetailViewModel @Inject constructor(
    private val repository: LaunchDetailRepository
) : ViewModel() {

    private val launchDetail: MutableLiveData<LaunchDetail> = MutableLiveData()
    private val rocketDetail: MutableLiveData<RocketDetail> = MutableLiveData()
    private val _isLaunchDetailLoading: LiveData<Boolean> =
        repository.getLaunchDetailLoadingStatus()
    private val launchDetailSnackBar = MutableLiveData<String>()
    private val _snackBar: MutableLiveData<String> = launchDetailSnackBar


    fun getLaunchDetailLoadingStatus() = _isLaunchDetailLoading

    fun loadData(flightNo: Int, rocketId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadLaunchDetail(flightNo)
            loadRocketDetail(rocketId)
        }
    }

    fun getLaunchDetail(): LiveData<LaunchDetail> = launchDetail

    fun getRocketDetail(): LiveData<RocketDetail> = rocketDetail

    suspend fun loadRocketDetail(rocketId: String) {
        when (val rocketDetailFromApi = repository.getRocketDetailFromApi(rocketId)) {
            is ResultWrapper.Success ->
                rocketDetailFromApi.value.collect {
                    rocketDetail.postValue(it.body())
                }
            is ResultWrapper.GenericError -> {
                var exception: Exception? = rocketDetailFromApi.error
                launchDetailSnackBar.postValue(exception?.message)
            }
        }
    }

    suspend fun loadLaunchDetail(flightNo: Int) {
        when (val launchDetailFromApi = repository.getLaunchDetailFromApi(flightNo)) {
            is ResultWrapper.Success ->
                launchDetailFromApi.value.collect {
                    launchDetail.postValue(it.body())

                }
            is ResultWrapper.GenericError -> {
                var exception: Exception? = launchDetailFromApi.error
                launchDetailSnackBar.postValue(exception?.message)
            }
        }
    }

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<String>
        get() = _snackBar

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

}