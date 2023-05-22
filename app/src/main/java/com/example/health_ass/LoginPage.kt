package com.example.health_ass

import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginPage : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email :EditText
    private lateinit var pwd :EditText
    private lateinit var btn : Button
    private lateinit var btn1 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        firebaseAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.Email)
        pwd = findViewById(R.id.Password)
        btn = findViewById(R.id.button)
        btn1 = findViewById(R.id.register1)

        btn1.setOnClickListener {
            // Click listener for the "Register" TextView
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }

        btn.setOnClickListener {
            // Click listener for the "Login" Button
            val email = email.text.toString()
            val pass = pwd.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // Perform sign-in with Firebase authentication
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign-in successful, navigate to home page
                        val intent = Intent(this, HomePage::class.java)
                        intent.putExtra("email", email) // Pass the logged-in user's email
                        startActivity(intent)
                    } else {
                        // Sign-in failed, display error message
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Empty fields, display error message
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            // If user is already signed in, navigate to home page
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

}