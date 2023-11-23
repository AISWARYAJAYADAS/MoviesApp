package com.example.movieapp.uiscreens.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.movieapp.BuildConfig
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMovieDetailsBinding
import com.example.movieapp.models.details.MovieDetailsRespnse
import com.example.movieapp.viewmodels.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()
    private var idValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        setContentView(binding.root)

        setSupportActionBar(binding.abMovieDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idValue = intent.getStringExtra("id").toString()
        getMovieDetails(idValue)
        observeMovieDetailData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMovieDetails(idValue: String) {
        movieDetailsViewModel.fetchMovieDetails(idValue, BuildConfig.API_KEY)
    }

    private fun observeMovieDetailData() {
        movieDetailsViewModel.movieDetailState.observe(this) { response ->
            when (response) {
                is MovieDetailsViewModel.MovieDetailState.Success -> {
                    handleSuccessState(response.movieDetail.body())
                }

                is MovieDetailsViewModel.MovieDetailState.Error -> {
                    showErrorState(response.errorMessage)
                }

                is MovieDetailsViewModel.MovieDetailState.Loading -> {
                    // Handle loading state if needed
                }

                else -> {}
            }
        }
    }

    private fun handleSuccessState(movieDetail: MovieDetailsRespnse?) {
        movieDetail?.let {
            with(binding) {
                ivMovieDetailMoviePoster.load(it.poster) {
                    crossfade(600)
                    //  .error(R.drawable.ic_placeholder_image)
                }
                tvMovieDetailTitle.text = it.title
                tvMovieDetailYear.text = it.year
                tvMovieDetailType.text = it.type
                tvMovieDetailDirector.text = it.director
                tvMovieDetailWriter2.text = it.writer
            }
        }
    }

    private fun showErrorState(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }
}


