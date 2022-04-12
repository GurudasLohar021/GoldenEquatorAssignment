package com.example.goldenequatorassignment.model.remote.top_rated

data class TopRatedMoviesResponse(
    val page: Int,
    val results: List<TopRatedMovies>,
    val total_pages: Int,
    val total_results: Int
)