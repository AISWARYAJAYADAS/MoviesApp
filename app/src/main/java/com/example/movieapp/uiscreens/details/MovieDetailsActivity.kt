package com.example.movieapp.uiscreens.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMovieDetailsBinding
import com.example.movieapp.util.Constants
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

       // supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idValue = intent.getStringExtra("id").toString()
        getMovieDetails(idValue)
        observeMovieDetailData()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // This will navigate to the previous screen
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMovieDetails(idValue:String) {
        movieDetailsViewModel.fetchMovieDetails(idValue, Constants.API_KEY)
    }
    private fun observeMovieDetailData() {
        movieDetailsViewModel.movieDetailState.observe(this) { response ->
            when (response) {
                is MovieDetailsViewModel.MovieDetailState.Success -> {
                    response.movieDetail.let { apiResponse ->
                        binding.moviePosterImageView.load(apiResponse.body()?.poster) {
                            crossfade(600)
                            //  .error(R.drawable.ic_placeholder_image)
                        }
                        binding.movieTitleTextView.text= response.movieDetail.body()?.title ?: "movie title"
                        binding.movieYearTextView.text= response.movieDetail.body()?.year ?: "movie year"
                        binding.movieTypeTextView.text= response.movieDetail.body()?.type ?: "movie type"
                        binding.directorTextView2.text= response.movieDetail.body()?.director ?: "movie director"
                        binding.writerTextView2.text= response.movieDetail.body()?.writer ?: "movie writer"




                    }
                }

                is MovieDetailsViewModel.MovieDetailState.Error -> {
                    Toast.makeText(
                        this,
                        response.errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }

                is MovieDetailsViewModel.MovieDetailState.Loading -> {
                    //  binding.progressBar.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }
}