package com.demo.spacexdata.testUtils

import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.model.Rocket

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

