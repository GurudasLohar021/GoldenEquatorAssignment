package com.example.goldenequatorassignment.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goldenequatorassignment.model.local.favorite_movie.FavoriteMovieDetails

@Database(
    entities = [FavoriteMovieDetails::class],
    version = 1
)

abstract class FavoriteMovieDatabase : RoomDatabase(){

    abstract fun getFavoriteMovieDao() : FavoriteMovieDao


    companion object{
        var INSTANCE: FavoriteMovieDatabase?=null
        fun getInstance(context: Context): FavoriteMovieDatabase
        {
            if (INSTANCE == null){

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteMovieDatabase::class.java,
                    "favorite_movie_details.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}