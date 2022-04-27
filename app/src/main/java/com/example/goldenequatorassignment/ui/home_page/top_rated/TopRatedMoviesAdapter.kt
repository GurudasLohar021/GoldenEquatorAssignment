package com.example.goldenequatorassignment.ui.home_page.top_rated

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
import com.example.goldenequatorassignment.model.remote.top_rated.TopRatedMovies
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.rest.IMAGE_BASE_URL
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsActivity
import com.example.goldenequatorassignment.viewmodel.TopRatedViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat

class TopRatedMoviesAdapter(var context:TopRatedFragment,var viewModel: TopRatedViewModel, val genreFromAPI : List<Genre>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var topRatedMovies = mutableListOf<TopRatedMovies>()

    val TOPRATED_VIEW_TYPE = 1
    val CONNECTION_VIEW_TYPE = 2

    private var connectionState: ConnectionState? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setMovieList(topRatedMovies: List<TopRatedMovies>){
        this.topRatedMovies = topRatedMovies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)

        var viewHolder: RecyclerView.ViewHolder? = null

        val view: View

        if (viewType == TOPRATED_VIEW_TYPE){
            view= layoutInflater.inflate(R.layout.movie_card_layout, parent, false)
            viewHolder = TopRatedItemViewHolder(view)

        }else{
            view = layoutInflater.inflate(R.layout.connection_status_layout,parent,false)
            viewHolder =  ConnectionStateItemViewHolder(view)
        }

        return viewHolder



    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)  {

        val movies = topRatedMovies[position]


        if (getItemViewType(position) == TOPRATED_VIEW_TYPE){

                if (position == itemCount-1){
                    viewModel.fetchLiveTopRatedMoviesList()
                }

            (holder as TopRatedItemViewHolder).bind(movies,context)
        }else{
            (holder as ConnectionStateItemViewHolder).bind(connectionState)
        }


    }

    override fun getItemCount(): Int {
        return topRatedMovies.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount-1){
            CONNECTION_VIEW_TYPE
        }else{
            TOPRATED_VIEW_TYPE
        }
    }

   inner class TopRatedItemViewHolder (view: View) : RecyclerView.ViewHolder(view){

        @SuppressLint("CheckResult", "SimpleDateFormat")
        fun bind(topRatedMovies: TopRatedMovies?, context: TopRatedFragment){

            val dateMovie : String = topRatedMovies?.release_date.toString()
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat: DateFormat = SimpleDateFormat("dd/MMM/yyyy")
            val date = inputFormat.parse(dateMovie)
            val outputDate = outputFormat.format(date)


            val genreFromAPI = genreFromAPI

            val genresList: List<Int>? = topRatedMovies?.genre_ids

            topRatedMovies?.genreArrayList = ArrayList()

            if (genresList != null) {
                for(i in genresList){
                    val indexGenre = genreFromAPI.indexOfFirst { genre -> genre.id == i}
                    if (indexGenre != -1){
                        topRatedMovies.genreArrayList.add(genreFromAPI[indexGenre])
                    }
                }
            }


            itemView.findViewById<TextView>(R.id.movie_title).text = topRatedMovies?.title
            itemView.findViewById<TextView>(R.id.movie_genre).text =
                topRatedMovies?.genreArrayList?.joinToString(
                separator = " | ",
            ) { genre: Genre -> genre.name }
            itemView.findViewById<TextView>(R.id.movie_release_date).text = outputDate.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_average).text = topRatedMovies?.vote_average.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_count).text = topRatedMovies?.vote_count.toString()

            val nowPlayingPosterURL = IMAGE_BASE_URL + topRatedMovies?.poster_path
            Glide.with(itemView.context)
                .load(nowPlayingPosterURL)
                .into(itemView.findViewById(R.id.movie_poster));

            itemView.setOnClickListener {
                val intent = Intent(context.activity, MovieDetailsActivity::class.java)
                intent.putExtra("id",topRatedMovies?.id)
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