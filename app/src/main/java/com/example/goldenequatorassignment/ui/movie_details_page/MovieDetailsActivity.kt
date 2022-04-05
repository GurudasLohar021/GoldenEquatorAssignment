package com.example.goldenequatorassignment.ui.movie_details_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.api.IMAGE_BASE_URL
import com.example.goldenequatorassignment.api.MovieClient
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.ui.favorite_page.FavoriteMovieViewModel
import com.example.goldenequatorassignment.vo.local.favorite_movie.FavoriteMovieDatabase
import com.example.goldenequatorassignment.vo.local.favorite_movie.FavoriteMovieDetails
import com.example.goldenequatorassignment.vo.remote.movie_details.MovieDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat


class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movieDetailsRepo: MovieDetailsRepo
    private  val favoriteViewModel : FavoriteMovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService : MovieInterface = MovieClient.getClient()
        movieDetailsRepo = MovieDetailsRepo(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
            Log.i("@@@@@@@@@@@@@@@", it.title)

            val favoriteSelected = FavoriteMovieDetails(
                id_movie = it.id,
                poster_path = it.poster_path,
                release_date = it.release_date,
                vote_average = it.vote_average,
                vote_count = it.vote_count,
                tagline = it.tagline,
                title = it.title,
            )

            Log.i("$$$$$$$$$$$", favoriteSelected.toString())

            var  isSelected = false
            findViewById<CheckBox>(R.id.movieDetails_favorite).setOnCheckedChangeListener { _, _ ->
                isSelected = !isSelected

                if(isSelected){
                    GlobalScope.launch (Dispatchers.IO) {
                        FavoriteMovieDatabase.getInstance(this@MovieDetailsActivity).getFavoriteMovieDao().addToFavoriteMovie(favoriteSelected)
                        Log.i("!!!!!!!!!!!!!!!", favoriteSelected.title)
                    }
                    Toast.makeText(this, "Added to Favorite List", Toast.LENGTH_LONG).show()
                }else{
                    GlobalScope.launch (Dispatchers.IO) {
                        FavoriteMovieDatabase.getInstance(this@MovieDetailsActivity).getFavoriteMovieDao().removeFromFavorite(favoriteSelected.id_movie)
                    }
                    Toast.makeText(this, "Removed from Favorite List", Toast.LENGTH_LONG).show()
                }
                findViewById<CheckBox>(R.id.movieDetails_favorite).isChecked = isSelected
            }
        })

        viewModel.connectionState.observe(this, Observer {
            findViewById<ProgressBar>(R.id.movieDetails_Progress).visibility =
                if (it == ConnectionState.LOADING) View.VISIBLE else View.GONE
            findViewById<TextView>(R.id.movieDetails_Error).visibility =
                if (it == ConnectionState.ERROR) View.VISIBLE else View.GONE
        })



    }

    @SuppressLint("SimpleDateFormat")
    fun bindUI (it: MovieDetails){

        val dateMovie : String = it.release_date
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd/MMM/yyyy")
        val date = inputFormat.parse(dateMovie)
        val outputDate = outputFormat.format(date)

        findViewById<TextView>(R.id.movieDetails_title).text = it.title
        findViewById<TextView>(R.id.movieDetails_tagline).text = it.tagline
        //findViewById<TextView>(R.id.movieDetails_genre).text = it.genres.toString()
        findViewById<TextView>(R.id.movieDetails_release_date).text = outputDate.toString()
        findViewById<TextView>(R.id.movieDetails_vote_average).text = it.vote_average.toString()
        findViewById<TextView>(R.id.movieDetails_vote_count).text = it.vote_count.toString()
        //findViewById<TextView>(R.id.movieDetails_language).text = it.spoken_languages.toString()
        findViewById<TextView>(R.id.movieDetails_status).text = it.status
        findViewById<TextView>(R.id.movieDetails_overview).text = it.overview

        val movieBackDropURL = IMAGE_BASE_URL + it.backdrop_path
        Glide.with(this)
            .load(movieBackDropURL)
            .into(findViewById(R.id.movieDetails_backdrop));

        val moviePosterURL = IMAGE_BASE_URL + it.poster_path
        Glide.with(this)
            .load(moviePosterURL)
            .into(findViewById(R.id.movieDetails_poster));
    }


    private fun getViewModel(movieId: Int) : MovieDetailsViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieDetailsViewModel(movieDetailsRepo,movieId) as T
            }
        })[MovieDetailsViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}