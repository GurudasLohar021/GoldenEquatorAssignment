package com.example.goldenequatorassignment.ui.favorite_page

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.source.local.FavoriteMovieDatabase
import com.example.goldenequatorassignment.model.local.favorite_movie.FavoriteMovieDetails
import com.example.goldenequatorassignment.model.sharedPreference.SharedFavoriteMovieDetails
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteMovieActivity : AppCompatActivity() {

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_movie)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lateinit var listFavorite : List<FavoriteMovieDetails>

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        GlobalScope.launch (Dispatchers.IO) {
            listFavorite = FavoriteMovieDatabase.getInstance(this@FavoriteMovieActivity).getFavoriteMovieDao().getFavoriteMovie()

            launch(Dispatchers.Main) {
                val favoriteMovieAdapter = FavoriteMovieAdapter(listFavorite, this@FavoriteMovieActivity)
                findViewById<RecyclerView>(R.id.favoriteActivity_recyclerView).layoutManager = linearLayoutManager
                findViewById<RecyclerView>(R.id.favoriteActivity_recyclerView).setHasFixedSize(true)
                findViewById<RecyclerView>(R.id.favoriteActivity_recyclerView).adapter = favoriteMovieAdapter
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}