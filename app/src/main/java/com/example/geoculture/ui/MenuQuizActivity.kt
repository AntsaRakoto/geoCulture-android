package com.example.geoculture.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.geoculture.MainActivity
import com.example.geoculture.R

class MenuQuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_quiz)

        val btnDrapeau = findViewById<CardView>(R.id.drapeau)
        val btnCapitale = findViewById<CardView>(R.id.capitale)
        val clickSound = MediaPlayer.create(this, R.raw.btn_sound)

        btnDrapeau.setOnClickListener {
            val intent = Intent(this@MenuQuizActivity, QuizDrapeauActivity::class.java)
            clickSound.start()
            startActivity(intent)
        }

        btnCapitale.setOnClickListener {
            val intent = Intent(this@MenuQuizActivity, MainActivity::class.java)
            clickSound.start()
            startActivity(intent)
        }

    }
}