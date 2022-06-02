package com.example.goldenequatorassignment.ui.search_page

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsActivity
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMovies
import com.example.goldenequatorassignment.rest.IMAGE_BASE_URL
import com.example.goldenequatorassignment.viewmodel.SearchMoviesViewModel

class SearchMovieAdapter(var context:SearchMovieActivity,var viewModel: SearchMoviesViewModel, val genreFromAPI : List<Genre>, val query: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var searchMovie = mutableListOf<SearchMovies>()

    val NOWPLAYING_VIEW_TYPE = 1
    val CONNECTION_VIEW_TYPE = 2

    private var connectionState: ConnectionState? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setMovieList(searchMovies : List<SearchMovies>){
        this.searchMovie = searchMovies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val movies = searchMovie[position]

        if (getItemViewType(position) == NOWPLAYING_VIEW_TYPE){

            if (position == itemCount-1){
                viewModel.searchUpdatedList(query)
            }

            (holder as SearchMovieItemViewHolder).bind(movies,context)
        }else{
            (holder as ConnectionStateItemViewHolder).bind(connectionState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)

        var viewHolder: RecyclerView.ViewHolder? = null

        val view: View

        if (viewType == NOWPLAYING_VIEW_TYPE){
            view= layoutInflater.inflate(R.layout.movie_card_layout, parent, false)
            viewHolder = SearchMovieItemViewHolder(view)

        }else{
            view = layoutInflater.inflate(R.layout.connection_status_layout,parent,false)
            viewHolder = ConnectionStateItemViewHolder(view)
        }
        return viewHolder

    }


    inner class SearchMovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view){

        @SuppressLint("SimpleDateFormat", "CheckResult")
        fun bind(searchMovies: SearchMovies?, context: SearchMovieActivity){


/*
            val dateMovie : String = searchMovies?.release_date.toString()
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat: DateFormat = SimpleDateFormat("dd/MMM/yyyy")
            val date = inputFormat.parse(dateMovie)
            val outputDate = outputFormat.format(date)
*/

                        val genreFromAPI = genreFromAPI
                        val genresList: List<Int>? = searchMovies?.genre_ids

                        searchMovies?.genreArrayList = ArrayList()

                        if (genresList != null) {
                            for(i in genresList){
                                val indexGenre = genreFromAPI.indexOfFirst { genre -> genre.id == i}
                                if (indexGenre != -1){
                                    searchMovies.genreArrayList.add(genreFromAPI[indexGenre])
                                }
                            }
                        }

            itemView.findViewById<TextView>(R.id.movie_title).text = searchMovies?.title
            itemView.findViewById<TextView>(R.id.movie_genre).text =
                searchMovies?.genreArrayList?.joinToString(
                    separator = " | ",
                ) { genre: Genre -> genre.name }
            itemView.findViewById<TextView>(R.id.movie_release_date).text = searchMovies?.release_date.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_average).text = searchMovies?.vote_average.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_count).text = searchMovies?.vote_count.toString()

            val nowPlayingPosterURL = IMAGE_BASE_URL + searchMovies?.poster_path
            Glide.with(itemView.context)
                .load(nowPlayingPosterURL)
                .into(itemView.findViewById(R.id.movie_poster));

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id",searchMovies?.id)
                context.startActivity(intent)
            }
        }

    }

    private fun hasExtraRow() : Boolean {
        return connectionState != null && connectionState != ConnectionState.COMPLETED
    }

    override fun getItemCount(): Int {
        return searchMovie.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount-1){
            CONNECTION_VIEW_TYPE
        }else{
            NOWPLAYING_VIEW_TYPE
        }
    }

    class ConnectionStateItemViewHolder (view: View) :RecyclerView.ViewHolder(view){

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

    fun setConnectionState (connectionState: ConnectionState){
        val previousState : ConnectionState? = this.connectionState
        val hadExtraRow : Boolean = hasExtraRow()
        this.connectionState = connectionState
        val hasExtraRow : Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(itemCount)
            }else{
                notifyItemInserted(itemCount)
            }
        }else if (hasExtraRow && previousState != connectionState){
            notifyItemChanged(itemCount - 1)
        }

    }
}