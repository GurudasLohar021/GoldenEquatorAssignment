package com.example.goldenequatorassignment.ui.search_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.goldenequatorassignment.bloc.SearchMovieDataSource
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.repo.*
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMovies
import com.example.goldenequatorassignment.rest.POST_PER_PAGE
import io.reactivex.disposables.CompositeDisposable

class SearchMoviesRepo (private val apiService : MovieInterface, val query: String){

    lateinit var searchMovieDataSourceFactory: SearchMovieDataSourceFactory
    lateinit var searchMoviesList: LiveData<PagedList<SearchMovies>>

    fun  fetchLiveSearchMovieList
                (compositeDisposable: CompositeDisposable)
            : LiveData<PagedList<SearchMovies>>{

        searchMovieDataSourceFactory = SearchMovieDataSourceFactory(apiService, compositeDisposable, query)


        val config : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        searchMoviesList = LivePagedListBuilder(searchMovieDataSourceFactory, config).build()

        return searchMoviesList

    }


    fun getConnectionState() : LiveData<ConnectionState>{
        return Transformations.switchMap<SearchMovieDataSource,ConnectionState>(
            searchMovieDataSourceFactory.searchMoviesLiveDataSource, SearchMovieDataSource :: connectionState
        )
    }
}