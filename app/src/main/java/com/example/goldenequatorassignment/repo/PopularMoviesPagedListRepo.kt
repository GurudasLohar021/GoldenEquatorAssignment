package com.example.goldenequatorassignment.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.goldenequatorassignment.data.PopularMoviesDataSource
import com.example.goldenequatorassignment.data.PopularMoviesDataSourceFactory
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.repo.*

import com.example.goldenequatorassignment.model.remote.popular.PopularMovies
import com.example.goldenequatorassignment.rest.POST_PER_PAGE
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