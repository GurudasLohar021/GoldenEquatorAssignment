package com.example.goldenequatorassignment.ui.movie_details_page

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.model.local.favorite_movie.FavoriteMovieDetails
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.model.remote.movie_details.MovieDetails
import com.example.goldenequatorassignment.model.remote.movie_details.SpokenLanguage
import com.example.goldenequatorassignment.model.sharedPreference.SharedFavoriteMovieDetails
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.repo.MovieDetailsRepo
import com.example.goldenequatorassignment.rest.IMAGE_BASE_URL
import com.example.goldenequatorassignment.rest.MovieClient
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.source.sharedPreference.SharedPreferenceData
import com.example.goldenequatorassignment.viewmodel.MovieDetailsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.DateFormat
import java.text.SimpleDateFormat


class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movieDetailsRepo: MovieDetailsRepo
    private lateinit var sharedPreferenceData: SharedPreferenceData

    @RequiresApi(Build.VERSION_CODES.N)
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService : MovieInterface = MovieClient.getClient()
        movieDetailsRepo = MovieDetailsRepo(apiService)

        viewModel = getViewModel(movieId)

        sharedPreferenceData = SharedPreferenceData()

       val favoriteMovieArrayList : ArrayList<SharedFavoriteMovieDetails> = SharedPreferenceData().retrieveFavoriteMovie(this)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)

            val favoriteSelected = FavoriteMovieDetails(
                id_movie = it.id,
                poster_path = it.poster_path,
                release_date = it.release_date,
                vote_average = it.vote_average,
                vote_count = it.vote_count,
                tagline = it.tagline,
                title = it.title,
            )


            val sharedFavoriteMovieDetails = SharedFavoriteMovieDetails(
                id_movie = it.id,
                poster_path = it.poster_path,
                release_date = it.release_date,
                vote_average = it.vote_average,
                vote_count = it.vote_count,
                tagline = it.tagline,
                title = it.title,
            )

            var isSelected = false

            Log.i("Favorite List",favoriteMovieArrayList.toString())

            for (i in favoriteMovieArrayList){
                if (it.id == i.id_movie){
                    findViewById<CheckBox>(R.id.movieDetails_favorite).isChecked = true
                    //isSelected = true
                    break
                }
            }

            findViewById<CheckBox>(R.id.movieDetails_favorite).setOnCheckedChangeListener { _, _ ->

                isSelected = !isSelected

                if(isSelected){
                    /*GlobalScope.launch (Dispatchers.IO) {
                        FavoriteMovieDatabase.getInstance(this@MovieDetailsActivity).getFavoriteMovieDao().addToFavoriteMovie(favoriteSelected)
                        Log.i("!!!!!!!!!!!!!!!", favoriteSelected.title)
                    }*/

                            sharedPreferenceData.addFavoriteMovie(this@MovieDetailsActivity,sharedFavoriteMovieDetails)

                            //Toast.makeText(this, "Added to Favorite List", Toast.LENGTH_LONG).show()

                }else{
                    /*GlobalScope.launch (Dispatchers.IO) {
                        FavoriteMovieDatabase.getInstance(this@MovieDetailsActivity).getFavoriteMovieDao().removeFromFavorite(favoriteSelected.id_movie)
                    }*/

                        sharedPreferenceData.removeFavoriteMovie(this@MovieDetailsActivity, sharedFavoriteMovieDetails, it.title)

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

    @SuppressLint("SimpleDateFormat", "CheckResult")
    fun bindUI (movieDetailsRes: MovieDetails){

        val dateMovie : String = movieDetailsRes.release_date
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd/MMM/yyyy")
        val date = inputFormat.parse(dateMovie)
        val outputDate = outputFormat.format(date)


        findViewById<TextView>(R.id.movieDetails_title).text = movieDetailsRes.title
        findViewById<TextView>(R.id.movieDetails_tagline).text = movieDetailsRes.tagline
       findViewById<TextView>(R.id.movieDetails_genre).text =
           movieDetailsRes.genres.joinToString (
                separator = " | ",
           ){ genre: Genre -> genre.name  }
        findViewById<TextView>(R.id.movieDetails_release_date).text = outputDate.toString()
        findViewById<TextView>(R.id.movieDetails_vote_average).text = movieDetailsRes.vote_average.toString()
        findViewById<TextView>(R.id.movieDetails_vote_count).text = movieDetailsRes.vote_count.toString()
        findViewById<TextView>(R.id.movieDetails_language).text = movieDetailsRes.spoken_languages.joinToString (
            separator = " | ",
        ) { spokenLanguage: SpokenLanguage -> spokenLanguage.english_name }
        findViewById<TextView>(R.id.movieDetails_status).text = movieDetailsRes.status
        findViewById<TextView>(R.id.movieDetails_overview).text = movieDetailsRes.overview

        val movieBackDropURL = IMAGE_BASE_URL + movieDetailsRes.backdrop_path
        Glide.with(this)
            .load(movieBackDropURL)
            .into(findViewById(R.id.movieDetails_backdrop));

        val moviePosterURL = IMAGE_BASE_URL + movieDetailsRes.poster_path
        Glide.with(this)
            .load(moviePosterURL)
            .into(findViewById(R.id.movieDetails_poster));
    }


    private fun getViewModel(movieId: Int) : MovieDetailsViewModel {
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