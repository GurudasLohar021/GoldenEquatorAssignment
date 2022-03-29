package com.example.goldenequatorassignment.api

import com.example.goldenequatorassignment.vm.now_playing.NowPlayingMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page : Int) : Single<NowPlayingMoviesResponse>

}