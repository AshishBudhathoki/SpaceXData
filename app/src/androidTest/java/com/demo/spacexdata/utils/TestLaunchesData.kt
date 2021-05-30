package com.demo.spacexdata.utils

import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.model.LaunchDetail
import com.demo.spacexdata.data.model.Rocket
import com.demo.spacexdata.data.model.RocketDetail

val testLaunch1 = Launch(
    75, "Nusantara Satu (PSN-6) / GTO-1 / Beresheet", null,
    1550799900, Rocket(
        "falcon9"
    )
)

val testLaunch2 = Launch(
    76, "CCtCap Demo Mission 1",
    true, 2008, Rocket(
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

