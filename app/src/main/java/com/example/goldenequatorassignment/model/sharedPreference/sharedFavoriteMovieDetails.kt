package com.example.goldenequatorassignment.model.sharedPreference

data class SharedFavoriteMovieDetails (
    val id_movie: Int,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int,
    val tagline: String,
    val title: String,
)
