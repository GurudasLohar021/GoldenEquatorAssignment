package com.example.goldenequatorassignment.ui.single_fragment

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.repo.SingleMovieRepo
import com.example.goldenequatorassignment.rest.MovieClient
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.viewmodel.SingleMovieViewModel

class SingleFragment (var movieIndex : Int) : Fragment() {

    private lateinit var singleMovieRepo: SingleMovieRepo

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_single, container, false)

        val apiService : MovieInterface =    MovieClient.getClient()

        singleMovieRepo = SingleMovieRepo(apiService, movieIndex)

        val genreFromAPI : List<Genre> = GenreListDataSource(apiService).fetchGenresList()

        val viewModel = ViewModelProvider(this, GetViewModel(singleMovieRepo))[SingleMovieViewModel::class.java]

        val singleMovieAdapter = SingleMovieAdapter(this, viewModel, genreFromAPI)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        root.findViewById<RecyclerView>(R.id.single_recyclerView).layoutManager = linearLayoutManager
        root.findViewById<RecyclerView>(R.id.single_recyclerView).setHasFixedSize(true)
        root.findViewById<RecyclerView>(R.id.single_recyclerView).adapter = singleMovieAdapter


        viewModel.updatedMovieList.observe(viewLifecycleOwner, Observer {
            singleMovieAdapter.setMovieList(it)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

        })

        viewModel.connectionState.observe(viewLifecycleOwner, Observer {
            root.findViewById<ProgressBar>(R.id.single_Progress).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.COMPLETED) View.VISIBLE else View.GONE

            root.findViewById<TextView>(R.id.single_Error).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.ERROR) View.VISIBLE else View.GONE

            if ( !viewModel.listIsEmpty()){
                singleMovieAdapter.setConnectionState(it)
            }
        })

        viewModel.fetchLiveMovieTypeList()

        return root
    }

    class GetViewModel constructor(private val singleMovieRepo: SingleMovieRepo): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SingleMovieViewModel::class.java)) {
                SingleMovieViewModel(this.singleMovieRepo) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}