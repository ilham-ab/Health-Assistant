package com.example.health_ass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterPage : AppCompatActivity() {

    // Declare the necessary UI elements
    private lateinit var inputfullname: EditText
    private lateinit var inputemail: EditText
    private lateinit var inputpwd: EditText
    private lateinit var inputpwd2: EditText
    private lateinit var registerbtn: Button
    private lateinit var login1: TextView

    // Declare an instance of FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    // Declare the FirebaseDatabase reference
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        // Initialize the FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize the FirebaseDatabase instance and the "Users" reference
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("Users")

        // Bind UI elements to their respective views
        inputfullname = findViewById(R.id.inputfullname)
        inputemail = findViewById(R.id.inputemail)
        inputpwd = findViewById(R.id.inputpwd)
        inputpwd2 = findViewById(R.id.inputpwd1)
        registerbtn = findViewById(R.id.button1)
        login1 = findViewById(R.id.login1)

        // Set an OnClickListener for the "login1" TextView to navigate to the LoginPage
        login1.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        // Set an OnClickListener for the "registerbtn" Button to register a new user
        registerbtn.setOnClickListener {
            val fullName = inputfullname.text.toString()
            val email = inputemail.text.toString()
            val password = inputpwd.text.toString()
            val confirmPassword = inputpwd2.text.toString()

            // Check if all input fields are not empty
            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                // Check if the password matches the confirm password
                if (password == confirmPassword) {
                    // Create a new user with the provided email and password
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val userId = user?.uid

                            // Create a new User object with the user's information
                            val newUser = User(userId, fullName, email)

                            // Save the new user in the database using the userId as the key
                            usersRef.child(userId!!).setValue(newUser)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        // Registration and data save successful, navigate to the LoginPage
                                        val intent = Intent(this, LoginPage::class.java)
                                        startActivity(intent)
                                    } else {
                                        // Error occurred while saving user data, display an error message
                                        Toast.makeText(this, "Failed to save user data.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            // Registration failed, display the error message
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Password and confirm password do not match, display an error message
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Empty fields are not allowed, display an error message
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
