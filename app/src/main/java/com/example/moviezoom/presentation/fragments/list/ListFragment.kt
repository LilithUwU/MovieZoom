package com.example.moviezoom.presentation.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezoom.R
import com.example.moviezoom.databinding.FragmentListBinding
import com.example.moviezoom.presentation.activity.MainViewModel
import com.example.moviezoom.presentation.uistate.ErrorType
import com.example.moviezoom.presentation.uistate.MovieUiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val mainViewModel: MainViewModel by activityViewModel()
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as? SearchView

                searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                        isSearching = true
                        binding.loadMoreButton.visibility = View.GONE
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        isSearching = false
                        mainViewModel.getMovies()
                        return true
                    }
                })

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrBlank()) {
                            mainViewModel.search(query)
                        }
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        if (mainViewModel.uiState.value !is MovieUiState.Success) {
            mainViewModel.getMovies()
        }

        val adapter = MoviesAdapter { movie ->
            mainViewModel.selectMovie(movie)
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isSearching) {
                    binding.loadMoreButton.visibility = View.GONE
                    return
                }

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == totalItemCount - 1) {
                    binding.loadMoreButton.visibility = View.VISIBLE
                } else {
                    binding.loadMoreButton.visibility = View.GONE
                }
            }
        })

        binding.recyclerView.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.getMovies()
        }

        binding.retryButton.setOnClickListener {
            mainViewModel.getMovies()
        }

        binding.loadMoreButton.setOnClickListener {
            mainViewModel.loadMoreMovies()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { state ->
                    handleUiState(state, adapter)
                }
            }
        }
    }

    private fun handleUiState(state: MovieUiState, adapter: MoviesAdapter) {
        binding.swipeRefreshLayout.isRefreshing = state is MovieUiState.Loading

        when (state) {
            is MovieUiState.Success -> {
                binding.errorLayout.visibility = View.GONE
                if (state.movies.isEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.loadMoreButton.visibility = View.GONE
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyTextView.visibility = View.GONE
                    adapter.setList(state.movies)
                }
            }

            is MovieUiState.Error -> {
                binding.recyclerView.visibility = View.GONE
                binding.emptyTextView.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
                binding.loadMoreButton.visibility = View.GONE
                
                binding.errorTextView.text = when (state.errorType) {
                    is ErrorType.Network -> getString(R.string.error_internet)
                    is ErrorType.Unknown -> state.errorType.message ?: getString(R.string.error_loading_movies)
                }
            }

            is MovieUiState.Loading -> {
                if (adapter.itemCount == 0) {
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyTextView.visibility = View.GONE
                }
            }
        }
    }
}