package com.example.goldenequatorassignment.model.remote.single_model

import com.example.goldenequatorassignment.model.remote.now_playing.Dates

data class MovieModelResponse(
    val dates: Dates,
    val page: Int,
    val results: List<MovieModel>,
    val total_pages: Int,
    val total_results: Int
)
