package com.example.goldenequatorassignment.ui.favorite_page

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.goldenequatorassignment.vm.favorite_movies.FavoriteMovieRepo

class FavoriteMovieViewModel @ViewModelInject constructor(
    private val repository : FavoriteMovieRepo
): ViewModel() {
    val movies = repository.getFavoriteMovies()
}