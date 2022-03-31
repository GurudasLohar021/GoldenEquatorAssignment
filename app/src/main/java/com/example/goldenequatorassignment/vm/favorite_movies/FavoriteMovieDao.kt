package com.example.goldenequatorassignment.vm.favorite_movies

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteMovieDao {

    @Insert
    suspend fun addToFavorite(favoriteMovieDetails: FavoriteMovieDetails)

    @Query("SELECT * FROM favorite_movie_details")

    fun getFavoriteMovie() : LiveData<List<FavoriteMovieDetails>>

    @Query("SELECT count(*) FROM favorite_movie_details WHERE favorite_movie_details.id_movie = :id")

    suspend fun checkMovie(id : String) : Int

    @Query("DELETE FROM favorite_movie_details WHERE favorite_movie_details.id_movie = :id")

    suspend fun removeFromFavorite(id : String) : Int
}