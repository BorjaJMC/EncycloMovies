package com.example.encyclomovies.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.encyclomovies.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val btnRemember = view.findViewById<Button>(R.id.rememberPassword)
        val btnLogin = view.findViewById<Button>(R.id.login)
        val btnRegister = view.findViewById<Button>(R.id.register)

        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        val email = view.findViewById<TextInputEditText>(R.id.emailInLogin)
        val password = view.findViewById<TextInputEditText>(R.id.passwordInLogin)

        btnLogin.setOnClickListener {
            if (email.text.isNullOrEmpty() || password.text.isNullOrEmpty()) {
                email.error = getString(R.string.error_textField)
                password.error = getString(R.string.error_textField)
            } else {
                if (email.text!!.contains('@')) {
                    signAccount(email.text.toString(), password.text.toString())
                } else {
                    email.error = getString(R.string.error_password)
                }
            }
        }

        btnRemember.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_passwordResetFragment)
        }

    }

    fun signAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "signInWithEmail:success")
                    val user = auth.currentUser
                    findNavController().navigate(R.id.action_loginFragment_to_mainMoviesFragment)
                } else {
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "No existe el usuario indicado.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}