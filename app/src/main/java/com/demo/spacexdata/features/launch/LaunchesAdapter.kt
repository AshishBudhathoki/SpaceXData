package com.demo.spacexdata.features.launch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.databinding.LaunchesRecyclerItemBinding

class LaunchesAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<LaunchesAdapter.ViewHolder>() {

    private var launchesList: List<Launch> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LaunchesRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (launchesList.isNotEmpty()) holder.bind(launchesList[position], itemClickListener)
    }

    override fun getItemCount(): Int = launchesList.size

    inner class ViewHolder(private val binding: LaunchesRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(launch: Launch, itemClickListener: OnItemClickListener) {
            binding.apply {
                textFlightNumber.text = launch.flight_number.toString()
                textLaunchDate.text = launch.launch_year.toString()
                textMissionName.text = launch.mission_name
                itemView.setOnClickListener {
                    if (adapterPosition != -1) itemClickListener.onItemClicked(
                        launch.flight_number.toString(),
                        launch.rocket.rocket_id,
                        itemView
                    )
                }
            }
        }
    }

    fun setData(data: List<Launch>) {
        launchesList = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(flightNumber: String, rocketId: String, itemView: View)
    }
}
