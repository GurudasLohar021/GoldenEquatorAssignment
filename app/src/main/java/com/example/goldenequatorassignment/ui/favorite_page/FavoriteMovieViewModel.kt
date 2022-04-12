package com.example.goldenequatorassignment.ui.favorite_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldenequatorassignment.model.local.favorite_movie.FavoriteMovieDetails
import com.example.goldenequatorassignment.model.local.favorite_movie.FavoriteMovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    public val favoriteMovieRepo: FavoriteMovieRepo
): ViewModel() {

    private val _response = MutableLiveData<Long>()
    val response: LiveData<Long> = _response

    fun addFavoriteMovieToDB(favoriteMovieDetails: FavoriteMovieDetails){
        viewModelScope.launch(Dispatchers.IO) {
            _response.postValue(favoriteMovieRepo.addToFavorite(favoriteMovieDetails))
        }
    }

    private val _favoriteDetails = MutableStateFlow<List<FavoriteMovieDetails>>(emptyList())
    val favoriteDetails : StateFlow<List<FavoriteMovieDetails>> = _favoriteDetails

    fun getFavoriteMovieDB(){
        viewModelScope.launch(Dispatchers.IO){
            favoriteMovieRepo.getFavorite()

        }
    }

    fun removeFavoriteMovieDB(id : Int){
        viewModelScope.launch(Dispatchers.IO){
            favoriteMovieRepo.removeFromFavorite(id)
        }
    }

}