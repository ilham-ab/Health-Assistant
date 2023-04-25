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


class RegisterPage : AppCompatActivity() {

    private lateinit var inputfullname : EditText
    private lateinit var inputemail : EditText
    private lateinit var inputpwd : EditText
    private lateinit var inputpwd2 : EditText
    private lateinit var registerbtn : Button
    private lateinit var login1 : TextView



    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        firebaseAuth = FirebaseAuth.getInstance()

        inputfullname =findViewById(R.id.inputfullname)
        inputemail =findViewById(R.id.inputemail)
        inputpwd =findViewById(R.id.inputpwd)
        inputpwd2 =findViewById(R.id.inputpwd1)
        registerbtn =findViewById(R.id.button1)
        login1 =findViewById(R.id.login1)

        login1.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        registerbtn.setOnClickListener {
            val fullName = inputfullname.text.toString()
            val email = inputemail.text.toString()
            val password = inputpwd.text.toString()
            val confirmPassword = inputpwd2.text.toString()

            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, RegisterPage::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
