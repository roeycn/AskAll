package com.roeyc.askall.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.roeyc.askall.NavigationIconClickListener
import com.roeyc.askall.R
import com.roeyc.askall.databinding.ActivityMainBinding
import com.roeyc.askall.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    // using android  https://developer.android.com/topic/libraries/view-binding
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
       // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setMenu()
    }

    private fun setMenu() {
        setSupportActionBar(binding.appBar)
        // handling the open/close manu button
        binding.appBar.setNavigationOnClickListener(
            NavigationIconClickListener(this, binding.grid, AccelerateDecelerateInterpolator(),
                ContextCompat.getDrawable(this, R.drawable.ic_menu_black), // Menu open icon
                ContextCompat.getDrawable(this, R.drawable.ic_close_black))
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       super.onCreateOptionsMenu(menu)
       // menuInflater.inflate(R.layout.menu_backdrop, menu)
      // menuInflater.inflate(R.menu.menu_backdrop, menu)
        return true
    }
}