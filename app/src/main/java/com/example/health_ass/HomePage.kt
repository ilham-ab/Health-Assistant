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

class HomePage: AppCompatActivity() {

    private lateinit var puser : LinearLayout
    private lateinit var btnlogout : ImageButton

    private lateinit var urgence : LinearLayout
    private lateinit var medecine: LinearLayout


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide();

        puser =findViewById(R.id.personpage)
        btnlogout = findViewById(R.id.logout)

        urgence = findViewById(R.id.Urgence)

        medecine=findViewById(R.id.medecine)

        btnlogout.setOnClickListener { signOut(it) }


        puser.setOnClickListener{
            val intent = Intent(it.context, Profile::class.java)
            it.context.startActivity(intent)
        }

        urgence.setOnClickListener{
            val intent = Intent(this, ContactActivity::class.java)
            it.context.startActivity(intent)
        }

        medecine.setOnClickListener{
            val intent = Intent(this, FetchingMedecineActivity::class.java)
            it.context.startActivity(intent)
        }
    }
    fun signOut(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginPage::class.java))
        finish()
    }
}