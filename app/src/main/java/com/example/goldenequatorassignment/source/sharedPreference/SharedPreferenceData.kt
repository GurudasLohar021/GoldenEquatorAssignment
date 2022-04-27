package com.example.goldenequatorassignment.source.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.goldenequatorassignment.model.sharedPreference.SharedFavoriteMovieDetails
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferenceData {

    var favoriteMovieArrayList : ArrayList<SharedFavoriteMovieDetails> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var currentFavoriteList : ArrayList<SharedFavoriteMovieDetails>


    fun addFavoriteMovie(context: Context, sharedFavoriteMovieDetails : SharedFavoriteMovieDetails ){

        currentFavoriteList = retrieveFavoriteMovie(context)

        favoriteMovieArrayList.add(sharedFavoriteMovieDetails)

        sharedPreferences = context.getSharedPreferences("Shared Preference",
            AppCompatActivity.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
        val gson = Gson()
        val json : String = gson.toJson(favoriteMovieArrayList)
        editor.putString("SharedFavoriteMovie", json)
        editor.apply()

    }

    fun removeFavoriteMovie(context: MovieDetailsActivity, sharedFavoriteMovieDetails: SharedFavoriteMovieDetails){

        favoriteMovieArrayList.remove(sharedFavoriteMovieDetails)

        sharedPreferences = context.getSharedPreferences("Shared Preference",
            AppCompatActivity.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
        val gson = Gson()
        val json : String = gson.toJson(favoriteMovieArrayList)
        editor.putString("SharedFavoriteMovie", json)
        editor.apply()
    }

    fun retrieveFavoriteMovie(context: Context): ArrayList<SharedFavoriteMovieDetails> {

        sharedPreferences = context.getSharedPreferences("Shared Preference",
            AppCompatActivity.MODE_PRIVATE
        )
        val gson = Gson()
        val json : String? = sharedPreferences.getString("SharedFavoriteMovie", null)
        val type : Type = object: TypeToken<ArrayList<SharedFavoriteMovieDetails>>() {}.getType()
        favoriteMovieArrayList = gson.fromJson(json, type)

        Log.i("FavoriteList", favoriteMovieArrayList.toString())
        return favoriteMovieArrayList
    }
}