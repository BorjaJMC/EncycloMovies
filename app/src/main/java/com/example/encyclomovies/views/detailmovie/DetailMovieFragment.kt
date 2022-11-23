package com.example.encyclomovies.views.detailmovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.encyclomovies.DataHolder
import com.example.encyclomovies.R
import com.example.encyclomovies.adapters.DetailFilmsAdapter
import com.example.encyclomovies.remote.ApiService
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class DetailMovieFragment : Fragment() {

    private val viewModel: DetailMovieViewModel by activityViewModels()
    private lateinit var adapter: DetailFilmsAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_movie, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressDialogDetailMovies)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loading.collect { loading ->
                        if (loading) {
                            progressBar.visibility = View.VISIBLE
                        } else {
                            progressBar.visibility = View.GONE
                        }

                    }
                }

                launch {
                    viewModel.movie.collect {
                        val detailTitle = view?.findViewById<TextView>(R.id.title_detail)
                        detailTitle?.text = viewModel.movie.value.title.toString()
                        val detailImgMovie = view?.findViewById<ImageView>(R.id.image_detail)
                        val urlImage =
                            ApiService.URL_image + viewModel.movie.value.poster_path.toString()
                        Glide.with(requireContext()).load(urlImage).into(detailImgMovie!!)
                        var detailDescription =
                            view?.findViewById<TextView>(R.id.description_detail)
                        detailDescription?.text = viewModel.movie.value.overview.toString()

                        viewModel.getMovies(DataHolder.idGenres.toString())
                    }
                }
                launch {
                    viewModel.moviesList.collect {
                        adapter.updateData(it.results)
                    }
                }

            }

        }

        adapter = DetailFilmsAdapter {
            DataHolder.idMovie = it.id!!
            DataHolder.idGenres = it.genre_ids?.first()!!
            findNavController().navigate(R.id.action_detailMovieFragment2_to_detailMovieSecondFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.detailRecycle)
        recyclerView.adapter = adapter

        viewModel.getMovie(DataHolder.idMovie.toString())

        val btBack = view.findViewById<MaterialToolbar>(R.id.topAppDetailBar)
        btBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}

