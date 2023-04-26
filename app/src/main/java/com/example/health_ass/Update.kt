package com.example.health_ass

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Update: AppCompatActivity() {

    private lateinit var user_name : EditText
    private lateinit var user_email : EditText
    private lateinit var user_address : EditText
    private lateinit var user_date : EditText
    private lateinit var user_gender : EditText
    private lateinit var user_poids : EditText
    private lateinit var user_taille : EditText
    private lateinit var save : Button

    private lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.insert_profile)
    supportActionBar?.hide()

        user_name=findViewById(R.id.addname)
        user_email=findViewById(R.id.addemail)
        user_address=findViewById(R.id.addaddress)
        user_date=findViewById(R.id.adddate)
        user_gender=findViewById(R.id.addgender)
        user_poids=findViewById(R.id.addpoids)
        user_taille=findViewById(R.id.addtaille)
        save=findViewById(R.id.button2)

        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        save.setOnClickListener {
            saveUserData()
        }



    }
    private fun saveUserData() {
        val name = user_name.text.toString()
        val email = user_email.text.toString()
        val address = user_address.text.toString()
        val date1 = user_date.text.toString()
        val gender = user_gender.text.toString()
        val poids = user_poids.text.toString()
        val taille = user_taille.text.toString()

        Log.d("UserData", "Name: $name")
        Log.d("UserData", "Email: $email")
        Log.d("UserData", "Address: $address")
        Log.d("UserData", "Date of birth: $date1")
        Log.d("UserData", "Gender: $gender")
        Log.d("UserData", "Weight: $poids")
        Log.d("UserData", "Height: $taille")

        if (name.isEmpty()) {
            user_name.error = "please enter your full name"
        }
        if (email.isEmpty()) {
            user_email.error = "please enter your email"
        }
        if (address.isEmpty()) {
            user_address.error = "please enter your address"
        }
        if (date1.isEmpty()) {
            user_date.error = "please enter your  date of birth"
        }
        if (gender.isEmpty()) {
            user_gender.error = "please enter your gender"
        }
        if (poids.isEmpty()) {
            user_poids.error = "please enter your weight"
        }
        if (taille.isEmpty()) {
            user_taille.error = "please enter your height"
        }

        val userId = dbRef.push().key!!
        val user = User(userId, name, email, address, date1, gender, poids, taille)
        Log.d("UserData", "User object created: $user")

        dbRef.child(userId).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
        user_name.text.clear()
        user_email.text.clear()
        user_address.text.clear()
        user_date.text.clear()
        user_gender.text.clear()
        user_poids.text.clear()
        user_taille.text.clear()
    }
}

