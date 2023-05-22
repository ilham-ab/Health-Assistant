package com.example.note_app_firebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(){

    private lateinit var notesRV: RecyclerView
    lateinit var addFAB : FloatingActionButton
    private lateinit var tvLoadingData: TextView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notesRV = findViewById(R.id.idRVnotes)
        addFAB = findViewById(R.id.idFABAddNote)

        notesRV.layoutManager = LinearLayoutManager(this)



       noteList = arrayListOf<Note>()

        getEmployeesData()
        addFAB.setOnClickListener {
            val intent = Intent(this@MainActivity,AddEditActivity::class.java)
            startActivity(intent)
            this.finish()
        }

    }

    private fun getEmployeesData() {

        notesRV.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("Notes")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(Note::class.java)
                        noteList.add(empData!!)
                    }
                    val mAdapter = NoteRVAdapter(noteList)
                    notesRV.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : NoteRVAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@MainActivity, AddEditActivity::class.java)

                            //put extras
                            intent.putExtra("noteType", "Edit")
                            intent.putExtra("noteId", noteList[position].noteId)
                            intent.putExtra("noteTitre", noteList[position].noteTitre)
                            intent.putExtra("noteDesc", noteList[position].noteDescription)
                            intent.putExtra("noteDate", noteList[position].noteDate)
                            startActivity(intent)
                        }

                    })

                    notesRV.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}