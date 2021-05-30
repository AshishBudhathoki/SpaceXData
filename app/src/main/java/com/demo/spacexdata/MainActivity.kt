package com.demo.spacexdata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.spacexdata.databinding.ActivityMainBinding
import com.demo.spacexdata.features.launch.LaunchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var launchFragment: LaunchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launchFragment = LaunchFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, launchFragment, TAG_LAUNCH_FRAGMENT)
            .commit()
    }
}

private const val TAG_LAUNCH_FRAGMENT = "TAG_LAUNCH_FRAGMENT"