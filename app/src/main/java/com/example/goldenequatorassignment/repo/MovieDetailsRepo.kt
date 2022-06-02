package com.example.goldenequatorassignment.repo

import androidx.lifecycle.LiveData
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.data.MovieDetailsDataSource
import com.example.goldenequatorassignment.model.remote.movie_details.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepo(private val apiService : MovieInterface) {

    lateinit var movieDetailsDataSource: MovieDetailsDataSource

    fun fetchMovieDetails
                (compositeDisposable: CompositeDisposable, movieId: Int)
                    : LiveData<MovieDetails>{
        movieDetailsDataSource = MovieDetailsDataSource(apiService, compositeDisposable)
        movieDetailsDataSource.fetchMovieDetails(movieId)

        return movieDetailsDataSource.downloadedMovieDetailsResponse

    }

    fun getMovieDetailsConnectionState() : LiveData<ConnectionState>{
        return movieDetailsDataSource.connectionState
    }

}