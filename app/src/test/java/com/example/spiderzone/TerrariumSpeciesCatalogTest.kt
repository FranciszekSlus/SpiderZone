package com.example.spiderzone

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TerrariumSpeciesCatalogTest {
  @Test
  fun parseCsv_readsSemicolonSeparatedRows() {
    val lines = listOf(
      "Nazwa naukowa;Nazwa polska;Gromada;Rząd;Podrząd;Rodzina",
      "Brachypelma hamorii;Ptasznik meksykański;Pajęczaki (Arachnida);Pająki (Araneae);Mygalomorphae;Ptaszniki (Theraphosidae)",
      "Python regius;Pyton królewski;Gady (Reptilia);Łuskonośne (Squamata);Węże (Serpentes);Pythonidae"
    )
    val species = TerrariumSpeciesCatalog.parseCsv(lines)
    assertEquals(2, species.size)
    assertEquals("Brachypelma hamorii", species[0].latinName)
    assertEquals("Ptasznik meksykański", species[0].commonName)
    assertEquals("tarantula", species[0].category)
    assertEquals("reptile", species[1].category)
  }

  @Test
  fun latinToStableId_normalizesDocumentKey() {
    assertEquals("brachypelma_hamorii", TerrariumSpeciesCatalog.latinToStableId("Brachypelma hamorii"))
  }

    @Test
    fun buildTaxonomyTree_groupsPhylumOrderFamilyAndSpecies() {
        val lines = listOf(
            "Nazwa naukowa;Nazwa polska;Gromada;Rząd;Podrząd;Rodzina",
            "Brachypelma hamorii;Ptasznik;Pajęczaki (Arachnida);Pająki (Araneae);Mygalomorphae;Ptaszniki (Theraphosidae)",
            "Python regius;Pyton;Gady (Reptilia);Łuskonośne (Squamata);Węże (Serpentes);Pythonidae"
        )
        val species = TerrariumSpeciesCatalog.parseCsv(lines)
        val tree = TerrariumSpeciesCatalog.buildTaxonomyTree(species)
        assertEquals("Terrarium", tree.title)
        assertEquals(2, tree.children.size)
        val spiders = tree.children.first { it.title.contains("Pajęczaki") }
        val suborder = spiders.children
            .flatMap { it.children }
            .first { it.title.contains("Mygalomorphae") }
        val family = suborder.children.first { it.title.contains("Theraphosidae") }
        assertEquals(1, family.children.size)
        assertEquals("Brachypelma hamorii", family.children.first().title)
    }

    @Test
    fun mergeSpeciesLists_prefersFirestoreCareData() {
    val catalog = listOf(
      Species(
        id = "brachypelma_hamorii",
        latinName = "Brachypelma hamorii",
        commonName = "Ptasznik",
        category = "tarantula",
        careNotes = "Gromada: Pajęczaki"
      )
    )
    val firestore = listOf(
      Species(
        id = "doc1",
        latinName = "Brachypelma hamorii",
        commonName = "Mexican Red Knee",
        category = "tarantula",
        difficulty = "beginner",
        humidity = "60-70%"
      )
    )
    val merged = mergeSpeciesLists(catalog, firestore)
    assertEquals(1, merged.size)
    assertEquals("beginner", merged[0].difficulty)
    assertEquals("Mexican Red Knee", merged[0].commonName)
    assertTrue(merged[0].careNotes.contains("Gromada"))
  }
}
