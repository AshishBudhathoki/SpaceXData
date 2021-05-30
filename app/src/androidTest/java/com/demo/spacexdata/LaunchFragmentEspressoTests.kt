package com.demo.spacexdata

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.demo.spacexdata.features.launch.LaunchesAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LaunchFragmentEspressoTests {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun openLaunchesDetailFragment() {

        // Check if fragment appeared
        onView(withId(R.id.root_recycler_sorting))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<LaunchesAdapter.ViewHolder>(1, click())
            )
        onView(withId(R.id.text_flight_number))
            .check(matches(isDisplayed()))
        onView(withId(R.id.text_mission_name))
            .check(matches(isDisplayed()))
        onView(withId(R.id.text_launch_date))
            .check(matches(isDisplayed()))
    }

}
