package com.example.moviezoom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.moviezoom.databinding.FragmentDetailsBinding
import com.example.moviezoom.network.IMAGE_BASE_URL

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = mainViewModel.selectedMovie.value
        Log.d(TAG, "onViewCreated: $movie")
        movie?.let {
            binding.titleTextView.text = movie.title
            binding.releaseDateTextView.text = movie.releaseDate
            binding.overviewTextView.text = movie.overview
            Glide.with(binding.posterImage.context)
                .load("$IMAGE_BASE_URL${movie.posterPath}")
                .into(binding.posterImage)
            binding.ratingBar.rating= movie.voteAverage.toFloat()
            binding.voteAverageTextView.text = "%.1f/10".format(movie.voteAverage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.clearSelectedMovie()

    }
}