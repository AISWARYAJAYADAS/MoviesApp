package com.example.movieapp.uiscreens.movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.BuildConfig
import com.example.movieapp.R
import com.example.movieapp.adapters.MoviesAdapter
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.uiscreens.details.MovieDetailsActivity
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
        observeMovies()
    }

    private fun observeMovies() {
        lifecycleScope.launch {
            moviesViewModel.getMoviesPaging(BuildConfig.SEARCH).collectLatest { pagingData ->
                mAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvMovieList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }

        mAdapter.addLoadStateListener { loadState ->
            handleLoadState(loadState)
        }
    }

    private fun handleLoadState(loadState: androidx.paging.CombinedLoadStates) {
        when (loadState.source.refresh) {
            is LoadState.Loading -> showLoadingState()
            is LoadState.Error -> showErrorState()
            else -> {} // Handle other states if needed
        }
    }

    private fun showLoadingState() {
        binding.tvErrorTextView.visibility = View.GONE
        binding.rvMovieList.visibility = View.VISIBLE
    }

    private fun showErrorState() {
        binding.tvErrorTextView.visibility = View.VISIBLE
        binding.rvMovieList.visibility = View.GONE
    }

    private fun setupSearchView() {
        binding.svMovieList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if needed in the future
                return false
            }
        })

        binding.svMovieList.setOnCloseListener {
            handleSearchQuery(BuildConfig.SEARCH)
            false // Return false to allow the normal close behavior
        }
    }

    private fun handleSearchQuery(newText: String?) {
        val query = newText.orEmpty().ifEmpty { BuildConfig.SEARCH }
        lifecycleScope.launch {
            moviesViewModel.getMoviesPaging(query).collectLatest { pagingData ->
                mAdapter.submitData(pagingData)
            }
        }
    }

    private fun startMovieDetailsActivity(movieId: String) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("id", movieId)
        startActivity(intent)
    }
}


//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//    private val moviesViewModel: MoviesViewModel by viewModels()
//    private val mAdapter by lazy { MoviesAdapter() }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        setContentView(binding.root)
//
//        mAdapter.onItemClick = { movieID ->
//            startMovieDetailsActivity(movieID)
//        }
//
//        setupUI()
//
//    }
//
//    private fun setupUI() {
//        setupRecyclerView()
//        setupSearchView()
//        observeData()
//    }
//
//    private fun observeData() {
//        // Observe the PagingData and submit it to the adapter
//        lifecycleScope.launch {
//            moviesViewModel.getMoviesPaging(BuildConfig.SEARCH).collectLatest { pagingData ->
//                mAdapter.submitData(pagingData)
//            }
//        }
//    }
//
//
//
//    private fun setupRecyclerView() {
//        binding.rvMovieList.apply {
//            adapter = mAdapter
//            layoutManager = LinearLayoutManager(applicationContext)
//        }
//
//        mAdapter.addLoadStateListener { loadState ->
//            when (loadState.source.refresh) {
//                is LoadState.Loading -> {
//                    binding.tvErrorTextView.visibility = View.GONE
//                    binding.rvMovieList.visibility=View.VISIBLE
//                    binding.rvMovieList.apply {
//                        adapter = mAdapter
//                    }
//                }
//                is LoadState.Error -> {
//                    binding.tvErrorTextView.visibility=View.VISIBLE
//                    binding.rvMovieList.visibility =View.GONE
//                }
//                else -> {}
//            }
//        }
//    }
//
//
//
//
//    private fun setupSearchView() {
//        binding.svMovieList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                handleSearchQuery(query)
//                return true
//            }
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
//        binding.svMovieList.setOnCloseListener {
//            handleSearchQuery(BuildConfig.SEARCH)
//            false // Return false to allow the normal close behavior
//        }
//    }
//
//
//
//    private fun handleSearchQuery(newText: String?) {
//        if (newText != null) {
//            val query = newText.ifEmpty { BuildConfig.SEARCH }
//            lifecycleScope.launch {
//                moviesViewModel.getMoviesPaging(query).collectLatest { pagingData ->
//                    mAdapter.submitData(pagingData)
//                }
//            }
//        }
//    }
//
//
//
//
//    private fun startMovieDetailsActivity(movieId: String) {
//        val intent = Intent(this, MovieDetailsActivity::class.java)
//        intent.putExtra("id", movieId)
//        startActivity(intent)
//    }
//}