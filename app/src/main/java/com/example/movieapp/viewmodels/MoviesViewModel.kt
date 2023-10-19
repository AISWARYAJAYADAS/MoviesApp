package com.example.movieapp.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.paging.PagingData
import com.example.movieapp.MyApplication
import com.example.movieapp.data.Repository
import com.example.movieapp.models.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: Repository,
    application: MyApplication
) : AndroidViewModel(application) {
    fun getMoviesPaging(searchQuery: String): Flow<PagingData<Search>> {
        return repository.getMoviesPaging(searchQuery)
    }
}

