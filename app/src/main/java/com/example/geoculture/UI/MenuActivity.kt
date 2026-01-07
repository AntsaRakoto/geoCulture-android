package com.example.geoculture.UI

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.geoculture.R
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.geoculture.MainActivity


class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnQuiz = findViewById<TextView>(R.id.btn_quiz)
        val btnApprendre = findViewById<TextView>(R.id.btn_apprendre)
        val clickSound = MediaPlayer.create(this, R.raw.btn_sound)

        YoYo.with(Techniques.Pulse)
            .duration(1200)
            .repeat(YoYo.INFINITE)
            .playOn(btnQuiz)

        btnQuiz.setOnClickListener {
            YoYo.with(Techniques.Bounce)
                .duration(450)
                .playOn(it)

            clickSound.start()

            val intent = Intent(this@MenuActivity, MenuQuizActivity::class.java)
            startActivity(intent)
        }

        btnApprendre.setOnClickListener {
            YoYo.with(Techniques.Bounce)
                .duration(450)
                .playOn(it)


            clickSound.start()
        }
    }
}