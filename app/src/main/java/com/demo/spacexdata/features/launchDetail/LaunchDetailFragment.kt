package com.demo.spacexdata.features.launchDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.demo.spacexdata.R
import com.demo.spacexdata.databinding.FragmentLaunchDetailBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import kotlin.properties.Delegates

class LaunchDetailFragment : Fragment(R.layout.fragment_launch_detail) {
    private val viewModel: LaunchDetailViewModel by activityViewModels()
    private var flightNumber by Delegates.notNull<Int>()
    private lateinit var rocketId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromLaunchFragment()

        val binding = FragmentLaunchDetailBinding.bind(view)
        binding.apply {
            setUpSwipeToRefresh()
            setUpUIFromData(binding)
            showSnackBar()
        }
    }

    private fun FragmentLaunchDetailBinding.setUpSwipeToRefresh() {
        swipeRefreshLayout.apply {
            setProgressBackgroundColorSchemeResource(R.color.colorSurface)
            setOnRefreshListener {
                viewModel.loadData(flightNumber, rocketId)
            }
        }
        // Observe if data is refreshing and show/hide loading indicator
        viewModel.getLaunchDetailLoadingStatus().observe(viewLifecycleOwner,
            Observer<Boolean> { isLaunchDetailRefreshing ->
                swipeRefreshLayout.isRefreshing = isLaunchDetailRefreshing
            })
    }

    private fun FragmentLaunchDetailBinding.showSnackBar() {
        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.snackbar.observe(viewLifecycleOwner, Observer { text ->
            text?.let {
                Snackbar.make(swipeRefreshLayout, text, Snackbar.LENGTH_LONG).setAction(
                    getString(R.string.snackbar_action_retry)
                ) {
                    viewModel.loadData(flightNumber, rocketId)
                }.show()
                viewModel.onSnackbarShown()
            }
        })
    }

    private fun setUpUIFromData(binding: FragmentLaunchDetailBinding) {
        viewModel.loadData(flightNumber, rocketId)
        val rocketDetail = viewModel.rocketDetail
        binding.textViewRocketId.text = rocketDetail.wikipedia
        binding.textViewRocketId.setOnClickListener {
            openWebUrl(rocketDetail.wikipedia)
        }
        val launchDetail = viewModel.launchDetail
        if (launchDetail != null) {
            Timber.d("Response: $launchDetail.details")
            binding.textViewFlightId.text = launchDetail.flight_number.toString()
        }
    }

    private fun getDataFromLaunchFragment() {
        val bundle: Bundle? = arguments
        flightNumber = bundle!!.get("FLIGHT_NUMBER").toString().toInt()
        rocketId = bundle!!.get("ROCKET_ID").toString()
        Timber.d("ROCKET ID: $rocketId")
    }

    private fun openWebUrl(urlAddress: String) {
        if (urlAddress.isNotEmpty()) startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(urlAddress)
            )
        )
    }
}