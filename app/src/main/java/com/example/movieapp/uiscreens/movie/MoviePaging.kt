package com.example.movieapp.uiscreens.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.RemoteDataSource
import com.example.movieapp.models.Search
import com.example.movieapp.util.Constants

class MoviePaging (private val s: String, private val remoteDataSource: RemoteDataSource): PagingSource<Int, Search>() {
    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        val page = params.key ?: 1
        return try {

            val data = remoteDataSource.getMovies(s, page, Constants.API_KEY)

            LoadResult.Page(
                data = data.body()?.search!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.body()?.search?.isEmpty()!!) null else page + 1
            )


        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}