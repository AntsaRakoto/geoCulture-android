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
import androidx.appcompat.app.AlertDialog
import com.example.geoculture.api.RetrofitInstance

class QuizDrapeauActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()
    private var quizFinished = false

    private var score : Int = 0


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

        lifecycleScope.launch {
            quizViewModel.isQuizFinished.collectLatest { finished ->
                if (finished) {
                    quizFinished = true
                    etAnswer.visibility = View.GONE
                    btnSubmit.text = "Finir"
                    sonFin.start()
                }
                if (!finished) {
                    quizFinished = false
                    etAnswer.visibility = View.VISIBLE
                    btnSubmit.text = "SUIVANT"
                    tvFeedBack.text = ""
                }

            }
        }

        // 3️⃣ Gestion du clic sur SUIVANT
        btnSubmit.setOnClickListener {
            if (!quizFinished) {
                val answer = etAnswer.text.toString()
                val correct = quizViewModel.checkAnswer(answer)

                if (correct) {
                    tvFeedBack.text = "✅ Correct !"
                    score++
                    sonCorrect.start()
                } else {
                    tvFeedBack.text = "❌ Incorrect ! ${quizViewModel.currentCountry.value?.name}"
                    sonIncorrect.start()
                }

                quizViewModel.nextQuestion()
                etAnswer.text.clear()
            }
            else {
                quizViewModel.onFinishClicked()
            }

        }


        lifecycleScope.launch {
            quizViewModel.showScoreDialog.collectLatest { show ->
                if (show) {
                    showScoreDialog()
                    quizViewModel.resetScoreDialog()
                }
            }
        }


    }
    private fun showScoreDialog() {

        val dialogView = layoutInflater.inflate(R.layout.dialog_score, null)

        val tvScore = dialogView.findViewById<TextView>(R.id.tvDialogScore)
        val btnReplay = dialogView.findViewById<TextView>(R.id.btn_replay)
        val btnBack = dialogView.findViewById<TextView>(R.id.btn_back)

        tvScore.text = "Score : $score/10"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnReplay.setOnClickListener {
            dialog.dismiss()
            score = 0
            quizViewModel.resetQuiz()
            recreate()
        }

        btnBack.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }


}
