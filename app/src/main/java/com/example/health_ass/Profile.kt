package com.example.health_ass

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity() {

    private lateinit var returnBtn: ImageButton
    private lateinit var editBtn: ImageButton
    private lateinit var fullName: TextView
    private lateinit var email: TextView
    private lateinit var address: TextView
    private lateinit var dateOfBirth: TextView
    private lateinit var gender: TextView
    private lateinit var weight: TextView
    private lateinit var height: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()
        returnBtn = findViewById(R.id.retour)
        editBtn = findViewById(R.id.edit)
        fullName = findViewById(R.id.fullname)
        email = findViewById(R.id.email)
        address = findViewById(R.id.address)
        dateOfBirth = findViewById(R.id.date2)
        gender = findViewById(R.id.gender)
        weight = findViewById(R.id.weight)
        height = findViewById(R.id.height)

        returnBtn.setOnClickListener{
            startActivity(Intent(this, HomePage::class.java))
        }

        editBtn.setOnClickListener{
            startActivity(Intent(this, Update::class.java))
        }

        // Create a reference to the "users" node in the database
        val database = Firebase.database
        val usersRef = database.getReference("Users")

        // Attach a listener to retrieve data from the "users" node
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val users = mutableListOf<User>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { users.add(it) }
                }

                // Display the first user's data in the TextViews
                users.firstOrNull()?.let { user ->
                    fullName.text = user.name ?: "No user found"
                    email.text = user.email ?: "No user found"
                    address.text = user.address ?: "No user found"
                    dateOfBirth.text = user.date1 ?: "No user found"
                    gender.text = user.gender ?: "No user found"
                    weight.text = user.poids ?: "No user found"
                    height.text = user.taille ?: "No user found"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
}
