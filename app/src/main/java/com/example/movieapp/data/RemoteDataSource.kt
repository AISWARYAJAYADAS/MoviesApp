package com.example.movieapp.data

import com.example.movieapp.data.network.MovieApiService
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.models.details.MovieDetailsRespnse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val movieApiService: MovieApiService
) {
    suspend fun getMovies(searchQuery: String, page:Int,apiKey: String): Response<MovieResponse> {
        return movieApiService.getMovies(searchQuery,page, apiKey)
    }
    suspend fun getMovieDetails(imdbIDQuery: String,apiKey: String) : Response<MovieDetailsRespnse>{
        return movieApiService.getMovieDetails(imdbIDQuery, apiKey)
    }
}