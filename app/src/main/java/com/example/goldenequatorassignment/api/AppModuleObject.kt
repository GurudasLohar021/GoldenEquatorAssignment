package com.example.goldenequatorassignment.api

import android.content.Context
import androidx.room.Room
import com.example.goldenequatorassignment.vm.favorite_movies.FavoriteMovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
object AppModuleObject {

    @Provides
    @Singleton
    fun provideFavoriteMovieDatabase(
        @ApplicationContext app:Context
    ) = Room.databaseBuilder(
        app,
        FavoriteMovieDatabase::class.java,
    "movie_database"
    ).build()

    @Provides
    @Singleton
    fun provideFavoriteDao(db:FavoriteMovieDatabase) = db.getFavoriteMovieDao()

}