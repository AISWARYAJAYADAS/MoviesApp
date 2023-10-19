package com.example.movieapp.data.network

import com.example.movieapp.models.MovieResponse
import com.example.movieapp.models.details.MovieDetailsRespnse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("/")
    suspend fun getMovies(
        @Query("s") searchQuery: String,
        @Query("page") page: Int,
        @Query("apikey") apiKey: String
    ): Response<MovieResponse>

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbIDQuery: String,
        @Query("apikey") apiKey: String
    ): Response<MovieDetailsRespnse>
}
