package com.example.goldenequatorassignment.ui.movie_details_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.ui.favorite_page.FavoriteMovieRepo
import com.example.goldenequatorassignment.vm.favorite_movies.FavoriteMovieDetails
import com.example.goldenequatorassignment.vm.movie_details.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val movieDetailsRepo: MovieDetailsRepo, private val favoriteMovieRepo: FavoriteMovieRepo,movieId: Int) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieDetailsRepo.fetchMovieDetails(compositeDisposable, movieId)
    }

    val connectionState : LiveData<ConnectionState> by lazy {
        movieDetailsRepo.getMovieDetailsConnectionState()
    }

    fun addFavoriteMovie(movieDetails: MovieDetails){
        CoroutineScope(Dispatchers.IO).launch {
            favoriteMovieRepo.addToFavorite(
                FavoriteMovieDetails(
                    movieDetails.id,
                    movieDetails.poster_path,
                    movieDetails.release_date,
                    movieDetails.vote_average,
                    movieDetails.vote_count,
                    movieDetails.tagline,
                    movieDetails.title,
                )
            )
        }
    }

    suspend fun checkMovie (id: String) = favoriteMovieRepo.checkMovie(id)

    fun removeFromFavoriteMovie (id: String){
        CoroutineScope(Dispatchers.IO).launch {
            favoriteMovieRepo.removeFromFavorite(id)
        }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}