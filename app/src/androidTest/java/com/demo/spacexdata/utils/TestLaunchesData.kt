package com.demo.spacexdata.utils

import com.demo.spacexdata.data.model.Launch
import com.demo.spacexdata.data.model.Rocket

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

