package com.example.encyclomovies.views.secondetailmovie

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
import com.example.encyclomovies.views.secondetailmovie.DetailMovieSecondViewModel
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class DetailMovieSecondFragment : Fragment() {

    private val viewModel: DetailMovieSecondViewModel by activityViewModels()
    private lateinit var adapter: DetailFilmsAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_movie_second, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressDialogDetailSecondMovies)

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
                        val detailTitle = view?.findViewById<TextView>(R.id.title_detailSecond)
                        detailTitle?.text = viewModel.movie.value.title.toString()
                        val detailImgMovie = view?.findViewById<ImageView>(R.id.image_detailSecond)
                        val urlImage =
                            ApiService.URL_image + viewModel.movie.value.poster_path.toString()
                        Glide.with(requireContext()).load(urlImage).into(detailImgMovie!!)
                        var detailDescription =
                            view?.findViewById<TextView>(R.id.description_detailSecond)
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
            findNavController().navigate(R.id.action_detailMovieSecondFragment_to_detailMovieFragment2)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.detailSecondRecycle)
        recyclerView.adapter = adapter

        viewModel.getMovie(DataHolder.idMovie.toString())

        val btBack = view.findViewById<MaterialToolbar>(R.id.topAppDetailSecondBar)
        btBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}