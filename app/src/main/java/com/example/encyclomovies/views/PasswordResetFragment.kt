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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordResetFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val textReset = view.findViewById<TextInputEditText>(R.id.emailInPasswordReset)
        val btReset = view.findViewById<Button>(R.id.btPasswordReset)
        val btBack = view.findViewById<MaterialToolbar>(R.id.topAppPasswordResetBar)

        btReset.setOnClickListener {
            if ( textReset.text.isNullOrEmpty()) {
                textReset.error = getString(R.string.error_textField)
            } else {
                resetPassword(textReset.text.toString())
                Toast.makeText(context, "Se ha enviado un email, revise el spam", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        btBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    fun resetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.v("ResetPassword", "Email sent.")
                }
            }
    }
}