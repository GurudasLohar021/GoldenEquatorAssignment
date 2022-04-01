package com.example.goldenequatorassignment.vo.local.favorite_movies

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [FavoriteMovieDetails::class],
    version = 1
)

abstract class FavoriteMovieDatabase : RoomDatabase(){

    abstract fun getFavoriteMovieDao() : FavoriteMovieDao


    companion object{
        var INSTANCE:FavoriteMovieDatabase?=null
        fun getInstance(context: Context):FavoriteMovieDatabase
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