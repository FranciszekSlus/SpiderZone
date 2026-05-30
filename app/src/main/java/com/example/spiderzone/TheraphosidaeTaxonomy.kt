package com.example.spiderzone

/** Węzeł drzewa taksonomii na ekranie Home (budowane z terrarium_species.csv). */
data class TaxonomyNode(
    val title: String,
    val subtitle: String? = null,
    val children: List<TaxonomyNode> = emptyList(),
    val detailNote: String? = null
) {
    val isLeaf: Boolean get() = children.isEmpty()
}

/** Gatunek z drzewa taksonomii — do wyszukiwarki w hodowli. */
data class TaxonomySpeciesPick(
    val latinName: String,
    val commonName: String?
)
