package com.example.health_ass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomePage : AppCompatActivity() {

    // Declare the necessary UI elements
    private lateinit var puser: LinearLayout
    private lateinit var btnlogout: ImageButton
    private lateinit var urgence: LinearLayout
    private lateinit var medecine: LinearLayout
    private lateinit var notes :LinearLayout

    // Declare the database reference
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Initialize the database reference
        database = FirebaseDatabase.getInstance().getReference()

        // Retrieve the passed email from the intent
        val email = intent.getStringExtra("email")



        // Bind UI elements to their respective views
        puser = findViewById(R.id.personpage)
        btnlogout = findViewById(R.id.logout)
        urgence = findViewById(R.id.Urgence)
        medecine = findViewById(R.id.medecine)
        notes = findViewById(R.id.Notes)

        // Set an OnClickListener for the "btnlogout" ImageButton to sign out the user
        btnlogout.setOnClickListener { signOut(it) }

        // Set an OnClickListener for the "puser" LinearLayout to navigate to the Profile page
        puser.setOnClickListener {
            val intent = Intent(it.context, Profile::class.java)
            intent.putExtra("email", email)
            it.context.startActivity(intent)
        }

        // Set an OnClickListener for the "urgence" LinearLayout to navigate to the ContactActivity
        urgence.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            it.context.startActivity(intent)
        }

        // Set an OnClickListener for the "medecine" LinearLayout to navigate to the FetchingMedecineActivity
        medecine.setOnClickListener {
            val intent = Intent(this, FetchingMedecineActivity::class.java)
            it.context.startActivity(intent)
        }

        // Set an OnClickListener for the "medecine" LinearLayout to navigate to the FetchingMedecineActivity
        notes.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            it.context.startActivity(intent)
        }


    }

    // Function to sign out the user
    fun signOut(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginPage::class.java))
        finish()
    }
}
