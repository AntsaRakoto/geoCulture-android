package com.example.geoculture.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.geoculture.models.QuizViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.geoculture.R
import android.media.MediaPlayer
import com.example.geoculture.api.RetrofitInstance

class QuizDrapeauActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_drapeau)

        val ivFlag = findViewById<ImageView>(R.id.ivFlag)
        val tvQuestionNumber = findViewById<TextView>(R.id.tvQuestionNumber)
        val etAnswer = findViewById<EditText>(R.id.etAnswer)
        val btnSubmit = findViewById<TextView>(R.id.btn_submit)
        val tvFeedBack = findViewById<TextView>(R.id.tvFeedBack)

        val sonFin = MediaPlayer.create(this, R.raw.fin_jeu)
        val sonCorrect = MediaPlayer.create(this, R.raw.correct)
        val sonIncorrect = MediaPlayer.create(this, R.raw.incorrect)

        lifecycleScope.launch {
            try {
                val countries = RetrofitInstance.api.getAllCountries()
                println("Pays récupérés: ${countries.size}")
                countries.take(5).forEach { println(it.name.common) }
            } catch (e: Exception) {
                println("erreur : ")
                e.printStackTrace()
            }
        }


        lifecycleScope.launch {
            quizViewModel.isQuizFinished.collectLatest { finished ->
                if (finished) {
                    tvFeedBack.text = "🎉 Quiz terminé !"
                    btnSubmit.isEnabled = false
                    sonFin.start()
                }
            }
        }


        // 1️⃣ Observer le drapeau
        lifecycleScope.launch {
            quizViewModel.currentCountry.collectLatest { country ->
                country?.let {
                    Glide.with(this@QuizDrapeauActivity)
                        .load(it.flagUrl) // URL depuis API
                        .into(ivFlag)
                }
            }
        }

        // 2️⃣ Observer le numéro de question
        lifecycleScope.launch {
            quizViewModel.questionNumber.collectLatest { text ->
                tvQuestionNumber.text = text
            }
        }

        // 3️⃣ Gestion du clic sur SUIVANT
        btnSubmit.setOnClickListener {
            val answer = etAnswer.text.toString()
            val correct = quizViewModel.checkAnswer(answer)

            if (correct) {
                tvFeedBack.text = "✅ Correct !"
                sonCorrect.start()
            } else {
                tvFeedBack.text = "❌ Incorrect ! ${quizViewModel.currentCountry.value?.name}"
                sonIncorrect.start()
            }

            quizViewModel.nextQuestion()
            etAnswer.text.clear()
        }
    }
}
