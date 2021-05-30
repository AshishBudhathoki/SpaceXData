package com.demo.spacexdata.features.launchDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.spacexdata.data.model.LaunchDetail
import com.demo.spacexdata.data.model.RocketDetail
import com.demo.spacexdata.data.repository.LaunchDetailRepository
import com.demo.spacexdata.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LaunchDetailViewModel @Inject constructor(
    private val repository: LaunchDetailRepository
) : ViewModel() {

    lateinit var launchDetail: LaunchDetail
    lateinit var rocketDetail: RocketDetail
    private val _isLaunchDetailLoading: LiveData<Boolean> =
        repository.getLaunchDetailLoadingStatus()
    private val _snackBar: MutableLiveData<String> = repository.getLaunchDetailSnackbar()


    fun getLaunchDetailLoadingStatus() = _isLaunchDetailLoading

    fun loadData(flightNo: Int, rocketId: String) {
        runBlocking {
            loadLaunchDetail(flightNo)
            loadRocketDetail(rocketId)
        }
    }

    suspend fun loadRocketDetail(rocketId: String) {
        when (val rocketDetailFromApi = repository.getRocketDetailFromApi(rocketId)) {
            is ResultWrapper.Success ->
                rocketDetail =
                    rocketDetailFromApi.value // get result as CustomerResponse model class

            is ResultWrapper.GenericError -> {
                var exception: Exception? = rocketDetailFromApi.error
            }
        }
    }

    suspend fun loadLaunchDetail(flightNo: Int) {
        when (val launchDetailFromApi = repository.getLaunchDetailFromApi(flightNo)) {
            is ResultWrapper.Success ->
                launchDetail =
                    launchDetailFromApi.value // get result as CustomerResponse model class

            is ResultWrapper.GenericError -> {
                var exception: Exception? = launchDetailFromApi.error
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