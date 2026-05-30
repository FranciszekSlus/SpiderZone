package com.example.spiderzone

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object SeedData {
    /**
     * Uzupełnia Firestore tylko gdy kolekcja species jest pusta (reguły muszą zezwalać na zapis).
     * Główna baza gatunków jest w assets/terrarium_species.csv — ładowana przez [TerrariumSpeciesCatalog].
     */
    suspend fun seedSpeciesIfEmpty() {
        val db = Firebase.firestore
        val speciesCol = db.collection("species")
        if (!speciesCol.limit(1).get().await().isEmpty) return

        val seed = listOf(
            mapOf(
                "latinName" to "Grammostola pulchra",
                "commonName" to "Brazilian Black",
                "category" to "tarantula",
                "occurrence" to "Brazylia, Urugwaj, Paragwaj",
                "lifeMode" to "Nocny, fosoryczny",
                "temperatureDay" to "22-24°C",
                "temperatureNight" to "20-22°C",
                "humidity" to "60-70%",
                "sizeMale" to "~6 cm",
                "sizeFemale" to "~14-16 cm",
                "temperament" to "Spokojny, rzadko kopie",
                "venom" to "Łagodny (ptasznik)",
                "difficulty" to "beginner",
                "cites" to "nie",
                "diet" to "Świerszcze, karaki, zuky 1×/tydz.",
                "documentedBreeding" to "Częste w hodowlach; linienie samicy.",
                "lifespan" to "20+ lat",
                "careNotes" to "Stabilna wilgotność i spokojne karmienie.",
                "funFact" to "Znany z bardzo ciemnego umaszczenia.",
                "imageUrl" to ""
            ),
            mapOf(
                "latinName" to "Pogona vitticeps",
                "commonName" to "Agama brodata",
                "category" to "reptile",
                "occurrence" to "Australia (pustynie i półpustynie)",
                "lifeMode" to "Dzienny",
                "temperatureDay" to "38-42°C (strefa grzewcza)",
                "temperatureNight" to "18-22°C",
                "humidity" to "30-40%",
                "sizeMale" to "~45-55 cm",
                "sizeFemale" to "~40-50 cm",
                "temperament" to "Spokojny, towarzyski",
                "venom" to "Brak (gad)",
                "difficulty" to "beginner",
                "cites" to "nie",
                "diet" to "Owady, warzywa, rzadko gady; suplementacja Ca+D3",
                "documentedBreeding" to "Powszechne w hodowlach.",
                "lifespan" to "8-12 lat",
                "careNotes" to "Wymaga UVB i strefy wygrzewania.",
                "funFact" to "Zmienia odcień brody podczas stresu.",
                "imageUrl" to ""
            )
        )

        try {
            seed.forEach { speciesCol.add(it).await() }
        } catch (error: FirebaseFirestoreException) {
            if (error.code != FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                throw error
            }
            // Writes to species are blocked by rules — katalog z CSV i tak działa offline w aplikacji.
        }
    }
}
