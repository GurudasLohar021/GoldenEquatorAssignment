package com.example.goldenequatorassignment.ui.single_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.model.remote.single_model.MovieModel
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.rest.IMAGE_BASE_URL
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsActivity
import com.example.goldenequatorassignment.viewmodel.SingleMovieViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat

class SingleMovieAdapter(var context: SingleFragment, var viewModel: SingleMovieViewModel, val genreFromAPI : List<Genre>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    var movieModelList = mutableListOf<MovieModel>()

    val MOVIES_VIEW_TYPE = 1
    val CONNECTION_VIEW_TYPE = 2

    private var connectionState: ConnectionState? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setMovieList(movieModels: List<MovieModel>){
        this.movieModelList = movieModels.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)

        var viewHolder: RecyclerView.ViewHolder? = null

        val view: View

        if (viewType == MOVIES_VIEW_TYPE){
            view= layoutInflater.inflate(R.layout.movie_card_layout, parent, false)
            viewHolder = MovieItemViewHolder(view)

        }else{
            view = layoutInflater.inflate(R.layout.connection_status_layout,parent,false)
            viewHolder = ConnectionStateItemViewHolder(view)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movies = movieModelList[position]

        if (getItemViewType(position) == MOVIES_VIEW_TYPE){

            if (position == itemCount-1){
                viewModel.fetchLiveMovieTypeList()
            }
            (holder as MovieItemViewHolder).bind(movies,context)
        }else{
            (holder as ConnectionStateItemViewHolder).bind(connectionState)
        }
    }

    override fun getItemCount(): Int {
        return  movieModelList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount-1){
            CONNECTION_VIEW_TYPE
        }else{
            MOVIES_VIEW_TYPE
        }
    }

    inner class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view){

        @SuppressLint("CheckResult", "SimpleDateFormat")
        fun bind(movieModel: MovieModel?, context: SingleFragment){

            val dateMovie : String = movieModel?.release_date.toString()
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat: DateFormat = SimpleDateFormat("dd/MMM/yyyy")
            val date = inputFormat.parse(dateMovie)
            val outputDate = outputFormat.format(date)

            val genreFromAPI = genreFromAPI

            val genresList: List<Int>? = movieModel?.genre_ids

            movieModel?.genreArrayList = ArrayList()

            if (genresList != null) {
                for(i in genresList){
                    val indexGenre = genreFromAPI.indexOfFirst { genre -> genre.id == i}
                    if (indexGenre != -1){
                        movieModel.genreArrayList.add(genreFromAPI[indexGenre])
                    }
                }
            }


            itemView.findViewById<TextView>(R.id.movie_title).text = movieModel?.title
            itemView.findViewById<TextView>(R.id.movie_genre).text =
                movieModel?.genreArrayList?.joinToString(
                    separator = " | ",
                ) { genre: Genre -> genre.name }
            itemView.findViewById<TextView>(R.id.movie_release_date).text = outputDate.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_average).text = movieModel?.vote_average.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_count).text = movieModel?.vote_count.toString()

            val nowPlayingPosterURL = IMAGE_BASE_URL + movieModel?.poster_path
            Glide.with(itemView.context)
                .load(nowPlayingPosterURL)
                .into(itemView.findViewById(R.id.movie_poster));

            itemView.setOnClickListener {
                val intent = Intent(context.activity, MovieDetailsActivity::class.java)
                intent.putExtra("id",movieModel?.id)
                context.startActivity(intent)
            }
        }
    }

    class ConnectionStateItemViewHolder (view: View) :RecyclerView.ViewHolder(view){

        @SuppressLint("CutPasteId")
        fun bind(connectionState: ConnectionState?){
            if (connectionState != null && connectionState == ConnectionState.LOADING){
                itemView.findViewById<ProgressBar>(R.id.connection_Progress).visibility = View.VISIBLE;

            }else{
                itemView.findViewById<ProgressBar>(R.id.connection_Progress).visibility = View.GONE;
            }
            if (connectionState != null && connectionState == ConnectionState.ERROR){

                itemView.findViewById<TextView>(R.id.connection_Error).visibility = View.VISIBLE;
                itemView.findViewById<TextView>(R.id.connection_Error).text = connectionState.message;
            }else if (connectionState != null && connectionState == ConnectionState.ENDOFLIST) {
                itemView.findViewById<TextView>(R.id.connection_Error).visibility = View.VISIBLE;
                itemView.findViewById<TextView>(R.id.connection_Error).text = connectionState.message;
            }else{
                itemView.findViewById<TextView>(R.id.connection_Error).visibility = View.GONE;
            }
        }
    }

    private fun hasExtraRow() : Boolean {
        return connectionState != null && connectionState != ConnectionState.COMPLETED
    }

    fun setConnectionState (connectionState: ConnectionState){
        val previousState : ConnectionState? = this.connectionState
        val hadExtraRow : Boolean = hasExtraRow()
        this.connectionState = connectionState
        val hasExtraRow : Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(itemCount)
                Log.i("Position", itemCount.toString())
            }else{
                notifyItemInserted(itemCount)
            }
        }else if (hasExtraRow && previousState != connectionState){
            notifyItemChanged(itemCount - 1)
        }

    }
}