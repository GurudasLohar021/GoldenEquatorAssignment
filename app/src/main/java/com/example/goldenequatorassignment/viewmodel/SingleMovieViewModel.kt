package com.example.goldenequatorassignment.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goldenequatorassignment.model.remote.single_model.MovieModel
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.repo.SingleMovieRepo
import io.reactivex.schedulers.Schedulers

class SingleMovieViewModel(private var  singleMovieRepo: SingleMovieRepo) : ViewModel() {

    var updatedMovieList: MutableLiveData<List<MovieModel>> = MutableLiveData()
    var currentMovieList: ArrayList<MovieModel> = ArrayList()
    val connectionState : MutableLiveData<ConnectionState> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()

    var page : Int = 0


    @SuppressLint("CheckResult")
    fun fetchLiveMovieTypeList() {

        if(connectionState.value == ConnectionState.LOADING){
            return
        }

        connectionState.postValue(ConnectionState.LOADING)

        page++

        val responseList = singleMovieRepo.getMoviesList(page)
        responseList.subscribeOn(Schedulers.io())
            .subscribe(
                {
                    currentMovieList.addAll(it.results)
                    updatedMovieList.postValue(currentMovieList)
                    connectionState.postValue(ConnectionState.COMPLETED)
                },
                {
                    errorMessage.postValue(it.message)
                    connectionState.postValue(ConnectionState.ERROR)
                }
            )
    }

    fun listIsEmpty( ): Boolean{
        return updatedMovieList.value?.isEmpty() ?: true
    }
}