package com.example.goldenequatorassignment.ui.search_page

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.api.MovieClient
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsViewModel

class SearchMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchMoviesViewModel
    private lateinit var searchMoviesRepo: SearchMoviesRepo

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seach_movie)


        var querySearch : String = "dune"

        var searchView = findViewById<SearchView>(R.id.searchView)

        val searchMovieAdapter = SearchMovieAdapter(this)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val apiService : MovieInterface = MovieClient.getClient()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                querySearch = query
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
               querySearch = newText
                return true
            }
        })

        searchMoviesRepo = SearchMoviesRepo(apiService,querySearch)

        viewModel = getViewModel(querySearch)

        findViewById<RecyclerView>(R.id.search_recyclerView).layoutManager = linearLayoutManager
        findViewById<RecyclerView>(R.id.search_recyclerView).setHasFixedSize(true)
        findViewById<RecyclerView>(R.id.search_recyclerView).adapter = searchMovieAdapter

        viewModel.searchMoviesList.observe(this, Observer {
            searchMovieAdapter.submitList(it)
        })

        viewModel.connectionState.observe(this, Observer {
            findViewById<ProgressBar>(R.id.search_Progress).visibility =
                if (it == ConnectionState.LOADING) View.VISIBLE else View.GONE
            findViewById<TextView>(R.id.search_Error).visibility =
                if (it == ConnectionState.ERROR) View.VISIBLE else View.GONE

            if ( !viewModel.listIsEmpty()){
                searchMovieAdapter.setConnectionState(it)
            }
        })

    }

    private fun getViewModel(query: String) : SearchMoviesViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchMoviesViewModel(searchMoviesRepo, query) as T
            }
        })[SearchMoviesViewModel::class.java]
    }

}