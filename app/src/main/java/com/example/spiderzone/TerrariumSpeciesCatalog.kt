package com.example.spiderzone

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Katalog gatunków terrariowych z pliku CSV w assets (terrarium_species.csv).
 * Separator: średnik. Nagłówek w pierwszym wierszu.
 */
internal object TerrariumSpeciesCatalog {
    private const val ASSET_FILE = "terrarium_species.csv"

    fun load(context: Context): List<Species> {
        context.assets.open(ASSET_FILE).use { input ->
            BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8)).use { reader ->
                return parseCsv(reader.readLines())
            }
        }
    }

    internal fun parseCsv(lines: List<String>): List<Species> {
        if (lines.isEmpty()) return emptyList()
        val header = lines.first().split(';').map { it.trim() }
        val hasCareColumns = header.any { it.equals("Występowanie", ignoreCase = true) }
        val dataLines = lines.drop(1).map { it.trim() }.filter { it.isNotEmpty() }
        val result = mutableListOf<Species>()
        val seenLatin = mutableSetOf<String>()

        for (line in dataLines) {
            val row = (if (hasCareColumns) {
                parseSemicolonLineWithHeader(line, header)
            } else {
                parseSemicolonLine(line)
            }) ?: continue
            val latin = row.latinName.trim()
            if (latin.isEmpty()) continue
            val key = latin.lowercase()
            if (!seenLatin.add(key)) continue

            result.add(
                Species(
                    id = latinToStableId(latin),
                    latinName = latin,
                    commonName = row.polishName.trim(),
                    category = inferCategory(row.phylum, row.family),
                    phylum = row.phylum.trim(),
                    taxonomicClass = row.taxonomicClass.trim(),
                    suborder = row.suborder.trim(),
                    family = row.family.trim(),
                    occurrence = row.occurrence,
                    lifeMode = row.lifeMode,
                    temperatureDay = row.temperatureDay,
                    temperatureNight = row.temperatureNight,
                    humidity = row.humidity,
                    sizeMale = row.sizeMale,
                    sizeFemale = row.sizeFemale,
                    temperament = row.temperament,
                    venom = row.venom,
                    difficulty = row.difficulty,
                    cites = row.cites,
                    diet = row.diet,
                    documentedBreeding = row.documentedBreeding,
                    careNotes = "",
                    funFact = "",
                    imageUrl = ""
                )
            )
        }
        return result.sortedBy { it.latinName.lowercase() }
    }

    private fun parseSemicolonLine(line: String): CsvSpeciesRow? {
        val parts = line.split(';')
        if (parts.size < 6) return null
        return CsvSpeciesRow(
            latinName = parts[0],
            polishName = parts[1],
            phylum = parts[2],
            taxonomicClass = parts[3],
            suborder = parts[4],
            family = parts[5]
        )
    }

    private fun parseSemicolonLineWithHeader(line: String, header: List<String>): CsvSpeciesRow? {
        val parts = line.split(';')
        if (parts.size < 6) return null
        fun col(vararg names: String): String {
            val index = header.indexOfFirst { h -> names.any { n -> h.equals(n, ignoreCase = true) } }
            return if (index >= 0 && index < parts.size) parts[index].trim() else ""
        }
        return CsvSpeciesRow(
            latinName = col("Nazwa naukowa"),
            polishName = col("Nazwa polska"),
            phylum = col("Gromada"),
            taxonomicClass = col("Rząd", "Rzad"),
            suborder = col("Podrząd", "Podrzad"),
            family = col("Rodzina"),
            occurrence = col("Występowanie"),
            lifeMode = col("Tryb życia", "Tryb zycia"),
            temperatureDay = col("Temperatura dzień", "Temperatura dzien"),
            temperatureNight = col("Temperatura noc"),
            humidity = col("Wilgotność", "Wilgotnosc"),
            sizeMale = col("Wielkość samiec", "Wielkosc samiec"),
            sizeFemale = col("Wielkość samica", "Wielkosc samica"),
            temperament = col("Temperament"),
            venom = col("Jad"),
            difficulty = col("Zaawansowanie"),
            cites = col("CITES"),
            diet = col("Dieta"),
            documentedBreeding = col("Udokumentowane rozmnażenia", "Udokumentowane rozmnozenia")
        )
    }

    private data class CsvSpeciesRow(
        val latinName: String,
        val polishName: String,
        val phylum: String,
        val taxonomicClass: String,
        val suborder: String,
        val family: String,
        val occurrence: String = "",
        val lifeMode: String = "",
        val temperatureDay: String = "",
        val temperatureNight: String = "",
        val humidity: String = "",
        val sizeMale: String = "",
        val sizeFemale: String = "",
        val temperament: String = "",
        val venom: String = "",
        val difficulty: String = "",
        val cites: String = "",
        val diet: String = "",
        val documentedBreeding: String = ""
    )

    internal fun latinToStableId(latin: String): String =
        latin.lowercase()
            .replace(Regex("[^a-z0-9]+"), "_")
            .trim('_')

    internal fun inferCategory(phylum: String, family: String): String {
        val p = phylum.lowercase()
        val f = family.lowercase()
        return when {
            p.contains("pajęczak") || f.contains("theraphosidae") -> "tarantula"
            p.contains("gady") || p.contains("reptil") -> "reptile"
            p.contains("płaz") || p.contains("amphib") -> "amphibian"
            p.contains("owad") || p.contains("insect") -> "insect"
            p.contains("równonog") || p.contains("isopod") -> "isopod"
            else -> "other"
        }
    }

    /** Drzewo Home: gromada → rząd → podrząd (jeśli jest) → rodzina → gatunki. */
    fun buildTaxonomyTree(species: List<Species>): TaxonomyNode {
        if (species.isEmpty()) {
            return TaxonomyNode(title = "Baza gatunków", subtitle = "Brak wpisów w katalogu")
        }
        val phylumNodes = species
            .groupBy { it.phylum.ifBlank { "Inne" } }
            .toSortedMap(String.CASE_INSENSITIVE_ORDER)
            .map { (phylum, inPhylum) -> buildPhylumBranch(phylum, inPhylum) }
        return TaxonomyNode(
            title = "Terrarium",
            subtitle = "${species.size} gatunków",
            children = phylumNodes
        )
    }

    fun toSpeciesPicks(species: List<Species>): List<TaxonomySpeciesPick> =
        species.map { sp ->
            TaxonomySpeciesPick(
                latinName = sp.latinName,
                commonName = sp.commonName.takeIf { it.isNotBlank() }
            )
        }

    fun findLeafInTree(root: TaxonomyNode, latin: String): TaxonomyNode? {
        val target = latin.trim()
        if (target.isEmpty()) return null
        fun walk(node: TaxonomyNode): TaxonomyNode? {
            if (node.isLeaf && node.title.equals(target, ignoreCase = true)) return node
            for (child in node.children) {
                walk(child)?.let { return it }
            }
            return null
        }
        return walk(root)
    }

    private fun buildPhylumBranch(phylum: String, species: List<Species>): TaxonomyNode {
        val orderNodes = species
            .groupBy { it.taxonomicClass.ifBlank { "—" } }
            .toSortedMap(String.CASE_INSENSITIVE_ORDER)
            .map { (order, inOrder) -> buildOrderBranch(order, inOrder) }
        return taxBranch(phylum, subtitle = "Gromada", children = orderNodes)
    }

    private fun buildOrderBranch(order: String, species: List<Species>): TaxonomyNode {
        val withSuborder = species.filter { it.suborder.isNotBlank() }
        val withoutSuborder = species.filter { it.suborder.isBlank() }
        val children = mutableListOf<TaxonomyNode>()
        withSuborder
            .groupBy { it.suborder.trim() }
            .toSortedMap(String.CASE_INSENSITIVE_ORDER)
            .forEach { (suborder, inSuborder) ->
                children.add(
                    taxBranch(
                        title = suborder,
                        subtitle = "Podrząd",
                        children = buildFamilyBranches(inSuborder)
                    )
                )
            }
        if (withoutSuborder.isNotEmpty()) {
            children.addAll(buildFamilyBranches(withoutSuborder))
        }
        return taxBranch(order, subtitle = "Rząd", children = children)
    }

    private fun buildFamilyBranches(species: List<Species>): List<TaxonomyNode> =
        species
            .groupBy { it.family.ifBlank { "Rodzina nieznana" } }
            .toSortedMap(String.CASE_INSENSITIVE_ORDER)
            .map { (family, inFamily) ->
                val leaves = inFamily
                    .sortedBy { it.latinName.lowercase() }
                    .map { sp -> speciesToLeaf(sp) }
                taxBranch(family, subtitle = "Rodzina", children = leaves)
            }

    private fun speciesToLeaf(species: Species): TaxonomyNode =
        TaxonomyNode(
            title = species.latinName,
            subtitle = species.commonName.takeIf { it.isNotBlank() }
        )

    private fun taxBranch(title: String, subtitle: String? = null, children: List<TaxonomyNode>): TaxonomyNode {
        val count = children.sumOf { countLeaves(it) }
        val levelLabel = subtitle?.let { "$it · $count gat." } ?: "$count gatunków"
        return TaxonomyNode(
            title = title,
            subtitle = levelLabel,
            children = children
        )
    }

    private fun countLeaves(node: TaxonomyNode): Int =
        if (node.isLeaf) 1 else node.children.sumOf { countLeaves(it) }
}

