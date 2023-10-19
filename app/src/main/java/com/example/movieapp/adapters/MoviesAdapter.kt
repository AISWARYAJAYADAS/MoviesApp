package com.example.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.MoviesRowLayoutBinding
import com.example.movieapp.models.Search

class MoviesAdapter : PagingDataAdapter<Search, MoviesAdapter.MovieViewHolder>(COMPARATOR) {
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            MoviesRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie)
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(movie.imdbID)
            }
        }
    }

    class MovieViewHolder(private val binding: MoviesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Search) {
            binding.movie = movie
            binding.executePendingBindings()
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Search>() {
            override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
                return oldItem.imdbID == newItem.imdbID
            }

            override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
                return oldItem == newItem
            }
        }
    }
}
