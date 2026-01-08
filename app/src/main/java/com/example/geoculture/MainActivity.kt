package com.example.geoculture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geoculture.ui.MenuActivity
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<TextView>(R.id.btn_start)
        val clickSound = MediaPlayer.create(this, R.raw.btn_sound)

        YoYo.with(Techniques.Pulse)
            .duration(1200)
            .repeat(YoYo.INFINITE)
            .playOn(btnStart)

        btnStart.setOnClickListener {
            YoYo.with(Techniques.Bounce)
                .duration(450)
                .playOn(it)

            clickSound.start()

            val intent = Intent(this@MainActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}