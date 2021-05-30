package com.demo.spacexdata.data.model

data class LaunchDetail(
    val flight_number: Int,
    val mission_name: String?,
    val launch_year: String?,
    val is_tentative: Boolean?,
    val launch_success: Boolean?,
    val details: String?
)
