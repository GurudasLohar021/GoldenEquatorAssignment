package com.example.goldenequatorassignment.ui.movie_details_page

import androidx.lifecycle.LiveData
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.bloc.MovieDetailsDataSource
import com.example.goldenequatorassignment.source.local.FavoriteMovieDao
import com.example.goldenequatorassignment.model.remote.movie_details.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepo(private val apiService : MovieInterface) {

    lateinit var movieDetailsDataSource: MovieDetailsDataSource
    lateinit var  favoriteMovieDao: FavoriteMovieDao

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