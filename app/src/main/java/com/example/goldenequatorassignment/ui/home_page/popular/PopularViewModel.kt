package com.example.goldenequatorassignment.ui.home_page.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.vo.popular.PopularMovies
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