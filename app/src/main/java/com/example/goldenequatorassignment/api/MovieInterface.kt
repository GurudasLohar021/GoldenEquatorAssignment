package com.example.goldenequatorassignment.api

import com.example.goldenequatorassignment.vo.movie_details.Genre
import com.example.goldenequatorassignment.vo.movie_details.MovieDetails
import com.example.goldenequatorassignment.vo.now_playing.NowPlayingMoviesResponse
import com.example.goldenequatorassignment.vo.popular.PopularMoviesResponse
import com.example.goldenequatorassignment.vo.search_movies.SearchMoviesResponse
import com.example.goldenequatorassignment.vo.top_rated.TopRatedMoviesResponse
import com.example.goldenequatorassignment.vo.upcoming.UpcomingMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page : Int) : Single<NowPlayingMoviesResponse>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page : Int) : Single<PopularMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovie(@Query("page") page : Int) : Single<TopRatedMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovie(@Query("page") page : Int) : Single<UpcomingMoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id : Int) : Single<MovieDetails>

    @GET("search/movie")
    fun getSearchMovie(@Query("query") query : String, @Query("page") page : Int) : Single<SearchMoviesResponse>

    @GET("genre/movie/list")
    fun getGenre() : Single<Genre>

}