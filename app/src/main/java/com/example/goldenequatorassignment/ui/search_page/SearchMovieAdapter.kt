package com.example.goldenequatorassignment.ui.search_page

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsActivity
import com.example.goldenequatorassignment.model.remote.search_movies.SearchMovies
import com.example.goldenequatorassignment.rest.IMAGE_BASE_URL

class SearchMovieAdapter(public var context:SearchMovieActivity)
    : PagedListAdapter<SearchMovies, RecyclerView.ViewHolder>(SearchMovieDiffCallback()) {

    val NOWPLAYING_VIEW_TYPE = 1
    val CONNECTION_VIEW_TYPE = 2

    private var connectionState: ConnectionState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == NOWPLAYING_VIEW_TYPE){
            (holder as SearchMovieItemViewHolder).bind(getItem(position),context)
        }else{
            (holder as ConnectionStateItemViewHolder).bind(connectionState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == NOWPLAYING_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_card_layout,parent,false)
            return SearchMovieItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.connection_status_layout,parent,false)
            return ConnectionStateItemViewHolder(view)
        }

    }


    inner class SearchMovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view){

        fun bind(searchMovies: SearchMovies?, context: SearchMovieActivity){
            itemView.findViewById<TextView>(R.id.movie_title).text = searchMovies?.title
            //itemView.findViewById<TextView>(R.id.movie_genre).text = searchMovies?.genre_ids.toString()
            itemView.findViewById<TextView>(R.id.movie_release_date).text = searchMovies?.release_date
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


    class SearchMovieDiffCallback : DiffUtil.ItemCallback<SearchMovies>(){
        override fun areItemsTheSame(
            oldItem: SearchMovies,
            newItem: SearchMovies
        ): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(
            oldItem: SearchMovies,
            newItem: SearchMovies
        ): Boolean {
            return oldItem == newItem
        }
    }

    private fun hasExtraRow() : Boolean {
        return connectionState != null && connectionState != ConnectionState.COMPLETED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
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
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        }else if (hasExtraRow && previousState != connectionState){
            notifyItemChanged(itemCount - 1)
        }

    }
}