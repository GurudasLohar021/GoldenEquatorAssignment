package com.example.goldenequatorassignment.model.local.favorite_movie

import com.example.goldenequatorassignment.source.local.FavoriteMovieDao
import javax.inject.Inject

class FavoriteMovieRepo  @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
){
    suspend fun addToFavorite(favoriteMovieDetails: FavoriteMovieDetails) : Long {
        return favoriteMovieDao.addToFavoriteMovie(favoriteMovieDetails)
    }


    suspend fun getFavorite() : List<FavoriteMovieDetails>  = favoriteMovieDao.getFavoriteMovie()

    suspend fun checkFavorite(id : String) = favoriteMovieDao.checkMovie(id)

    suspend fun removeFromFavorite(id: Int){
        favoriteMovieDao.removeFromFavorite(id)
    }
}