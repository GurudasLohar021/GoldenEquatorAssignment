package com.example.goldenequatorassignment.vo.top_rated

data class TopRatedMoviesResponse(
    val page: Int,
    val results: List<TopRatedMovies>,
    val total_pages: Int,
    val total_results: Int
)