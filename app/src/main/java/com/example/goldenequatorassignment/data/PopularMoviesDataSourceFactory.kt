package com.example.goldenequatorassignment.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.goldenequatorassignment.data.PopularMoviesDataSource
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.model.remote.popular.PopularMovies
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