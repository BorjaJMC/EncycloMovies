package com.example.encyclomovies.remote

import com.example.encyclomovies.models.Movie
import com.example.encyclomovies.models.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("movie/popular")
    suspend fun getMainMovies(
        @Query("api_key") apikey: String = ApiService.api_key,
        @Query("language") language: String = ApiService.language
    ): Response<Movies>

    @GET("movie/{id}")
    suspend fun getMovie(
        @Path("id") movie_id: String,
        @Query("api_key") apikey: String = ApiService.api_key,
        @Query("language") language: String = ApiService.language,
    ): Response<Movie>

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apikey: String = ApiService.api_key,
        @Query("language") language: String = ApiService.language,
        @Query("with_genres") genres: String
    ): Response<Movies>
}