internal fun mergeSpeciesLists(catalog: List<Species>, firestore: List<Species>): List<Species> {
    val merged = catalog.associateBy { it.latinName.lowercase() }.toMutableMap()
    for (remote in firestore) {
        val key = remote.latinName.lowercase()
        if (key.isBlank()) continue
        val local = merged[key]
        merged[key] = if (local != null) local.enrichFrom(remote) else remote
    }
    return merged.values.sortedBy { it.latinName.lowercase() }
}

private fun Species.enrichFrom(remote: Species): Species = copy(
    id = remote.id.ifBlank { id },
    commonName = remote.commonName.ifBlank { commonName },
    category = remote.category.ifBlank { category },
    occurrence = occurrence.ifBlank { remote.occurrence }.ifBlank { remote.origin },
    lifeMode = lifeMode.ifBlank { remote.lifeMode },
    temperatureDay = temperatureDay.ifBlank { remote.temperatureDay }.ifBlank { remote.temperature },
    temperatureNight = temperatureNight.ifBlank { remote.temperatureNight },
    humidity = humidity.ifBlank { remote.humidity },
    sizeMale = sizeMale.ifBlank { remote.sizeMale }.ifBlank { remote.size },
    sizeFemale = sizeFemale.ifBlank { remote.sizeFemale },
    temperament = temperament.ifBlank { remote.temperament },
    venom = venom.ifBlank { remote.venom },
    difficulty = difficulty.ifBlank { remote.difficulty },
    cites = cites.ifBlank { remote.cites },
    diet = diet.ifBlank { remote.diet },
    documentedBreeding = documentedBreeding.ifBlank { remote.documentedBreeding },
    careNotes = if (remote.careNotes.isNotBlank() && !remote.careNotes.startsWith("Gromada:")) {
        remote.careNotes
    } else {
        careNotes
    },
    funFact = remote.funFact.ifBlank { funFact },
    imageUrl = remote.imageUrl.ifBlank { imageUrl },
    phylum = phylum.ifBlank { remote.phylum },
    taxonomicClass = taxonomicClass.ifBlank { remote.taxonomicClass },
    suborder = suborder.ifBlank { remote.suborder },
    family = family.ifBlank { remote.family },
    temperature = temperature.ifBlank { remote.temperature },
    lifespan = lifespan.ifBlank { remote.lifespan },
    size = size.ifBlank { remote.size },
    origin = origin.ifBlank { remote.origin }
)
