package com.example.moviezoom

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviezoom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setSupportActionBar(binding.toolbar)

        supportFragmentManager.addOnBackStackChangedListener {
            updateActionBar()
        }
        
        updateActionBar()
    }

    private fun updateActionBar() {
        val showBack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.setDisplayHomeAsUpEnabled(showBack)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        supportActionBar?.title = when (currentFragment) {
            is ListFragment -> "Movies"
            is DetailsFragment -> "Details"
            else -> "MovieZoom"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}