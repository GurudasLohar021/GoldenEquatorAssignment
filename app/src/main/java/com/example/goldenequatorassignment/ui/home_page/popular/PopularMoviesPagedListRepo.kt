package com.example.goldenequatorassignment.ui.home_page.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.api.POST_PER_PAGE
import com.example.goldenequatorassignment.repo.*

import com.example.goldenequatorassignment.vm.now_playing.NowPlayingMovies
import com.example.goldenequatorassignment.vm.popular.PopularMovies
import io.reactivex.disposables.CompositeDisposable

class PopularMoviesPagedListRepo (private val apiService : MovieInterface)  {

    lateinit var popularPagedList : LiveData<PagedList<PopularMovies>>
    lateinit var popularDataSourceFactory : PopularMoviesDataSourceFactory


    fun fetchLivePopularPagedList
                (compositeDisposable: CompositeDisposable)
            : LiveData<PagedList<PopularMovies>>{

        popularDataSourceFactory = PopularMoviesDataSourceFactory(apiService, compositeDisposable)

        val config : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        popularPagedList = LivePagedListBuilder(popularDataSourceFactory, config).build()

        return popularPagedList

    }

    fun getConnectionState() : LiveData<ConnectionState>{
        return Transformations.switchMap<PopularMoviesDataSource, ConnectionState>(
            popularDataSourceFactory.popularMoviesLiveDataSource, PopularMoviesDataSource::connectionState
        )
    }

}