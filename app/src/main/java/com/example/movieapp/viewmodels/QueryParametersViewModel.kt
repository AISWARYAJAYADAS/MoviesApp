package com.example.movieapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.movieapp.util.Constants.Companion.API_KEY
import com.example.movieapp.util.Constants.Companion.QUERY_API_KEY
import com.example.movieapp.util.Constants.Companion.QUERY_SEARCH
import com.example.movieapp.util.Constants.Companion.SEARCH
import javax.inject.Inject

class QueryParametersViewModel @Inject constructor(): ViewModel() {
    fun applyQueries(): Map<String, String> {
        return mapOf(
            QUERY_API_KEY to API_KEY,
            QUERY_SEARCH to SEARCH
        )
    }
}