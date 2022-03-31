package com.example.goldenequatorassignment.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.vm.now_playing.NowPlayingMovies
import io.reactivex.disposables.CompositeDisposable

class NowPlayingMovieDataSourceFactory
    (private  val apiService: MovieInterface, private val compositeDisposable: CompositeDisposable)
            : DataSource.Factory<Int, NowPlayingMovies>() {

    val nowPlayingMoviesLiveDataSource = MutableLiveData<NowPlayingMovieDataSource>()

    override fun create(): DataSource<Int, NowPlayingMovies> {

      val nowPlayingMoviesDataSource = NowPlayingMovieDataSource(apiService, compositeDisposable)

        nowPlayingMoviesLiveDataSource.postValue(nowPlayingMoviesDataSource)

         return nowPlayingMoviesDataSource
    }
}