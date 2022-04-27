package com.example.goldenequatorassignment.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.room.Query
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMovies
import com.example.goldenequatorassignment.model.remote.upcoming.UpcomingMovies
import com.example.goldenequatorassignment.repo.SearchMoviesRepo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchMoviesViewModel(private val searchMoviesRepo: SearchMoviesRepo): ViewModel() {

    var searchMoviesList : MutableLiveData<List<SearchMovies>> = MutableLiveData()
    var currentList: ArrayList<SearchMovies> = ArrayList()
    val connectionState : MutableLiveData<ConnectionState> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()


    var page : Int = 0

    @SuppressLint("CheckResult")
    fun searchUpdatedList(query : String){

        if(connectionState.value == ConnectionState.LOADING){
            return
        }

        connectionState.postValue(ConnectionState.LOADING)

        page++

        val responseList = searchMoviesRepo.fetchLiveSearchMovieList(page, query)
        responseList.subscribeOn(Schedulers.io())
            .subscribe(
                {
                    currentList.addAll(it.results)
                    searchMoviesList.postValue(currentList)
                    connectionState.postValue(ConnectionState.COMPLETED)
                },
                {
                    errorMessage.postValue(it.message)
                    connectionState.postValue(ConnectionState.ERROR)
                }
            )
    }


    fun listIsEmpty() : Boolean{
        return searchMoviesList.value?.isEmpty() ?: true
    }

}