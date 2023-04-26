package com.example.health_ass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {
    private lateinit var nameContact : TextView
    private lateinit var teleContact : TextView
    private lateinit var btnSave : Button
    private lateinit var auth: FirebaseAuth

    // private lateinit var sName : String
    //private lateinit var sTele : String

    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_add)

        //auth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("contacts")
        nameContact = findViewById(R.id.nameContact)
        teleContact = findViewById(R.id.teleContact)
        btnSave = findViewById(R.id.save)
        btnSave.setOnClickListener{
            saveContactData()
        }}
    private fun saveContactData(){
        val sName = nameContact.text.toString().trim()
        val sTele = teleContact.text.toString().trim()
        if(sName.isEmpty()){
            nameContact.error = "stp entrer le nom"
        }
        if(sTele.isEmpty()){
            teleContact.error = "stp entrer le nom"
        }
        val contactID = database.push().key!!
        val contact = Contact(contactID,sName,sTele)
        database.child(contactID).setValue(contact)
            .addOnCompleteListener{
                val intent = Intent(this, ContactActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "les données sont enregistrés avec succées",Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                    err->
                Toast.makeText(this, "Erreur ${err.message}",Toast.LENGTH_LONG).show()

            }
        //  val contactID = FirebaseAuth.getInstance().currentUser!!.uid
        // database.child("contacts").child(contactID).setValue(contact)
    }





}