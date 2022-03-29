package com.example.goldenequatorassignment.ui.home_page.now_playing

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
import com.example.goldenequatorassignment.R.layout.connection_status_layout
import com.example.goldenequatorassignment.R.layout.movie_card_layout
import com.example.goldenequatorassignment.api.IMAGE_BASE_URL
import com.example.goldenequatorassignment.repo.ConnectionState
import com.example.goldenequatorassignment.ui.home_page.NowPlayingFragment
import com.example.goldenequatorassignment.vm.now_playing.NowPlayingMovies


class NowPlayingPagedListAdapter(public var context: NowPlayingFragment) : PagedListAdapter<NowPlayingMovies, RecyclerView.ViewHolder> (NowPlayingMovieDiffCallback()){

    val NOWPLAYING_VIEW_TYPE = 1
    val CONNECTION_VIEW_TYPE = 2

    private var connectionState: ConnectionState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == NOWPLAYING_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_card_layout,parent,false)
            return NowPlayingItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.connection_status_layout,parent,false)
            return ConnectionStateItemViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == NOWPLAYING_VIEW_TYPE){
            (holder as NowPlayingItemViewHolder).bind(getItem(position),context)
        }else{
            (holder as ConnectionStateItemViewHolder).bind(connectionState)
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

    class NowPlayingMovieDiffCallback : DiffUtil.ItemCallback<NowPlayingMovies>(){
        override fun areItemsTheSame(
            oldItem: NowPlayingMovies,
            newItem: NowPlayingMovies
        ): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NowPlayingMovies,
            newItem: NowPlayingMovies
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class NowPlayingItemViewHolder (view: View) : RecyclerView.ViewHolder(view){

        fun bind(nowPlayingMovies: NowPlayingMovies?, context: NowPlayingFragment){
            itemView.findViewById<TextView>(R.id.movie_title).text = nowPlayingMovies?.title
            //itemView.findViewById<TextView>(R.id.movie_genre).text = nowPlayingMovies?.genre_ids.toString()
            itemView.findViewById<TextView>(R.id.movie_release_date).text = nowPlayingMovies?.release_date
            itemView.findViewById<TextView>(R.id.movie_vote_average).text = nowPlayingMovies?.vote_average.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_count).text = nowPlayingMovies?.vote_count.toString()

            val nowPlayingPosterURL = IMAGE_BASE_URL + nowPlayingMovies?.poster_path
            Glide.with(itemView.context)
                .load(nowPlayingPosterURL)
                .into(itemView.findViewById(R.id.movie_poster));
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