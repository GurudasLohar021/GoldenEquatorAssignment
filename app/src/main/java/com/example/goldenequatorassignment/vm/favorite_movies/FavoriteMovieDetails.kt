package com.example.goldenequatorassignment.vm.favorite_movies

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import com.example.goldenequatorassignment.vm.movie_details.Genre
import java.io.Serializable
import kotlinx.parcelize.Parcelize

@Entity (tableName = "favorite_movie_details")
@Parcelize
data class FavoriteMovieDetails(
    val id_movie: Int,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int,
    val tagline: String,
    val title: String,
    ) : Serializable, Parcelable{
        @PrimaryKey(autoGenerate = true)
        var id : Int = 0
        val baseUrl get() = "https://image.tmdb.org/t/p/w500"
    }