package com.demo.spacexdata.features.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.repository.LaunchesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    private val allLaunches: MutableLiveData<List<Launch>> = MutableLiveData()
    private val _areLaunchesLoading: LiveData<Boolean> = repository.getLaunchesLoadingStatus()
    private val _snackBar: MutableLiveData<String> = repository.getLaunchesSnackbar()

    private val _sortingOrder =
        MutableLiveData<LaunchesSortingOrder>(LaunchesSortingOrder.BY_LAUNCH_DATE_OLDEST)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllLaunchesFlow()
                .collect { launches -> sortAndSetLaunches(launches) }
        }
    }

    fun getAllLaunches(): LiveData<List<Launch>> = allLaunches

    fun getLaunchesOrder(): LiveData<LaunchesSortingOrder> = _sortingOrder

    fun setLaunchesSortingOrder(sortingOrder: LaunchesSortingOrder) {
        _sortingOrder.value = sortingOrder
        refreshAllLaunches()
        viewModelScope.launch(Dispatchers.IO) {
            if (allLaunches.value != null) sortAndSetLaunches(allLaunches.value!!)
        }
    }

    fun getLaunchesLoadingStatus(): LiveData<Boolean> = _areLaunchesLoading

    // Wrapper for refreshing launches data
    fun refreshAllLaunches() = viewModelScope.launch { repository.performDataRefresh() }

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

    private fun sortAndSetLaunches(launches: List<Launch>) {
        allLaunches.postValue(when (_sortingOrder.value) {
            LaunchesSortingOrder.BY_LAUNCH_DATE_NEWEST -> {
                launches.sortedByDescending { it.launch_year }
            }
            LaunchesSortingOrder.BY_LAUNCH_DATE_OLDEST -> {
                launches.sortedBy { it.launch_year }
            }
            LaunchesSortingOrder.BY_MISSION_NAME -> {
                launches.sortedBy { it.mission_name }
            }
            LaunchesSortingOrder.BY_SUCCESS -> {
                launches.filter { it.launch_success == true }
            }
            else -> launches.sortedBy { it.flight_number }
        })
    }

    enum class LaunchesSortingOrder { BY_LAUNCH_DATE_NEWEST, BY_LAUNCH_DATE_OLDEST, BY_MISSION_NAME, BY_SUCCESS }
}
