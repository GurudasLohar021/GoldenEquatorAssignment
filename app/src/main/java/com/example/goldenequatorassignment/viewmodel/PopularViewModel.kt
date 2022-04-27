package com.example.goldenequatorassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.model.remote.popular.PopularMovies
import com.example.goldenequatorassignment.repo.PopularMoviesPagedListRepo
import io.reactivex.disposables.CompositeDisposable

class PopularViewModel(private val popularMoviesRepo : PopularMoviesPagedListRepo) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val popularMoviePagedList : LiveData<PagedList<PopularMovies>> by lazy {
        popularMoviesRepo.fetchLivePopularPagedList(compositeDisposable)
    }

    val connectionState : LiveData<ConnectionState> by lazy {
        popularMoviesRepo.getConnectionState()
    }

    fun listIsEmpty() : Boolean{
        return popularMoviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}