package com.example.goldenequatorassignment.ui.favorite_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.api.AppModuleObject
import com.example.goldenequatorassignment.ui.search_page.SearchMoviesViewModel
import com.example.goldenequatorassignment.vm.favorite_movies.FavoriteMovieDao
import com.example.goldenequatorassignment.vm.favorite_movies.FavoriteMovieDatabase
import com.example.goldenequatorassignment.vm.favorite_movies.FavoriteMovieRepo

class FavoriteMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: FavoriteMovieViewModel
    private lateinit var favoriteMovieRepo: FavoriteMovieRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_movie)


        val favoriteMovieAdapter = FavoriteMovieAdapter()

        //val appModule = AppModuleObject.provideFavoriteDao(FavoriteMovieDatabase.)

        //favoriteMovieRepo = FavoriteMovieRepo(appModule)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        findViewById<RecyclerView>(R.id.favoriteActivity_recyclerView).layoutManager = linearLayoutManager
        findViewById<RecyclerView>(R.id.favoriteActivity_recyclerView).setHasFixedSize(true)
        findViewById<RecyclerView>(R.id.favoriteActivity_recyclerView).adapter = favoriteMovieAdapter


        viewModel = getViewModel()

        viewModel.movies.observe(this){
            favoriteMovieAdapter.setMovieList(it)
        }

    }

    private fun getViewModel() : FavoriteMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavoriteMovieViewModel(favoriteMovieRepo) as T
            }
        })[FavoriteMovieViewModel::class.java]
    }
}