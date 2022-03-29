package com.example.goldenequatorassignment.vm.now_playing

data class NowPlayingMoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<NowPlayingMovies>,
    val total_pages: Int,
    val total_results: Int
)