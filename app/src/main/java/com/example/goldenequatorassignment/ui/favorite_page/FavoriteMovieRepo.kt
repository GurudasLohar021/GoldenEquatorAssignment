package com.example.goldenequatorassignment.ui.favorite_page

import com.example.goldenequatorassignment.vm.favorite_movies.FavoriteMovieDao
import com.example.goldenequatorassignment.vm.favorite_movies.FavoriteMovieDetails
import javax.inject.Inject

class FavoriteMovieRepo  @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
){
    suspend fun addToFavorite(favoriteMovieDetails: FavoriteMovieDetails) =
        favoriteMovieDao.addToFavorite(favoriteMovieDetails)

    suspend fun checkMovie(id : String) = favoriteMovieDao.checkMovie(id)

    suspend fun removeFromFavorite(id: String){
        favoriteMovieDao.removeFromFavorite(id)
    }
}