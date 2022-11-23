package com.example.encyclomovies.views.mainmovies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.encyclomovies.DataHolder
import com.example.encyclomovies.R
import com.example.encyclomovies.adapters.MovieMainAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainMoviesFragment : Fragment() {

    private val viewModel: MainMoviesViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MovieMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_movies, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressDialogMainMovies)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.exit) {
                exit()
            }
            true
        }

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
                    viewModel.moviesMainList.collect {
                        adapter.updateData(it.results)
                    }
                }
            }
        }
        adapter = MovieMainAdapter {
            DataHolder.idMovie = it.id!!
            DataHolder.idGenres = it.genre_ids?.first()!!
            findNavController().navigate(R.id.action_mainMoviesFragment_to_detailMovieFragment)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.moviesRecycle)
        recyclerView.adapter = adapter


        viewModel.getMainMovies()
    }

    fun exit() {
        Firebase.auth.signOut()
        findNavController().navigate(R.id.action_mainMoviesFragment_to_loginFragment)
    }

}