package com.example.goldenequatorassignment.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.goldenequatorassignment.api.FIRST_PAGE
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.vm.now_playing.NowPlayingMovies
import com.example.goldenequatorassignment.vm.popular.PopularMovies
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularMoviesDataSource
    (private  val apiService: MovieInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, PopularMovies>(){

    var page = FIRST_PAGE
    val connectionState : MutableLiveData<ConnectionState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMovies>
    ) {
        connectionState.postValue(ConnectionState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results, null, page+1)
                        connectionState.postValue(ConnectionState.COMPLETED)
                    },
                    {
                        connectionState.postValue(ConnectionState.ERROR)
                        Log.e("PopularDataSource", it.message.toString())

                    }
                )
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovies>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovies>) {
        connectionState.postValue(ConnectionState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
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
                        Log.e("PopularDataSource", it.message.toString())

                    }
                )
        )
    }
}