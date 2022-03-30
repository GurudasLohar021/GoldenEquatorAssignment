package com.example.goldenequatorassignment.vm.favorite_movies

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.goldenequatorassignment.vm.movie_details.Genre

@Entity (tableName = "favorite_movie_details")
data class FavoriteMovieDetails(
    @PrimaryKey
    val id: Int,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int,
    val tagline: String,
    val title: String,
    )