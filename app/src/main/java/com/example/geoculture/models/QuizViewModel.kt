package com.example.geoculture.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoculture.data.Country
import com.example.geoculture.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val repository = QuizRepository()

    // 1️⃣ Flow pour la question courante
    private val _currentCountry = MutableStateFlow<Country?>(null)
    val currentCountry: StateFlow<Country?> = _currentCountry

    private val _questionNumber = MutableStateFlow("Question 1/10")
    val questionNumber: StateFlow<String> = _questionNumber

    private var countries: List<Country> = emptyList()
    private var currentIndex = 0

    init {
        loadCountries()
    }

    private fun loadCountries() {
        viewModelScope.launch {
            try {
                countries = repository.get10RandomCountries()
                loadQuestion()
            } catch (e: Exception) {
                e.printStackTrace()
                // On peut mettre une question fake pour ne pas crasher
                countries = listOf(
                    Country("France", "Paris", "https://node01.flagstat.net/media/catalog/product/detail/11477.png")
                )
                loadQuestion()
            }
        }
    }

    private fun loadQuestion() {
        if (countries.isNotEmpty() && currentIndex < countries.size) {
            _currentCountry.value = countries[currentIndex]
            _questionNumber.value = "Question ${currentIndex + 1}/${countries.size}"
        }
    }

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished

    fun nextQuestion() {
        if (currentIndex < countries.size - 1) {
            currentIndex++
            loadQuestion()
        } else {
            // Fin du quiz
            _isQuizFinished.value = true
        }
    }

    fun checkAnswer(answer: String): Boolean {
        return answer.trim().equals(countries[currentIndex].name, ignoreCase = true)
    }
}
