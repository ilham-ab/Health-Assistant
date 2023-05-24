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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterPage : AppCompatActivity() {

    // Déclarez les éléments d'interface utilisateur nécessaires
    private lateinit var inputfullname: EditText
    private lateinit var inputemail: EditText
    private lateinit var inputpwd: EditText
    private lateinit var inputpwd2: EditText
    private lateinit var registerbtn: Button
    private lateinit var login1: TextView

    // Déclarez une instance de FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    // Déclarez la référence à FirebaseDatabase
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        // Initialisez l'instance de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialisez l'instance de FirebaseDatabase et la référence "Users"
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("Users")

        // Associez les éléments d'interface utilisateur à leurs vues respectives
        inputfullname = findViewById(R.id.inputfullname)
        inputemail = findViewById(R.id.inputemail)
        inputpwd = findViewById(R.id.inputpwd)
        inputpwd2 = findViewById(R.id.inputpwd1)
        registerbtn = findViewById(R.id.button1)
        login1 = findViewById(R.id.login1)

        // Définissez un OnClickListener pour le TextView "login1" pour naviguer vers la page LoginPage
        login1.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        // Définissez un OnClickListener pour le bouton "registerbtn" afin d'enregistrer un nouvel utilisateur
        registerbtn.setOnClickListener {
            val fullName = inputfullname.text.toString()
            val email = inputemail.text.toString()
            val password = inputpwd.text.toString()
            val confirmPassword = inputpwd2.text.toString()

            // Vérifiez que tous les champs d'entrée ne sont pas vides
            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                // Vérifiez que le mot de passe correspond au mot de passe de confirmation
                if (password == confirmPassword) {
                    // Créez un nouvel utilisateur avec l'e-mail et le mot de passe fournis
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                val userId = user?.uid

                                // Créez un nouvel objet User avec les informations de l'utilisateur
                                val newUser = User(userId, fullName, email)

                                // Enregistrez le nouvel utilisateur dans la base de données en utilisant l'ID de l'utilisateur comme clé
                                usersRef.child(userId!!).setValue(newUser)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            // Inscription et enregistrement des données réussis, déconnectez l'utilisateur
                                            firebaseAuth.signOut()

                                            // Naviguez vers la page LoginPage
                                            val intent = Intent(this, LoginPage::class.java)
                                            startActivity(intent)
                                            finish() // Optionnel : fermez la page RegisterPage pour éviter de revenir en arrière
                                        } else {
                                            // Une erreur s'est produite lors de l'enregistrement des données de l'utilisateur, affichez un message d'erreur
                                            Toast.makeText(
                                                this,
                                                "Échec de l'enregistrement des données de l'utilisateur.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                // L'inscription a échoué, affichez le message d'erreur
                                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    // Les champs vides ne sont pas autorisés, affichez un message d'erreur
                    Toast.makeText(this, "Les champs vides ne sont pas autorisés !!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
