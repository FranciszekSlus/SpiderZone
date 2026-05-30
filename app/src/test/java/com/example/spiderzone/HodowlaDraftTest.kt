package com.example.spiderzone

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class HodowlaDraftTest {
    private val sampleAnimal = Animal(
        id = "a1",
        name = "Molly",
        speciesLatinName = "Brachypelma hamorii",
        speciesCommonName = "Mexican Red Knee",
        speciesSource = "firestore_species",
        stage = "L5",
        notes = "Spokojna",
        isPublic = true
    )

    @Test
    fun editKeepsSpeciesWhenSearchUnchanged() {
        val draft = buildAnimalDraftFromForm(
            HodowlaFormState(
                editingAnimal = sampleAnimal,
                pseudonym = "Molly II",
                wyborGatunku = null,
                trybWlasnyGatunek = false,
                customLatin = "",
                customCommon = "",
                stage = "L6",
                notes = "Spokojna",
                ownedSince = "",
                isPublic = true
            )
        )
        assertNotNull(draft)
        assertEquals("Molly II", draft!!.name)
        assertEquals("L6", draft.stage)
        assertEquals("Brachypelma hamorii", draft.speciesLatinName)
    }

    @Test
    fun customEditFallsBackToStoredLatinName() {
        val custom = sampleAnimal.copy(
            speciesSource = "custom",
            speciesLatinName = "",
            speciesLabel = "Poecilotheria metallica"
        )
        val draft = buildAnimalDraftFromForm(
            HodowlaFormState(
                editingAnimal = custom,
                pseudonym = "Meta",
                wyborGatunku = null,
                trybWlasnyGatunek = true,
                customLatin = "",
                customCommon = "",
                stage = "L4",
                notes = "",
                ownedSince = "",
                isPublic = false
            )
        )
        assertNotNull(draft)
        assertEquals("Poecilotheria metallica", draft!!.speciesLatinName)
    }

    @Test
    fun newAnimalWithoutSpeciesReturnsNull() {
        val draft = buildAnimalDraftFromForm(
            HodowlaFormState(
                editingAnimal = null,
                pseudonym = "Nowy",
                wyborGatunku = null,
                trybWlasnyGatunek = false,
                customLatin = "",
                customCommon = "",
                stage = "L1",
                notes = "",
                ownedSince = "",
                isPublic = false
            )
        )
        assertNull(draft)
    }
}
