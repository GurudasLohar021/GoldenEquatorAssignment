package com.example.goldenequatorassignment.vm.favorite_movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteMovieDao {

    @Insert
    suspend fun addToFavorite(favoriteMovieDetails: FavoriteMovieDetails)

    @Query("SELECT count(*) FROM favorite_movie_details WHERE favorite_movie_details.id = :id")

    suspend fun checkMovie(id : String) : Int

    @Query("DELETE FROM favorite_movie_details WHERE favorite_movie_details.id = :id")

    suspend fun removeFromFavorite(id : String) : Int
}