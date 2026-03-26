package com.example.moviezoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviezoom.databinding.ListItemBinding
import com.example.moviezoom.network.IMAGE_BASE_URL
import com.example.moviezoom.network.Movie

class MoviesAdapter(private val onMovieClick: (Movie) -> Unit) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var movies: List<Movie> = emptyList()

    fun setList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    class MovieViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.title.text = movie.title
        holder.binding.rating.text = "%.1f".format(movie.voteAverage)
        Glide.with(holder.binding.imageView.context)
            .load("$IMAGE_BASE_URL${movie.posterPath}")
            .into(holder.binding.imageView)
        
        holder.binding.cardView.setOnClickListener {
            onMovieClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size
}
