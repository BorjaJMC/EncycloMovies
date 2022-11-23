package com.example.encyclomovies.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    lateinit var api: Api

    val URL = "https://api.themoviedb.org/3/"
    val URL_image = "https://image.tmdb.org/t/p/w500/"
    val api_key = "9cb0dc93d804b79fa4d51dbf783ef895"
    val language = "es-ES"

    init {
        initServices()
    }

    private fun initServices() {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(Api::class.java)
    }

}