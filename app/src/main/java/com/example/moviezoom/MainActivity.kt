package com.example.moviezoom

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moviezoom.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.selectedMovie.collectLatest { movie ->
                    movie?.let { supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, DetailsFragment())
                        .addToBackStack(null)
                        .commit()
                        mainViewModel.clearSelectedMovie()
                    }
                }
            }
        }
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