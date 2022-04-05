package com.example.goldenequatorassignment.api

import com.example.goldenequatorassignment.vo.local.genres.Genre
import com.example.goldenequatorassignment.vo.local.genres.GenresObjectResponse
import com.example.goldenequatorassignment.vo.remote.movie_details.MovieDetails
import com.example.goldenequatorassignment.vo.remote.movie_details.now_playing.NowPlayingMoviesResponse
import com.example.goldenequatorassignment.vo.remote.movie_details.popular.PopularMoviesResponse
import com.example.goldenequatorassignment.vo.remote.movie_details.search_movies.SearchMoviesResponse
import com.example.goldenequatorassignment.vo.remote.movie_details.top_rated.TopRatedMoviesResponse
import com.example.goldenequatorassignment.vo.remote.movie_details.upcoming.UpcomingMoviesResponse
import io.reactivex.Single
import retrofit2.Call
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
    fun getGenre() : Single<GenresObjectResponse>

}