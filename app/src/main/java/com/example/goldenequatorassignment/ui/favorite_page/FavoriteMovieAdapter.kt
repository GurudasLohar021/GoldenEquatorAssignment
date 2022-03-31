package com.example.goldenequatorassignment.ui.favorite_page

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.api.IMAGE_BASE_URL
import com.example.goldenequatorassignment.vo.favorite_movies.FavoriteMovieDetails

class FavoriteMovieAdapter : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {

    private lateinit var list : List<FavoriteMovieDetails>

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setMovieList(list: List<FavoriteMovieDetails>){
        this.list = list
        notifyDataSetChanged()
    }

    inner class FavoriteMovieViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(favoriteMovieDetails: FavoriteMovieDetails, context: FavoriteMovieActivity) {
            itemView.findViewById<TextView>(R.id.movie_title).text = favoriteMovieDetails?.title
            //itemView.findViewById<TextView>(R.id.movie_genre).text = nowPlayingMovies?.genre_ids.toString()
            itemView.findViewById<TextView>(R.id.movie_release_date).text = favoriteMovieDetails?.release_date
            itemView.findViewById<TextView>(R.id.movie_vote_average).text = favoriteMovieDetails?.vote_average.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_count).text = favoriteMovieDetails?.vote_count.toString()

            val nowPlayingPosterURL = IMAGE_BASE_URL + favoriteMovieDetails?.poster_path
            Glide.with(itemView.context)
                .load(nowPlayingPosterURL)
                .into(itemView.findViewById(R.id.movie_poster));

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {

        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)

        val view: View = layoutInflater.inflate(R.layout.movie_card_layout,parent,false)
            return FavoriteMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        Log.e("FavoriteMovieAdapter", "Favorite Movie Holder")
        holder.bind(list[position],FavoriteMovieActivity())
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback {
        fun onItemClick(favoriteMovieDetails: FavoriteMovieDetails)
    }
}