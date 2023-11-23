package com.example.movieapp.uiscreens.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.BuildConfig
import com.example.movieapp.data.RemoteDataSource
import com.example.movieapp.models.Search

class MoviePaging(private val s: String, private val remoteDataSource: RemoteDataSource) :
    PagingSource<Int, Search>() {
    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        val page = params.key ?: 1
        return try {
            val query = if(s == "") BuildConfig.SEARCH else  s
            val data = remoteDataSource.getMovies(query, page, BuildConfig.API_KEY)
            val movies = data.body()?.search ?: emptyList()

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.body()?.search?.isEmpty()!!) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}


