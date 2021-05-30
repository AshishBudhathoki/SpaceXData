package com.demo.spacexdata.features.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.demo.spacexdata.R
import com.demo.spacexdata.databinding.BottomSheetLaunchesSortingBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchesSortingBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: LaunchesViewModel by activityViewModels()
    private lateinit var selectedSorting: LaunchesViewModel.LaunchesSortingOrder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.bottom_sheet_launches_sorting, container, false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = BottomSheetLaunchesSortingBinding.bind(view)
        binding.apply {
            viewModel.getLaunchesOrder()
                .observe(viewLifecycleOwner, Observer { sortingOrder ->
                    selectedSorting = sortingOrder

                    when (selectedSorting) {
                        LaunchesViewModel.LaunchesSortingOrder.BY_LAUNCH_DATE_NEWEST ->
                            radioGroupSorting.check(radioButtonFlightNumberNewest.id)
                        LaunchesViewModel.LaunchesSortingOrder.BY_LAUNCH_DATE_OLDEST ->
                            radioGroupSorting.check(radioButtonFlightNumberOldest.id)
                        LaunchesViewModel.LaunchesSortingOrder.BY_MISSION_NAME ->
                            radioGroupSorting.check(radioButtonMissionName.id)
                        LaunchesViewModel.LaunchesSortingOrder.BY_SUCCESS ->
                            radioGroupSorting.check(radioButtonSuccess.id)
                    }
                })

            radioGroupSorting.check(radioButtonFlightNumberNewest.id)
            radioGroupSorting.setOnCheckedChangeListener { group, checkedId ->
                when (group.checkedRadioButtonId) {
                    radioButtonFlightNumberNewest.id -> {
                        viewModel.setLaunchesSortingOrder(
                            LaunchesViewModel.LaunchesSortingOrder.BY_LAUNCH_DATE_NEWEST
                        )
                    }
                    radioButtonFlightNumberOldest.id -> {
                        viewModel.setLaunchesSortingOrder(
                            LaunchesViewModel.LaunchesSortingOrder.BY_LAUNCH_DATE_OLDEST
                        )
                    }
                    radioButtonMissionName.id -> {
                        viewModel.setLaunchesSortingOrder(
                            LaunchesViewModel.LaunchesSortingOrder.BY_MISSION_NAME
                        )
                    }
                    radioButtonSuccess.id -> {
                        viewModel.setLaunchesSortingOrder(
                            LaunchesViewModel.LaunchesSortingOrder.BY_SUCCESS
                        )
                    }
                }
                if (isVisible) dismiss()
            }
        }
    }

    companion object {
        const val TAG = "bottom_sheet_launches_sorting"
    }
}
