package com.example.goldenequatorassignment.vo.favorite_movies

import javax.inject.Inject

class FavoriteMovieRepo  @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
){
    suspend fun addToFavorite(favoriteMovieDetails: FavoriteMovieDetails) =
        favoriteMovieDao.addToFavorite(favoriteMovieDetails)

    fun getFavoriteMovies() = favoriteMovieDao.getFavoriteMovie()

    suspend fun checkMovie(id : String) = favoriteMovieDao.checkMovie(id)

    suspend fun removeFromFavorite(id: String){
        favoriteMovieDao.removeFromFavorite(id)
    }
}