package com.example.health_ass


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ContactActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var contactRecyclerview : RecyclerView
    private lateinit var contactarraylist : ArrayList<Contact>
    private lateinit var addbtn : ImageView
    private lateinit var locbtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_list)
        addbtn = findViewById(R.id.addButton)
        locbtn = findViewById(R.id.MyLoc)
        contactRecyclerview = findViewById(R.id.contactsList)
        contactRecyclerview.layoutManager = LinearLayoutManager(this)
        contactRecyclerview.setHasFixedSize(true)
        contactarraylist = arrayListOf()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        dbref = FirebaseDatabase.getInstance().getReference("contacts").child(userId.orEmpty())
        // val contactId = FirebaseAuth.getInstance().currentUser!!.uid

        //FirebaseDatabase.getInstance().getReference().child("contacts")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (contactSnapshot in snapshot.children){
                        val contact = contactSnapshot.getValue(Contact::class.java)
                        if(!contactarraylist.contains(contact)){
                            contactarraylist.add(contact!!)
                        }}
                    contactRecyclerview.adapter = MyAdapter(contactarraylist)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ContactActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        addbtn.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        locbtn.setOnClickListener{
            val intent = Intent(this, Location::class.java)
            startActivity(intent)
        }


    }}