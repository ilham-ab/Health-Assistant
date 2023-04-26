package com.example.health_ass

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var button1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        supportActionBar?.hide();

        button1 = findViewById(R.id.button1)
        button1.setOnClickListener(View.OnClickListener {
            val intent = Intent(it.context, LoginPage::class.java)
            it.context.startActivity(intent)
        })
    }
}