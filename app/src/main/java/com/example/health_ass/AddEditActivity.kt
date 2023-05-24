package com.example.health_ass

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date


class AddEditActivity : AppCompatActivity() {

    private lateinit var noteTitreEdt: EditText
    private lateinit var noteDescriptionEdit: EditText
    private lateinit var btnSaveData: Button

     lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_activity)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        noteTitreEdt = findViewById(R.id.idEdtNoteName)
        noteDescriptionEdit = findViewById(R.id.idEdtNoteDesc)
        btnSaveData = findViewById(R.id.idBtn)
        val noteType =intent.getStringExtra("noteType")
        if(noteType.equals("Edit")){
            val noteTitle =intent.getStringExtra("noteTitre")
            val noteDescription =intent.getStringExtra("noteDesc")
            btnSaveData.setText("Update Note")
            noteTitreEdt.setText(noteTitle)
            noteDescriptionEdit.setText(noteDescription)
        }

       // dbRef = FirebaseDatabase.getInstance().getReference("Notes")

        btnSaveData.setOnClickListener {
            if(noteType.equals("Edit")){
                val noteTitle = noteTitreEdt.text.toString()
                val noteDescription = noteDescriptionEdit.text.toString()
                val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                val currentDateAndTime: String = sdf.format(Date())
                val noteId = intent.getStringExtra("noteId").toString()
                val updatedNote = mapOf("noteId" to noteId,
                                        "noteTitre" to noteTitle,
                                        "noteDescription" to noteDescription,
                                            "noteDate" to currentDateAndTime)
                dbRef = FirebaseDatabase.getInstance().getReference("Notes").child(userId.orEmpty()).child(noteId)
                dbRef.setValue(updatedNote)
                //dbRef.child("Notes").child(noteId).setValue(updatedNote)

            }
            else {
                saveNote()
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }

    private fun saveNote() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Notes").child(userId.orEmpty())

        //getting values
        val noteTitre = noteTitreEdt.text.toString()
        val noteDescription = noteDescriptionEdit.text.toString()

        if (noteTitre.isEmpty()) {
            noteTitreEdt.error = "Please enter name"
        }
        else {
            val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
            val currentDateAndTime: String = sdf.format(Date())


            val noteId = dbRef.push().key!!

            val note = Note(
                noteId,
                noteTitre,
                noteDescription,
                currentDateAndTime
            )

            dbRef.child(noteId).setValue(note)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ", Toast.LENGTH_LONG).show()
                }
        }
    }

}
