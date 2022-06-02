package com.example.goldenequatorassignment.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.model.remote.movie_details.MovieDetails
import com.example.goldenequatorassignment.state.ConnectionState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsDataSource
    (private  val apiService: MovieInterface, private val compositeDisposable: CompositeDisposable){

        private val _connectionState = MutableLiveData<ConnectionState>()
        val connectionState: LiveData<ConnectionState>
        get() = _connectionState

        private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
        val downloadedMovieDetailsResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

        fun fetchMovieDetails(movieId: Int){
            _connectionState.postValue(ConnectionState.LOADING)

            try {
                compositeDisposable.add(
                    apiService.getMovieDetails(movieId)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            {
                                _downloadedMovieDetailsResponse.postValue(it)
                                _connectionState.postValue(ConnectionState.COMPLETED)
                            },
                            {
                                _connectionState.postValue(ConnectionState.ERROR)
                                Log.e("MovieDetailsDataSource",it.message.toString())
                            }
                        )
                )
            }catch (e: Exception){
                Log.e("MovieDetailsDataSource", e.message.toString())
            }
        }
}