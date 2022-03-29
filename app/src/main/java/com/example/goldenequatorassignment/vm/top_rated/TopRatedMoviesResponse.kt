package com.example.goldenequatorassignment.vm.top_rated

data class TopRatedMoviesResponse(
    val page: Int,
    val results: List<TopRatedMovies>,
    val total_pages: Int,
    val total_results: Int
)