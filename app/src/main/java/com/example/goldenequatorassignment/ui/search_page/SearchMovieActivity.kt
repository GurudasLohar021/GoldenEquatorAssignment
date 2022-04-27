package com.example.goldenequatorassignment.ui.search_page

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.data.GenreListDataSource
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.repo.SearchMoviesRepo
import com.example.goldenequatorassignment.rest.MovieClient
import com.example.goldenequatorassignment.viewmodel.SearchMoviesViewModel



class SearchMovieActivity : AppCompatActivity() {

    private lateinit var searchMoviesRepo: SearchMoviesRepo

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seach_movie)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val searchView = findViewById<SearchView>(R.id.searchView)
        val closeButtonId : Int = searchView.context.resources.getIdentifier("android:id/search_close_btn", null, null)
        val closeButton = searchView.findViewById<ImageView>(closeButtonId)


        var querySearch : String = "Bat";


        val apiService : MovieInterface =    MovieClient.getClient()

        searchMoviesRepo = SearchMoviesRepo(apiService)

        val genreFromAPI : List<Genre> = GenreListDataSource(apiService).fetchGenresList()

        val viewModel = ViewModelProvider(this, GetViewModel(searchMoviesRepo))[SearchMoviesViewModel::class.java]

        val searchMovieAdapter = SearchMovieAdapter(this,viewModel,genreFromAPI,querySearch)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        findViewById<RecyclerView>(R.id.search_recyclerView).layoutManager = linearLayoutManager
        findViewById<RecyclerView>(R.id.search_recyclerView).setHasFixedSize(true)
        findViewById<RecyclerView>(R.id.search_recyclerView).adapter = searchMovieAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                querySearch = query
                Log.i("query", query)
                viewModel.searchUpdatedList(query)
                searchMovieAdapter.notifyDataSetChanged()
                viewModel.page = 0
                viewModel.currentList.clear()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })



        viewModel.searchMoviesList.observe(this, Observer {
            searchMovieAdapter.setMovieList(it)
        })

        viewModel.errorMessage.observe(this) {

        }

        viewModel.connectionState.observe(this, Observer {
            findViewById<ProgressBar>(R.id.search_Progress).visibility =
                if (it == ConnectionState.LOADING) View.VISIBLE else View.GONE
            findViewById<TextView>(R.id.search_Error).visibility =
                if (it == ConnectionState.ERROR) View.VISIBLE else View.GONE

            if ( !viewModel.listIsEmpty()){
                searchMovieAdapter.setConnectionState(it)
            }
        })

        closeButton.setOnClickListener {
            searchView.setQuery("", false)
            searchView.isIconified
            Toast.makeText(this@SearchMovieActivity, "Search Closed", Toast.LENGTH_LONG).show()
            //viewModel.page = 0
            //viewModel.currentList.clear()
            //searchMovieAdapter.notifyDataSetChanged()
            //viewModel.searchMoviesList.postValue(null)
            //viewModel.searchUpdatedList("a")

        }
    }

/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val  inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu,menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.action_bar_search)

        val searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchItem.collapseActionView()
                Toast.makeText(this@SearchMovieActivity, "Search Here", Toast.LENGTH_LONG).show()
                return true
            }
            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
        return true
    }*/

    class GetViewModel constructor(private val searchMoviesRepo: SearchMoviesRepo): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SearchMoviesViewModel::class.java)) {
                SearchMoviesViewModel(this.searchMoviesRepo) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}