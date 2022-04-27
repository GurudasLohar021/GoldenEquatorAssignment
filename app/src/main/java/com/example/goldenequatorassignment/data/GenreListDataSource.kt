package com.example.goldenequatorassignment.data

import android.annotation.SuppressLint
import android.util.Log
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.source.api.MovieInterface
import io.reactivex.schedulers.Schedulers

class GenreListDataSource(private  val apiService: MovieInterface) {

    @SuppressLint("CheckResult")
    fun fetchGenresList() : ArrayList<Genre>{
        val genreFromAPI : ArrayList<Genre> = ArrayList()
        apiService.getGenre()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    genreFromAPI.addAll(it.genres)
                    println("!!!!!!@@@@########$$$$$$$$")
                    println(genreFromAPI)
                },
                {
                    Log.e("GenreDataSource", it.message.toString())
                }
            )
        return genreFromAPI
    }
}