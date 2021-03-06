package com.example.goldenequatorassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.model.remote.movie_details.MovieDetails
import com.example.goldenequatorassignment.repo.MovieDetailsRepo
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel(private val movieDetailsRepo: MovieDetailsRepo, movieId: Int) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieDetailsRepo.fetchMovieDetails(compositeDisposable, movieId)
    }

    val connectionState : LiveData<ConnectionState> by lazy {
        movieDetailsRepo.getMovieDetailsConnectionState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}