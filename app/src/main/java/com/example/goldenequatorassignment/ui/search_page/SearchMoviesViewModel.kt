package com.example.goldenequatorassignment.ui.search_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMovies
import io.reactivex.disposables.CompositeDisposable

class SearchMoviesViewModel(private val searchMoviesRepo: SearchMoviesRepo): ViewModel() {


    private val compositeDisposable = CompositeDisposable()

    val searchMoviesList : LiveData<PagedList<SearchMovies>> by lazy {
        searchMoviesRepo.fetchLiveSearchMovieList(compositeDisposable)
    }

    val connectionState : LiveData<ConnectionState> by lazy {
        searchMoviesRepo.getConnectionState()
    }

    fun listIsEmpty() : Boolean{
        return searchMoviesList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}