package pl.sebcel.bpg.data.local.database

class PainDescriptions {
    fun getPainDescription(pain : Int) : String {
        return when (pain) {
            0 -> "Brak bólu"
            1 -> "Ból lekki"
            2 -> "Ból średni"
            3 -> "Ból mocny"
            else -> throw IllegalArgumentException()
        }
    }
}