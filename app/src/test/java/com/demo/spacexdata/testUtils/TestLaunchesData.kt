package com.demo.spacexdata.testUtils

import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.model.LaunchDetail
import com.demo.spacexdata.data.model.Rocket
import com.demo.spacexdata.data.model.RocketDetail

val testLaunch1 = Launch(
    15, "Sample Mission Name", null,
    2008, Rocket(
        "falcon9"
    )
)

val testLaunch2 = Launch(
    29, "Mission Impossible",
    true, 2010, Rocket(
        "falcon1"
    )
)

val testLaunchDetail1 = LaunchDetail(
    15, "Sample Mission Name", null,
    true, true, "Was a good launch Project"
)

val testRocketDetail1 = RocketDetail(
    15, "Sample Mission Name", "USA", "SPaceX", "http//wikipedia",
    "was a fast rocket", "falcon9", "falcon", "Fast"
)

