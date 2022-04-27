package com.example.goldenequatorassignment.repo

import com.example.goldenequatorassignment.model.remote.upcoming.UpcomingMoviesResponse
import com.example.goldenequatorassignment.source.api.MovieInterface
import io.reactivex.Single

class UpcomingMoviesRepo constructor(private  val apiService: MovieInterface){


    fun getUpcomingMovies(page : Int) : Single<UpcomingMoviesResponse> {
        return apiService.getUpcomingMovie(page)
    }
}