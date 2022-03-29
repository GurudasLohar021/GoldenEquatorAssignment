package com.example.goldenequatorassignment.vm.upcoming

data class UpcomingMoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<UpcomingMovies>,
    val total_pages: Int,
    val total_results: Int
)