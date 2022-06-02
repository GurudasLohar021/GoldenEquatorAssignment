package com.example.goldenequatorassignment.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.data.NowPlayingMovieDataSource
import com.example.goldenequatorassignment.data.NowPlayingMovieDataSourceFactory
import com.example.goldenequatorassignment.model.now_playing.NowPlayingMovies
import com.example.goldenequatorassignment.rest.POST_PER_PAGE
import com.example.goldenequatorassignment.state.ConnectionState

import io.reactivex.disposables.CompositeDisposable

class NowPlayingMoviesPagedListRepo (private val apiService : MovieInterface) {


    lateinit var nowPlayingPagedList : LiveData<PagedList<NowPlayingMovies>>
    lateinit var nowPlayingDataSourceFactory : NowPlayingMovieDataSourceFactory


    fun  fetchLiveNowPlayingPagedList
                (compositeDisposable: CompositeDisposable)
                    : LiveData<PagedList<NowPlayingMovies>>{

        nowPlayingDataSourceFactory = NowPlayingMovieDataSourceFactory(apiService, compositeDisposable)

        val config : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        nowPlayingPagedList = LivePagedListBuilder(nowPlayingDataSourceFactory, config).build()

        return nowPlayingPagedList

    }

    fun getConnectionState() : LiveData<ConnectionState>{
        return Transformations.switchMap<NowPlayingMovieDataSource, ConnectionState>(
            nowPlayingDataSourceFactory.nowPlayingMoviesLiveDataSource, NowPlayingMovieDataSource::connectionState
        )
    }
}