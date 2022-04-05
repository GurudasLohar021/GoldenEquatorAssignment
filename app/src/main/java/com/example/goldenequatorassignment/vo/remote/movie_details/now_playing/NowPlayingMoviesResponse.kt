package com.example.goldenequatorassignment.vo.remote.movie_details.now_playing

import com.example.goldenequatorassignment.vo.now_playing.NowPlayingMovies

data class NowPlayingMoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<NowPlayingMovies>,
    val total_pages: Int,
    val total_results: Int
)