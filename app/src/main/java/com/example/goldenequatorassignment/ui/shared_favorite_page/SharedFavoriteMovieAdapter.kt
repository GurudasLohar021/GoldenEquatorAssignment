package com.example.goldenequatorassignment.ui.shared_favorite_page

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.model.sharedPreference.SharedFavoriteMovieDetails
import com.example.goldenequatorassignment.rest.IMAGE_BASE_URL
import com.example.goldenequatorassignment.ui.movie_details_page.MovieDetailsActivity

class SharedFavoriteMovieAdapter
    (private val listSharedFavorite: List<SharedFavoriteMovieDetails>, private val context: SharedFavoriteMovieActivity)
    : RecyclerView.Adapter<SharedFavoriteMovieAdapter.SharedFavoriteMovieViewHolder>(){

    class SharedFavoriteMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(favoriteMovieDetails: SharedFavoriteMovieDetails, context: SharedFavoriteMovieActivity) {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SharedFavoriteMovieViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.movie_card_layout, parent, false)
        return SharedFavoriteMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SharedFavoriteMovieViewHolder, position: Int) {

        holder.bind(listSharedFavorite[position], context)
    }

    override fun getItemCount(): Int = listSharedFavorite.size
}