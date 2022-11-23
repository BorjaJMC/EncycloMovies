package com.example.encyclomovies.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.encyclomovies.R
import com.example.encyclomovies.models.Movie
import com.example.encyclomovies.remote.ApiService

class DetailFilmsAdapter (val onClick: (Movie) -> Unit): RecyclerView.Adapter<DetailFilmsAdapter.ViewHolder>() {

    var data = mutableListOf<Movie>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sugerencyfilms, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(movieData: List<Movie>) {
        data = movieData.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageSugerenciesFilms)
        val card = itemView.findViewById<CardView>(R.id.cardSugerenciesFilm)

        fun bind(item: Movie) {

            val urlImage = ApiService.URL_image + item.poster_path
            Glide.with(card).load(urlImage).into(image)

            card.setOnClickListener {
                Log.v("Pulso sobre", item.id.toString())
                onClick(item)
            }
        }
    }
}