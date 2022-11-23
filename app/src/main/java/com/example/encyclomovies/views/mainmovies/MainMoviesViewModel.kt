package com.example.encyclomovies.views.mainmovies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.encyclomovies.models.Movies
import com.example.encyclomovies.remote.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainMoviesViewModel: ViewModel() {
    val moviesMainList = MutableStateFlow(Movies())
    val loading = MutableStateFlow(false)

    fun getMainMovies(){
        loading.value = true
        viewModelScope.launch {
            val response = ApiService.api.getMainMovies()
            if (response.isSuccessful){
                moviesMainList.value = response.body() ?: Movies()
                Log.v("GenresVM", "Todo fenomenal en la petición de géneros ${response.body()}")
            } else {
                Log.v("GenresVM", "Error en la petición de géneros ${response.toString()}")
            }
            loading.value = false
        }
    }
}