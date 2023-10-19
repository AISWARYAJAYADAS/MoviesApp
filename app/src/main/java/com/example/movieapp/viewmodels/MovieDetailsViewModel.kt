package com.example.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Repository
import com.example.movieapp.models.details.MovieDetailsRespnse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    // LiveData to hold the movie details
    private val _movieDetailState = MutableLiveData<MovieDetailState>()
    val movieDetailState: LiveData<MovieDetailState> get() = _movieDetailState

    // Function to fetch movie details
    fun fetchMovieDetails(imdbIDQuery: String, apiKey: String) {
        _movieDetailState.value = MovieDetailState.Loading
        viewModelScope.launch {
            try {
                val movieDetail = repository.getMovieDetails(imdbIDQuery, apiKey)
                _movieDetailState.value = MovieDetailState.Success(movieDetail)
            } catch (e: HttpException) {
                // Handle HTTP errors (e.g., 404 Not Found)
                _movieDetailState.value = MovieDetailState.Error("Movie details not found")
            } catch (e: IOException) {
                // Handle network-related errors (e.g., no internet connection)
                _movieDetailState.value = MovieDetailState.Error("Network error")
            } catch (e: Exception) {
                // Handle other exceptions
                _movieDetailState.value = MovieDetailState.Error("Failed to fetch movie details")
            }
        }
    }


    sealed class MovieDetailState {
        object Loading : MovieDetailState()
        data class Success(val movieDetail: Response<MovieDetailsRespnse>) : MovieDetailState()
        data class Error(val errorMessage: String) : MovieDetailState()
    }
}
