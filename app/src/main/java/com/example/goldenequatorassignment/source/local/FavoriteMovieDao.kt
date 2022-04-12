package com.example.goldenequatorassignment.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.goldenequatorassignment.model.local.favorite_movie.FavoriteMovieDetails

@Dao
interface FavoriteMovieDao {

    @Insert
    suspend fun addToFavoriteMovie(favoriteMovieDetails: FavoriteMovieDetails) :Long

    @Query("SELECT * FROM favorite_movie_details")
    suspend fun getFavoriteMovie() : List<FavoriteMovieDetails>

    @Query("SELECT count(*) FROM favorite_movie_details WHERE favorite_movie_details.id_movie = :id")
    suspend fun checkMovie(id : String) : Int

    @Query("DELETE FROM favorite_movie_details WHERE favorite_movie_details.id_movie = :id")
    suspend fun removeFromFavorite(id : Int) : Int
}