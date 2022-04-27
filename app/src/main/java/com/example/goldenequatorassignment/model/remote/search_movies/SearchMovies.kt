package com.example.goldenequatorassignment.model.remote.search_movies

import com.example.goldenequatorassignment.model.local.genres.Genre

data class SearchMovies(
    val adult: Boolean,
    val backdrop_path: Any,
    val genre_ids: List<Int>,
    var genreArrayList: ArrayList<Genre>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Float
)