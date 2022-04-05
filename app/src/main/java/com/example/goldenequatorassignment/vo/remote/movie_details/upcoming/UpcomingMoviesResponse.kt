package com.example.goldenequatorassignment.vo.remote.movie_details.upcoming

data class UpcomingMoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<UpcomingMovies>,
    val total_pages: Int,
    val total_results: Int
)