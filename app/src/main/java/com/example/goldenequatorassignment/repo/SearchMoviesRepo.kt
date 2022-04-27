package com.example.goldenequatorassignment.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Query
import com.example.goldenequatorassignment.data.SearchMovieDataSource
import com.example.goldenequatorassignment.data.SearchMovieDataSourceFactory
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.repo.*
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMovies
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMoviesResponse
import com.example.goldenequatorassignment.rest.POST_PER_PAGE
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class SearchMoviesRepo (private val apiService : MovieInterface){


    fun  fetchLiveSearchMovieList(page: Int, query: String) : Single<SearchMoviesResponse>{
        return apiService.getSearchMovie(query, page)
    }

}