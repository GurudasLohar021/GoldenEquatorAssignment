package com.example.goldenequatorassignment.model.remote.now_playing

import com.example.goldenequatorassignment.model.now_playing.NowPlayingMovies

data class NowPlayingMoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<NowPlayingMovies>,
    val total_pages: Int,
    val total_results: Int
)