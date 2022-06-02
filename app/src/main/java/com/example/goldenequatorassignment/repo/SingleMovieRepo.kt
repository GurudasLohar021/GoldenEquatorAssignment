package com.example.goldenequatorassignment.repo

import com.example.goldenequatorassignment.model.remote.single_model.MovieModelResponse
import com.example.goldenequatorassignment.source.api.MovieInterface
import io.reactivex.Single

class SingleMovieRepo (private  val apiService: MovieInterface, private val movieTypeIndex : Int){

    lateinit var movieTypeList : Single<MovieModelResponse>

    fun getMoviesList(page : Int) : Single<MovieModelResponse> {

        when (movieTypeIndex) {
            1 -> {
                movieTypeList = apiService.getNowPlayingMovieSingle(page)
            }

            2 -> {
                movieTypeList = apiService.getPopularMovieSingle(page)
            }

            3 -> {
                movieTypeList = apiService.getTopRatedMovieSingle(page)
            }

            4 -> {
                movieTypeList = apiService.getUpcomingMovieSingle(page)
            }
        }
        return movieTypeList
    }
}