package com.example.movieapp.uiscreens.movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.adapters.MoviesAdapter
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.uiscreens.details.MovieDetailsActivity
import com.example.movieapp.util.Constants.Companion.SEARCH
import com.example.movieapp.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val moviesViewModel: MoviesViewModel by viewModels()
    private val mAdapter by lazy { MoviesAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        mAdapter.onItemClick = { movieID ->
            startMovieDetailsActivity(movieID)
        }

        setupUI()

    }

    private fun setupUI() {
        setupRecyclerView()
        setupSearchView()
        observeData()
    }

    private fun observeData() {
        // Observe the PagingData and submit it to the adapter
        lifecycleScope.launch {
            moviesViewModel.getMoviesPaging(SEARCH).collectLatest { pagingData ->
                mAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    private fun setupSearchView() {
        binding.movieSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handleSearchQuery(newText)
                return true
            }
        })
    }

    private fun handleSearchQuery(newText: String?) {
        if (newText != null) {
            val query = newText.ifEmpty { SEARCH }
            lifecycleScope.launch {
                moviesViewModel.getMoviesPaging(query).collectLatest { pagingData ->
                    mAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun startMovieDetailsActivity(movieId: String) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("id", movieId)
        startActivity(intent)
    }
}