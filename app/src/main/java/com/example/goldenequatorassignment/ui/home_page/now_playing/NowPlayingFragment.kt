package com.example.goldenequatorassignment.ui.home_page

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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.data.GenreListDataSource
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.source.api.MovieInterface
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.rest.MovieClient
import com.example.goldenequatorassignment.repo.NowPlayingMoviesPagedListRepo
import com.example.goldenequatorassignment.ui.home_page.now_playing.NowPlayingPagedListAdapter
import com.example.goldenequatorassignment.viewmodel.NowPlayingViewModel

class NowPlayingFragment : Fragment() {

    private lateinit var viewModel: NowPlayingViewModel
    lateinit var nowPlayingMoviesPagedListRepo: NowPlayingMoviesPagedListRepo


    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_now_playing, container, false)

        val apiService : MovieInterface =    MovieClient.getClient()

        val genreFromAPI : List<Genre> = GenreListDataSource(apiService).fetchGenresList()

        nowPlayingMoviesPagedListRepo = NowPlayingMoviesPagedListRepo(apiService)

        viewModel = getViewModel()

        val nowPlayingPagedListAdapter = NowPlayingPagedListAdapter(this,genreFromAPI)

        val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        rootView.findViewById<RecyclerView>(R.id.nowPlaying_recyclerView).layoutManager = linearLayoutManager
        rootView.findViewById<RecyclerView>(R.id.nowPlaying_recyclerView).setHasFixedSize(true)
        rootView.findViewById<RecyclerView>(R.id.nowPlaying_recyclerView).adapter = nowPlayingPagedListAdapter

       viewModel.nowPlayingMoviePagedList.observe(viewLifecycleOwner, Observer {
           nowPlayingPagedListAdapter.submitList(it)
       })

        viewModel.connectionState.observe(viewLifecycleOwner, Observer {
            rootView.findViewById<ProgressBar>(R.id.nowPlaying_Progress).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.COMPLETED) View.VISIBLE else View.GONE

            rootView.findViewById<TextView>(R.id.nowPlaying_Error).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.ERROR) View.VISIBLE else View.GONE

            if ( !viewModel.listIsEmpty()){
                nowPlayingPagedListAdapter.setConnectionState(it)
            }
        })

        return  rootView;
    }

    private fun getViewModel() : NowPlayingViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NowPlayingViewModel(nowPlayingMoviesPagedListRepo) as T
            }
        })[NowPlayingViewModel::class.java]
    }
}