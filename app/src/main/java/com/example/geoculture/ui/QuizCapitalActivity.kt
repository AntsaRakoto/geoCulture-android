package com.example.geoculture.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.geoculture.R
import com.example.geoculture.models.QuizViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class QuizCapitalActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()
    private var quizFinished = false

    private var score: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_capital)

        val ivCapital = findViewById<TextView>(R.id.ivCapital)
        val tvQuestionNumber = findViewById<TextView>(R.id.tvQuestionNumber)
        val etAnswer = findViewById<EditText>(R.id.etAnswer)
        val btnSubmit = findViewById<TextView>(R.id.btn_submit)
        val tvFeedBack = findViewById<TextView>(R.id.tvFeedBack)

        val sonFin = MediaPlayer.create(this, R.raw.fin_jeu)
        val sonCorrect = MediaPlayer.create(this, R.raw.correct)
        val sonIncorrect = MediaPlayer.create(this, R.raw.incorrect)


        // Country
        lifecycleScope.launch {
            quizViewModel.currentCountry.collectLatest { country ->
                country?.let {
                    ivCapital.text = it.name
                }
            }
        }

        // Question number
        lifecycleScope.launch {
            quizViewModel.questionNumber.collectLatest {
                tvQuestionNumber.text = it
            }
        }

        // Fin du quiz
        lifecycleScope.launch {
            quizViewModel.isQuizFinished.collectLatest { finished ->
                quizFinished = finished
                if (finished) {
                    etAnswer.visibility = View.GONE
                    btnSubmit.text = "Finir"
                    sonFin.start()
                } else {
                    etAnswer.visibility = View.VISIBLE
                    btnSubmit.text = "SUIVANT"
                    tvFeedBack.text = ""
                }
            }
        }

        btnSubmit.setOnClickListener {
            if (!quizFinished) {
                val answer = etAnswer.text.toString()
                val correct = quizViewModel.checkAnswerCapital(answer)

                if (correct) {
                    tvFeedBack.text = "✅ Correct !"
                    score++
                    sonCorrect.start()
                } else {
                    tvFeedBack.text = "❌ Incorrect ! ${quizViewModel.currentCountry.value?.capital}"
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