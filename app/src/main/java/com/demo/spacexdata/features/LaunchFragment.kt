package com.demo.spacexdata.features

import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.demo.spacexdata.R
import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.databinding.FragmentLaunchBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchFragment : Fragment(R.layout.fragment_launch),
    LaunchesAdapter.OnItemClickListener {

    private lateinit var viewAdapter: LaunchesAdapter
    private val viewModel: LaunchesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLaunchBinding.bind(view)
        // Setup recyclerView
        viewAdapter = LaunchesAdapter(this)
        binding.apply {
            recyclerView.apply {
                setHasFixedSize(true)
                adapter = viewAdapter
            }

            swipeRefreshLayout.apply {
                setProgressBackgroundColorSchemeResource(R.color.colorSurface)
            }

            // ViewModel setup
            viewModel.getAllLaunches()
                .observe(viewLifecycleOwner, Observer<List<Launch>> { launches ->
                    when {
                        launches.isEmpty() -> {
                            recyclerView.visibility = View.GONE
                            emptyState.visibility = View.VISIBLE
                        }
                        launches != null -> {
                            recyclerView.visibility = View.VISIBLE
                            emptyState.visibility = View.GONE
                            viewAdapter.setData(launches)
                        }
                    }
                })

            // Observe if data is refreshing and show/hide loading indicator
            viewModel.getLaunchesLoadingStatus()
                .observe(viewLifecycleOwner, Observer<Boolean> { areLaunchesRefreshing ->
                    swipeRefreshLayout.isRefreshing = areLaunchesRefreshing
                })

            // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
            viewModel.snackbar.observe(viewLifecycleOwner, Observer { text ->
                text?.let {
                    Snackbar.make(swipeRefreshLayout, text, Snackbar.LENGTH_LONG).setAction(
                        getString(R.string.snackbar_action_retry)
                    ) {
                        viewModel.refreshAllLaunches()
                    }.show()
                    viewModel.onSnackbarShown()
                }
            })

            viewModel.getLaunchesOrder()
                .observe(viewLifecycleOwner, Observer { sortingOrder ->
                    buttonSorting.text = when (sortingOrder) {
                        LaunchesViewModel.LaunchesSortingOrder.BY_LAUNCH_DATE_NEWEST ->
                            getString(R.string.launch_year_newest)
                        LaunchesViewModel.LaunchesSortingOrder.BY_LAUNCH_DATE_OLDEST ->
                            getString(R.string.launch_year_oldest)
                        LaunchesViewModel.LaunchesSortingOrder.BY_MISSION_NAME ->
                            getString(R.string.mission_name)
                        LaunchesViewModel.LaunchesSortingOrder.BY_SUCCESS ->
                            getString(R.string.filter_by_success)
                        else -> getString(R.string.launch_year_newest)
                    }
                })

            // Swipe to refresh
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshAllLaunches()
            }

            buttonSorting.setOnClickListener {
                val sortingBottomSheetDialog = LaunchesSortingBottomSheetFragment()

                sortingBottomSheetDialog.show(
                    childFragmentManager,
                    LaunchesSortingBottomSheetFragment.TAG
                )
            }

            binding.fabScrollToTop.setOnClickListener {
                recyclerView.layoutManager?.smoothScrollToPosition(recyclerView, null, 0)
            }

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy < 0 && fabScrollToTop.visibility == View.VISIBLE) {
                        fabScrollToTop.hide()
                    } else if (dy > 0 && fabScrollToTop.visibility != View.VISIBLE) {
                        fabScrollToTop.show()
                    }
                }
            })
        }
    }


    // Respond to user clicks on recyclerView items
    override fun onItemClicked(flightNumber: Int, itemView: View) {
        Snackbar.make(itemView, flightNumber.toString(), Snackbar.LENGTH_SHORT).show()
        val bundle = Bundle()
        bundle.putString("FLIGHT_NUMBER", flightNumber.toString())

        val launchDetailFragment = LaunchDetailFragment()
        launchDetailFragment.arguments = bundle
        requireActivity()!!.supportFragmentManager.beginTransaction()
            .replace(
                (requireView().parent as ViewGroup).id,
                launchDetailFragment,
                "LAUNCH_DETAIL_FRAGMENT"
            )
            .addToBackStack(null)
            .commit()

    }

}
