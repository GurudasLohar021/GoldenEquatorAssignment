package com.example.goldenequatorassignment.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.goldenequatorassignment.data.SearchMovieDataSource
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMovies
import io.reactivex.disposables.CompositeDisposable

class SearchMovieDataSourceFactory
    (private  val apiService: MovieInterface, private val compositeDisposable: CompositeDisposable, val query : String)
    : DataSource.Factory<Int, SearchMovies>(){

    val searchMoviesLiveDataSource = MutableLiveData<SearchMovieDataSource>()

    override fun create(): DataSource<Int, SearchMovies> {

        val searchMovieDataSource = SearchMovieDataSource(apiService, compositeDisposable,query)

        searchMoviesLiveDataSource.postValue(searchMovieDataSource)

        return searchMovieDataSource

    }


}