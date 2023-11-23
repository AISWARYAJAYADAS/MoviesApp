package com.example.movieapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.movieapp.models.details.MovieDetailsRespnse
import com.example.movieapp.uiscreens.movie.MoviePaging
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getMovieDetails(
        imdbIDQuery: String,
        apiKey: String
    ): Response<MovieDetailsRespnse> {
        return remoteDataSource.getMovieDetails(imdbIDQuery, apiKey)
    }

    fun getMoviesPaging(searchQuery: String) =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { MoviePaging(searchQuery, remoteDataSource) }
        ).flow
}