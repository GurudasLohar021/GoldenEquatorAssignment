package com.example.goldenequatorassignment.ui.home_page.now_playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.api.POST_PER_PAGE
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.repo.NowPlayingMovieDataSource
import com.example.goldenequatorassignment.repo.NowPlayingMovieDataSourceFactory
import com.example.goldenequatorassignment.vo.now_playing.NowPlayingMovies
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
        return Transformations.switchMap<NowPlayingMovieDataSource,ConnectionState>(
            nowPlayingDataSourceFactory.nowPlayingMoviesLiveDataSource, NowPlayingMovieDataSource::connectionState
        )
    }
}