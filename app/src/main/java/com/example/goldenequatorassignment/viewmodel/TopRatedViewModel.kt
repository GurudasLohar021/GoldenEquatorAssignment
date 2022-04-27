package com.example.goldenequatorassignment.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goldenequatorassignment.model.remote.top_rated.TopRatedMovies
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.repo.TopRatedMoviesRepo
import io.reactivex.schedulers.Schedulers

class TopRatedViewModel(private var topRatedMoviesRepo: TopRatedMoviesRepo) : ViewModel() {


    var topMovieList : MutableLiveData<List<TopRatedMovies>> = MutableLiveData()
    var currentList : ArrayList<TopRatedMovies> = ArrayList()
    val connectionState : MutableLiveData<ConnectionState> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()

    var page : Int = 0


    @SuppressLint("CheckResult")
    fun fetchLiveTopRatedMoviesList() {

        if(connectionState.value == ConnectionState.LOADING){
            return
        }

        connectionState.postValue(ConnectionState.LOADING)

        page++

        val responseList = topRatedMoviesRepo.getTopRatedMovies(page)
        responseList.subscribeOn(Schedulers.io())
            .subscribe(
                {
                    currentList.addAll(it.results)
                    topMovieList.postValue(currentList)
                    connectionState.postValue(ConnectionState.COMPLETED)
                },
                {
                    errorMessage.postValue(it.message)
                    connectionState.postValue(ConnectionState.ERROR)
                }
            )
    }

    fun listIsEmpty() : Boolean{
        return topMovieList.value?.isEmpty() ?: true
    }
}