package com.example.goldenequatorassignment.ui.home_page.popular

import android.annotation.SuppressLint
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
import com.example.goldenequatorassignment.state.ConnectionState
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsActivity
import com.example.goldenequatorassignment.model.local.genres.Genre
import com.example.goldenequatorassignment.model.remote.popular.PopularMovies
import com.example.goldenequatorassignment.rest.IMAGE_BASE_URL

class PopularPagedListAdapter( var context: PopularFragment,val genreFromAPI : List<Genre>)
    : PagedListAdapter<PopularMovies, RecyclerView.ViewHolder>(PopularPagedListAdapter.PopularMovieDiffCallback()){

    val POPULAR_VIEW_TYPE = 1
    val CONNECTION_VIEW_TYPE = 2

    private var connectionState: ConnectionState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == POPULAR_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_card_layout,parent,false)
            return PopularItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.connection_status_layout,parent,false)
            return ConnectionStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == POPULAR_VIEW_TYPE){
            (holder as PopularItemViewHolder).bind(getItem(position),context)
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
            POPULAR_VIEW_TYPE
        }
    }


    class PopularMovieDiffCallback : DiffUtil.ItemCallback<PopularMovies>(){
        override fun areItemsTheSame(
            oldItem: PopularMovies,
            newItem: PopularMovies
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PopularMovies,
            newItem: PopularMovies
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class PopularItemViewHolder (view: View) : RecyclerView.ViewHolder(view){

        @SuppressLint("CheckResult", "SimpleDateFormat")
        fun bind(popularMovies: PopularMovies?, context: PopularFragment){

                        val genreFromAPI = genreFromAPI
                        val genresList: List<Int>? = popularMovies?.genre_ids
                        popularMovies?.genreArrayList = ArrayList()

                        if (genresList != null) {
                            for(i in genresList){
                                val indexGenre = genreFromAPI.indexOfFirst { genre -> genre.id == i}
                                if (indexGenre != -1){
                                    popularMovies.genreArrayList.add(genreFromAPI[indexGenre])
                                }
                            }
                        }


            itemView.findViewById<TextView>(R.id.movie_title).text = popularMovies?.title
            itemView.findViewById<TextView>(R.id.movie_genre).text =
                popularMovies?.genreArrayList?.joinToString(
                    separator = " | ",
                ) { genre: Genre -> genre.name }
            itemView.findViewById<TextView>(R.id.movie_release_date).text = popularMovies?.release_date
            itemView.findViewById<TextView>(R.id.movie_vote_average).text = popularMovies?.vote_average.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_count).text = popularMovies?.vote_count.toString()

            val popularPosterURL = IMAGE_BASE_URL + popularMovies?.poster_path
            Glide.with(itemView.context)
                .load(popularPosterURL)
                .into(itemView.findViewById(R.id.movie_poster));

            itemView.setOnClickListener {
                val intent = Intent(context.activity, MovieDetailsActivity::class.java)
                intent.putExtra("id",popularMovies?.id)
                context.startActivity(intent)
            }
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