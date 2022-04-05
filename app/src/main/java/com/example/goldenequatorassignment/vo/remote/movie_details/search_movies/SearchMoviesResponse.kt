package com.example.goldenequatorassignment.vo.remote.movie_details.search_movies

data class SearchMoviesResponse(
    val page: Int,
    val results: List<SearchMovies>,
    val total_pages: Int,
    val total_results: Int
)