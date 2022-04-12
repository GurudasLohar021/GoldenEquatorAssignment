package com.example.goldenequatorassignment.ui.favorite_page

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsActivity
import com.example.goldenequatorassignment.model.local.favorite_movie.FavoriteMovieDetails
import com.example.goldenequatorassignment.rest.IMAGE_BASE_URL

class FavoriteMovieAdapter(private val listFavorite: List<FavoriteMovieDetails>, private val context: FavoriteMovieActivity) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        val view: View = layoutInflater.inflate(R.layout.movie_card_layout, parent, false)
        return FavoriteMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        Log.e("FavoriteMovieAdapter", "Favorite Movie Holder")
        holder.bind(listFavorite[position], context)
    }

    class FavoriteMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(favoriteMovieDetails: FavoriteMovieDetails, context: FavoriteMovieActivity) {
            itemView.findViewById<TextView>(R.id.movie_title).text = favoriteMovieDetails.title
            //itemView.findViewById<TextView>(R.id.movie_genre).text = favoriteMovieDetails.genre_ids.toString()
            itemView.findViewById<TextView>(R.id.movie_release_date).text =
                favoriteMovieDetails.release_date
            itemView.findViewById<TextView>(R.id.movie_vote_average).text =
                favoriteMovieDetails.vote_average.toString()
            itemView.findViewById<TextView>(R.id.movie_vote_count).text =
                favoriteMovieDetails.vote_count.toString()

            val nowPlayingPosterURL = IMAGE_BASE_URL + favoriteMovieDetails.poster_path
            Glide.with(itemView.context)
                .load(nowPlayingPosterURL)
                .into(itemView.findViewById(R.id.movie_poster));

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", favoriteMovieDetails.id_movie)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listFavorite.size
}