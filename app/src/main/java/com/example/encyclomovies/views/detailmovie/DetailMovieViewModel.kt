package com.example.encyclomovies.views.detailmovie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.encyclomovies.models.Movie
import com.example.encyclomovies.models.Movies
import com.example.encyclomovies.remote.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailMovieViewModel: ViewModel() {
    val movie = MutableStateFlow(Movie())
    val moviesList = MutableStateFlow(Movies())
    val loading = MutableStateFlow(false)

    fun getMovie(id: String) {
        loading.value = true
        viewModelScope.launch {
            val response = ApiService.api.getMovie(id)
            if (response.isSuccessful) {
                movie.value = response.body() ?: Movie()
                Log.v("GenresVM", "Todo fenomenal en la petición ${response}")
            } else {
                Log.v("GenresVM", "Error en la petición de generos ${response.toString()}")
            }

            loading.value = false
        }
    }

    fun getMovies(id: String) {
        loading.value = true
        viewModelScope.launch {
            val response = ApiService.api.getMovies(genres = id)
            if (response.isSuccessful) {
                moviesList.value = response.body() ?: Movies()
                Log.v("GenresVM", "Todo fenomenal en la petición de listado peliculas ${response.body()}")
            } else {
                Log.v("GenresVM", "Error en la petición de generos ${response.toString()}")
            }

            loading.value = false
        }
    }
}

