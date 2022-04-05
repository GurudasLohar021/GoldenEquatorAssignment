package com.example.goldenequatorassignment.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.vo.remote.movie_details.popular.PopularMovies
import io.reactivex.disposables.CompositeDisposable

class PopularMoviesDataSourceFactory
    (private  val apiService: MovieInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, PopularMovies>() {

    val popularMoviesLiveDataSource = MutableLiveData<PopularMoviesDataSource>()

    override fun create(): DataSource<Int, PopularMovies> {

        val popularMoviesDataSource = PopularMoviesDataSource(apiService, compositeDisposable)

        popularMoviesLiveDataSource.postValue(popularMoviesDataSource)
        return popularMoviesDataSource

    }
}