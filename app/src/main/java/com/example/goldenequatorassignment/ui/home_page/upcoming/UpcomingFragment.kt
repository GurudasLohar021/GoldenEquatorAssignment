package com.example.goldenequatorassignment.ui.home_page.upcoming

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.data.GenreListDataSource
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.repo.UpcomingMoviesRepo
import com.example.goldenequatorassignment.rest.MovieClient
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.viewmodel.UpcomingViewModel

class UpcomingFragment : Fragment() {

    private lateinit var upcomingMoviesRepo: UpcomingMoviesRepo

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =   inflater.inflate(R.layout.fragment_upcoming, container, false)

        val apiService : MovieInterface =    MovieClient.getClient()

        upcomingMoviesRepo = UpcomingMoviesRepo(apiService)

        val genreFromAPI : List<Genre> = GenreListDataSource(apiService).fetchGenresList()

        val viewModel = ViewModelProvider(this, GetViewModel(upcomingMoviesRepo))[UpcomingViewModel::class.java]

        val upcomingMoviesAdapter = UpcomingMoviesAdapter(this,viewModel, genreFromAPI)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        rootView.findViewById<RecyclerView>(R.id.upcoming_recyclerView).layoutManager = linearLayoutManager
        rootView.findViewById<RecyclerView>(R.id.upcoming_recyclerView).setHasFixedSize(true)
        rootView.findViewById<RecyclerView>(R.id.upcoming_recyclerView).adapter = upcomingMoviesAdapter

        viewModel.upcomingMovieList.observe(viewLifecycleOwner, Observer {
            Log.i("Upcoming", it.toString())
            upcomingMoviesAdapter.setMovieList(it)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

        })

        viewModel.connectionState.observe(viewLifecycleOwner, Observer {
            rootView.findViewById<ProgressBar>(R.id.upcoming_Progress).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.COMPLETED) View.VISIBLE else View.GONE

            rootView.findViewById<TextView>(R.id.upcoming_Error).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.ERROR) View.VISIBLE else View.GONE

            if ( !viewModel.listIsEmpty()){
                upcomingMoviesAdapter.setConnectionState(it)
            }
        })

        viewModel.fetchLiveUpcomingMoviesList()

        return rootView
    }

    class GetViewModel constructor(private val upcomingMoviesRepo: UpcomingMoviesRepo): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
                UpcomingViewModel(this.upcomingMoviesRepo) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

}