package com.example.goldenequatorassignment.ui.home_page.top_rated

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.data.GenreListDataSource
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.repo.TopRatedMoviesRepo
import com.example.goldenequatorassignment.rest.MovieClient
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.viewmodel.TopRatedViewModel


class TopRatedFragment : Fragment() {

    lateinit var topRatedMoviesRepo: TopRatedMoviesRepo

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_top_rated, container, false)

        val apiService : MovieInterface =    MovieClient.getClient()
        topRatedMoviesRepo = TopRatedMoviesRepo(apiService)

        val genreFromAPI : List<Genre> = GenreListDataSource(apiService).fetchGenresList()

        val viewModel = ViewModelProvider(this, GetViewModel(topRatedMoviesRepo))[TopRatedViewModel::class.java]

        val topRatedMoviesAdapter = TopRatedMoviesAdapter(this,viewModel,genreFromAPI)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)


        rootView.findViewById<RecyclerView>(R.id.topRated_recyclerView).layoutManager = linearLayoutManager
        rootView.findViewById<RecyclerView>(R.id.topRated_recyclerView).setHasFixedSize(true)
        rootView.findViewById<RecyclerView>(R.id.topRated_recyclerView).adapter = topRatedMoviesAdapter


        viewModel.topMovieList.observe(viewLifecycleOwner, Observer {
            Log.i("Top Rated", it.toString())
            topRatedMoviesAdapter.setMovieList(it)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

        })

        viewModel.connectionState.observe(viewLifecycleOwner, Observer {
            rootView.findViewById<ProgressBar>(R.id.topRated_Progress).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.COMPLETED) View.VISIBLE else View.GONE

            rootView.findViewById<TextView>(R.id.topRated_Error).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.ERROR) View.VISIBLE else View.GONE

            if ( !viewModel.listIsEmpty()){
                topRatedMoviesAdapter.setConnectionState(it)
            }
        })

        viewModel.fetchLiveTopRatedMoviesList()

        return rootView
    }


    class GetViewModel constructor(private val topRatedMoviesRepo: TopRatedMoviesRepo): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(TopRatedViewModel::class.java)) {
                TopRatedViewModel(this.topRatedMoviesRepo) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

}