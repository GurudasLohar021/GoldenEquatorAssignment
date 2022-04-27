package com.example.goldenequatorassignment.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goldenequatorassignment.model.remote.upcoming.UpcomingMovies
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.repo.UpcomingMoviesRepo
import io.reactivex.schedulers.Schedulers


class UpcomingViewModel constructor (private var upcomingMoviesRepo: UpcomingMoviesRepo) : ViewModel()  {

    var upcomingMovieList: MutableLiveData<List<UpcomingMovies>> = MutableLiveData()
    var currentList: ArrayList<UpcomingMovies> = ArrayList()
    val connectionState : MutableLiveData<ConnectionState> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()

    var page : Int = 0

    @SuppressLint("CheckResult")
    fun fetchLiveUpcomingMoviesList() {

        if(connectionState.value == ConnectionState.LOADING){
            return
        }

        connectionState.postValue(ConnectionState.LOADING)

        page++

        val responseList = upcomingMoviesRepo.getUpcomingMovies(page)
        responseList.subscribeOn(Schedulers.io())
            .subscribe(
                {
                    currentList.addAll(it.results)
                    upcomingMovieList.postValue(currentList)
                    connectionState.postValue(ConnectionState.COMPLETED)
                },
                {
                    errorMessage.postValue(it.message)
                    connectionState.postValue(ConnectionState.ERROR)
                }
            )
    }

    fun listIsEmpty( ): Boolean{
        return upcomingMovieList.value?.isEmpty() ?: true
    }
}