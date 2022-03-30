package com.example.goldenequatorassignment.vm.favorite_movies

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteMovieDetails::class],
    version = 1

)

abstract class FavoriteMovieDatabase : RoomDatabase(){
    abstract fun getFavoriteMovieDao() : FavoriteMovieDao
}