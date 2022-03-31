package com.example.goldenequatorassignment.ui.search_page

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.api.POST_PER_PAGE
import com.example.goldenequatorassignment.repo.*
import com.example.goldenequatorassignment.vm.movie_details.MovieDetails
import com.example.goldenequatorassignment.vm.now_playing.NowPlayingMovies
import com.example.goldenequatorassignment.vm.search_movies.SearchMovies
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