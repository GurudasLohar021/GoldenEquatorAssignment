package com.example.goldenequatorassignment.vm.popular

data class PopularMoviesResponse(
    val page: Int,
    val results: List<PopularMovies>,
    val total_pages: Int,
    val total_results: Int
)