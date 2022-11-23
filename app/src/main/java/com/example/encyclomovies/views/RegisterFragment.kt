package com.example.encyclomovies.views

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.encyclomovies.R
import com.example.encyclomovies.models.User
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val btnRegister = view.findViewById<Button>(R.id.btRegister)

        val email = view.findViewById<TextInputEditText>(R.id.emailInRegister)
        val password = view.findViewById<TextInputEditText>(R.id.passwordInRegister)
        val name = view.findViewById<TextInputEditText>(R.id.nameInRegister)

        btnRegister.setOnClickListener {
            if (name.text.isNullOrEmpty() || password.text.isNullOrEmpty() ||email.text.isNullOrEmpty()) {
                name.error = getString(R.string.error_textField)
                password.error = getString(R.string.error_textField)
                email.error = getString(R.string.error_textField)
            } else {
                if (email.text!!.contains('@')) {
                    val miUser = User()
                    miUser.name = name.text.toString()
                    createAccount(email.text.toString(), password.text.toString(), miUser)
                    findNavController().navigate(R.id.action_registerFragment_to_mainMoviesFragment)
                } else {
                    email.error = getString(R.string.error_password)
                }
            }
        }

        val btBack = view.findViewById<MaterialToolbar>(R.id.topAppRegisterBar)
        btBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun createAccount(email: String, password: String, miUser: User) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    registerUserInFirestore(miUser, auth.currentUser!!.uid)

                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Error en la creación de la cuenta de usuario",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    fun registerUserInFirestore(user: User, UID: String) {
        db.collection("users").document(UID).set(user)
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_registerFragment_to_mainMoviesFragment)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al crear el usuario en Firestore", Toast.LENGTH_SHORT).show()
            }
    }
}