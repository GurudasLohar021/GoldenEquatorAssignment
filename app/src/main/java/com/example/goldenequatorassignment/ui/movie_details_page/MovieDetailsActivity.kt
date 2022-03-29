package com.example.goldenequatorassignment.ui.movie_details_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
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
import com.example.goldenequatorassignment.vm.movie_details.MovieDetails

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movieDetailsRepo: MovieDetailsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService : MovieInterface = MovieClient.getClient()
        movieDetailsRepo = MovieDetailsRepo(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.connectionState.observe(this, Observer {
            findViewById<ProgressBar>(R.id.movieDetails_Progress).visibility =
                if (it == ConnectionState.LOADING) View.VISIBLE else View.GONE
            findViewById<TextView>(R.id.movieDetails_Error).visibility =
                if (it == ConnectionState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI (it: MovieDetails){
        findViewById<TextView>(R.id.movieDetails_title).text = it.title
        findViewById<TextView>(R.id.movieDetails_tagline).text = it.tagline
        //findViewById<TextView>(R.id.movieDetails_genre).text = it.genres.toString()
        findViewById<TextView>(R.id.movieDetails_release_date).text = it.release_date
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
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailsViewModel(movieDetailsRepo, movieId) as T
            }
        })[MovieDetailsViewModel::class.java]
    }

}