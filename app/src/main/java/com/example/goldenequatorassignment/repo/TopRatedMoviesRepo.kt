package com.example.goldenequatorassignment.repo

import com.example.goldenequatorassignment.model.remote.top_rated.TopRatedMoviesResponse
import com.example.goldenequatorassignment.source.api.MovieInterface
import io.reactivex.Single

class TopRatedMoviesRepo(private val apiService : MovieInterface) {


    fun getTopRatedMovies(page: Int) : Single<TopRatedMoviesResponse> {
        return apiService.getTopRatedMovie(page)
    }

}