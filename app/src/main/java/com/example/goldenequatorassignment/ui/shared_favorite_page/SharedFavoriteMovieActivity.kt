package com.example.goldenequatorassignment.ui.shared_favorite_page

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.model.sharedPreference.SharedFavoriteMovieDetails
import com.example.goldenequatorassignment.source.sharedPreference.SharedPreferenceData
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedFavoriteMovieActivity : AppCompatActivity() {

    private lateinit var favoriteMovieArrayListPage : ArrayList<SharedFavoriteMovieDetails>
    private lateinit var sharedPreferenceData: SharedPreferenceData

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_favorite_page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferenceData = SharedPreferenceData()

        favoriteMovieArrayListPage = sharedPreferenceData.retrieveFavoriteMovie(this)

        val linearLayoutManager = LinearLayoutManager(this@SharedFavoriteMovieActivity, LinearLayoutManager.VERTICAL,false)

        val favoriteMovieAdapter = SharedFavoriteMovieAdapter(favoriteMovieArrayListPage, this@SharedFavoriteMovieActivity)
        findViewById<RecyclerView>(R.id.sharedFavoriteActivity_recyclerView).layoutManager = linearLayoutManager
        findViewById<RecyclerView>(R.id.sharedFavoriteActivity_recyclerView).setHasFixedSize(true)
        findViewById<RecyclerView>(R.id.sharedFavoriteActivity_recyclerView).adapter = favoriteMovieAdapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}