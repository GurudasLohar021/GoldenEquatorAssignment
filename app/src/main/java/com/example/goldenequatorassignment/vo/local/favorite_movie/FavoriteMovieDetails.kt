package com.example.goldenequatorassignment.vo.local.favorite_movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "favorite_movie_details")
data class FavoriteMovieDetails(
    val id_movie: Int,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int,
    val tagline: String,
    val title: String,
    ) : Serializable {
        @PrimaryKey(autoGenerate = true)
        var id : Int = 0
    }