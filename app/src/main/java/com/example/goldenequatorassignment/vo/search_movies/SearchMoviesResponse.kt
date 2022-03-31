package com.example.goldenequatorassignment.vo.search_movies

data class SearchMoviesResponse(
    val page: Int,
    val results: List<SearchMovies>,
    val total_pages: Int,
    val total_results: Int
)