package com.example.health_ass

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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
            openUpdateDialog()
        }
        val email1 = intent.getStringExtra("email")
        // Create a reference to the "users" node in the database
        val database = Firebase.database
        val usersRef = database.getReference("Users").orderByChild("email").equalTo(email1)


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
                if (users.isNotEmpty()) {
                    // Display the first user's data in the TextViews
                    val user = users[0]
                    fullName.text = user.name ?: "No name found"
                    email.text = user.email ?: "No email found"
                    address.text = user.address ?: "No address found"
                    dateOfBirth.text = user.date1 ?: "No date found"
                    gender.text = user.gender ?: "No gender found"
                    weight.text = user.poids ?: "No weight found"
                    height.text = user.taille ?: "No height found"
                } else {
                    fullName.text = "Your full name"
                    email.text = "Your address email"
                    address.text = "Your address"
                    dateOfBirth.text = "Your date of birth"
                    gender.text = "Gender"
                    weight.text = "Weight"
                    height.text = "Height"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
    @SuppressLint("MissingInflatedId")
    private fun openUpdateDialog() {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_user, null)

        mDialog.setView(mDialogView)

        val user_name = mDialogView.findViewById<EditText>(R.id.update_name)
        val user_email = mDialogView.findViewById<EditText>(R.id.update_email)
        val user_address = mDialogView.findViewById<EditText>(R.id.update_address)
        val user_date = mDialogView.findViewById<EditText>(R.id.update_date)
        val user_gender = mDialogView.findViewById<EditText>(R.id.update_gender)
        val user_poids = mDialogView.findViewById<EditText>(R.id.update_weight)
        val user_taille = mDialogView.findViewById<EditText>(R.id.update_height)
        val save = mDialogView.findViewById<Button>(R.id.update_button)

        user_name.setText(fullName.text.toString())
        user_email.setText(email.text.toString())
        user_address.setText(address.text.toString())
        user_date.setText(dateOfBirth.text.toString())
        user_gender.setText(gender.text.toString())
        user_poids.setText(weight.text.toString())
        user_taille.setText(height.text.toString())

        mDialog.setTitle("Updating user")
        val alertDialog = mDialog.create()
        alertDialog.show()

        save.setOnClickListener {
            updateMedData(
                user_name.text.toString(),
                user_email.text.toString(),
                user_address.text.toString(),
                user_date.text.toString(),
                user_gender.text.toString(),
                user_poids.text.toString(),
                user_taille.text.toString(),
            )

            Toast.makeText(applicationContext, "Users Data Updated", Toast.LENGTH_LONG).show()

            // Update the displayed information with the new values
            fullName.text = user_name.text.toString()
            email.text = user_email.text.toString()
            address.text = user_address.text.toString()
            dateOfBirth.text = user_date.text.toString()
            gender.text = user_gender.text.toString()
            weight.text = user_poids.text.toString()
            height.text = user_taille.text.toString()

            alertDialog.dismiss()
        }
    }
    private fun updateMedData(
        name: String,
        email: String,
        address: String,
        date1: String,
        gender: String,
        poids: String,
        taille: String
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")
        val userRef = usersRef.child(userId!!)

        val userData = HashMap<String, Any>()
        userData["name"] = name
        userData["email"] = email
        userData["address"] = address
        userData["date1"] = date1
        userData["gender"] = gender
        userData["poids"] = poids
        userData["taille"] = taille

        userRef.updateChildren(userData)
    }


}
