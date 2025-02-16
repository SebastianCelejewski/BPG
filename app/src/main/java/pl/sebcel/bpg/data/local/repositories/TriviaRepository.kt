package pl.sebcel.bpg.data.local.repositories

import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R
import javax.inject.Inject

class TriviaRepository @Inject constructor() {
    fun GetTrivia() : List<String> {
        return BpgApplication.instance.resources.getStringArray(R.array.trivia).toList()
    }
}