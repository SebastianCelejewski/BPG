package pl.sebcel.bpg.data.local.database.model

import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R

class PainDescriptions {
    fun getPainDescription(pain : Int) : String {
        return when (pain) {
            0 -> BpgApplication.instance.getString(R.string.pain_description_0)
            1 -> BpgApplication.instance.getString(R.string.pain_description_1)
            2 -> BpgApplication.instance.getString(R.string.pain_description_2)
            3 -> BpgApplication.instance.getString(R.string.pain_description_3)
            else -> throw IllegalArgumentException("There is no pain description for pain value $pain")
        }
    }
}