package com.example.goldenequatorassignment.bloc

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.model.now_playing.NowPlayingMovies
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.rest.FIRST_PAGE
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NowPlayingMovieDataSource
    (private  val apiService: MovieInterface, private val compositeDisposable: CompositeDisposable)
                : PageKeyedDataSource<Int, NowPlayingMovies>(){

    var page = FIRST_PAGE
    val connectionState : MutableLiveData<ConnectionState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NowPlayingMovies>
    ) {
        connectionState.postValue(ConnectionState.LOADING)


        compositeDisposable.add(
            apiService.getNowPlayingMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results, null, page+1)
                        connectionState.postValue(ConnectionState.COMPLETED)
                    },
                    {
                        connectionState.postValue(ConnectionState.ERROR)
                        Log.e("NowPlayingDataSource", it.message.toString())

                    }
                )
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NowPlayingMovies>
    ) {

    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NowPlayingMovies>
    ) {
        connectionState.postValue(ConnectionState.LOADING)

        compositeDisposable.add(
            apiService.getNowPlayingMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                     if(it.total_pages >= params.key){

                         callback.onResult(it.results, params.key+1)
                         connectionState.postValue(ConnectionState.COMPLETED)

                     }else{
                         connectionState.postValue(ConnectionState.ENDOFLIST)
                     }
                    },
                    {
                        connectionState.postValue(ConnectionState.ERROR)
                        Log.e("NowPlayingDataSource", it.message.toString())

                    }
                )
        )
    }
}