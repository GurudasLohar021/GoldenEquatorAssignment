package com.example.goldenequatorassignment.ui.search_page

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.bloc.SearchMovieDataSource
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.repo.SearchMovieDataSourceFactory
import com.example.goldenequatorassignment.rest.MovieClient

class SearchMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchMoviesViewModel
    private lateinit var searchMoviesRepo: SearchMoviesRepo

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seach_movie)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var querySearch : String = "Red";

        val searchView = findViewById<SearchView>(R.id.searchView)

        val searchMovieAdapter = SearchMovieAdapter(this)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val apiService : MovieInterface = MovieClient.getClient()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                querySearch = query
                Log.i("query", query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
               querySearch = newText
                Log.i("newText", newText)

                return true
            }
        })


        searchMoviesRepo = SearchMoviesRepo(apiService,querySearch)
        viewModel = getViewModel()

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

    private fun getViewModel() : SearchMoviesViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchMoviesViewModel(searchMoviesRepo) as T
            }
        })[SearchMoviesViewModel::class.java]
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}