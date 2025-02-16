package pl.sebcel.bpg.ui.trivia

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sebcel.bpg.data.local.repositories.TriviaRepository
import javax.inject.Inject

class TriviaViewModel @Inject constructor(private val triviaRepository: TriviaRepository) : ViewModel() {

    fun GetRandomTrivia() : String {
        var trivia = triviaRepository.GetTrivia()
        return trivia.random()
    }
}