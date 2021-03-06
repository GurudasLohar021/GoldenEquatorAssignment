package com.example.goldenequatorassignment.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMovies
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.rest.FIRST_PAGE
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchMovieDataSource
    (private  val apiService: MovieInterface, private val compositeDisposable: CompositeDisposable, val  query : String)
    : PageKeyedDataSource<Int, SearchMovies>(){

    var page = FIRST_PAGE
    val connectionState : MutableLiveData<ConnectionState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SearchMovies>
    ) {
        connectionState.postValue(ConnectionState.LOADING)

        compositeDisposable.add(
            apiService.getSearchMovie(query, page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results, null, page+1)
                        print(it.results)
                        connectionState.postValue(ConnectionState.COMPLETED)
                    },
                    {
                        connectionState.postValue(ConnectionState.ERROR)
                        Log.e("SearchMovieDataSource", it.message.toString())

                    }
                )
        )
    }


    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, SearchMovies>) {

        connectionState.postValue(ConnectionState.LOADING)

        compositeDisposable.add(
            apiService.getSearchMovie(query, params.key)
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
                        Log.e("SearchMovieDataSource", it.message.toString())

                    }
                )
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, SearchMovies>) {
        TODO("Not yet implemented")
    }
}