package com.example.goldenequatorassignment.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.vo.movie_details.MovieDetails
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