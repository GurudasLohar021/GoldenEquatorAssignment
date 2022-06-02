package com.example.goldenequatorassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.model.now_playing.NowPlayingMovies
import com.example.goldenequatorassignment.repo.NowPlayingMoviesPagedListRepo

import io.reactivex.disposables.CompositeDisposable

class NowPlayingViewModel(private val nowPlayingMoviesRepo : NowPlayingMoviesPagedListRepo) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    val nowPlayingMoviePagedList : LiveData<PagedList<NowPlayingMovies>> by lazy {
        nowPlayingMoviesRepo.fetchLiveNowPlayingPagedList(compositeDisposable)
    }

    val connectionState : LiveData<ConnectionState> by lazy {
        nowPlayingMoviesRepo.getConnectionState()
    }

    fun listIsEmpty() : Boolean{
        return nowPlayingMoviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}