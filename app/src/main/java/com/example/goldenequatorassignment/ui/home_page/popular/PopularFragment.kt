package com.example.goldenequatorassignment.ui.home_page.popular

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
import com.example.goldenequatorassignment.api.MovieClient
import com.example.goldenequatorassignment.api.MovieInterface
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.ui.home_page.now_playing.NowPlayingMoviesPagedListRepo
import com.example.goldenequatorassignment.ui.home_page.now_playing.NowPlayingPagedListAdapter
import com.example.goldenequatorassignment.ui.home_page.now_playing.NowPlayingViewModel

class PopularFragment : Fragment() {

    private lateinit var viewModel: PopularViewModel
    lateinit var popularMoviesPagedListRepo: PopularMoviesPagedListRepo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_popular, container, false)

        val apiService : MovieInterface = MovieClient.getClient()

        popularMoviesPagedListRepo = PopularMoviesPagedListRepo(apiService)

        viewModel = getViewModel()

       val popularPagedListAdapter = PopularPagedListAdapter(this)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        rootView.findViewById<RecyclerView>(R.id.popular_recyclerView).layoutManager = linearLayoutManager
        rootView.findViewById<RecyclerView>(R.id.popular_recyclerView).setHasFixedSize(true)
        rootView.findViewById<RecyclerView>(R.id.popular_recyclerView).adapter = popularPagedListAdapter


        viewModel.popularMoviePagedList.observe(viewLifecycleOwner, Observer {
            popularPagedListAdapter.submitList(it)
        })

        viewModel.connectionState.observe(viewLifecycleOwner, Observer {
            rootView.findViewById<ProgressBar>(R.id.popular_Progress).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.COMPLETED) View.VISIBLE else View.GONE
            rootView.findViewById<TextView>(R.id.popular_Error).visibility =
                if (viewModel.listIsEmpty() && it == ConnectionState.ERROR) View.VISIBLE else View.GONE

            if ( !viewModel.listIsEmpty()){
                popularPagedListAdapter.setConnectionState(it)
            }
        })

        return rootView;
    }

    private fun getViewModel() : PopularViewModel{
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PopularViewModel(popularMoviesPagedListRepo) as T
            }
        })[PopularViewModel::class.java]
    }

}