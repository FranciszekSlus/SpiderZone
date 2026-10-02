# UX-02 — Home + wyszukiwarka gatunków (szkic v1)

> Status: pierwszy szkic do dopracowania. Zadanie Notion: UX-02.

## Cel

Home ma być wejściem do **bazy gatunków**: przeglądanie drzewem taksonomii + szybkie wyszukiwanie po literach.

## Założenia (v1)

1. Wyszukiwarka jest **na Home** (nie osobna zakładka).
2. Domyślny widok Home = **drzewo taksonomii** (gromada → rząd → podrząd → rodzina → gatunek).
3. Nad drzewem jest pole wyszukiwania.
4. Tap w pole → **płynne przejście do pełnego ekranu wyszukiwania**.
5. Wyniki to **karty gatunków**; brak wyników = komunikat „Nie mamy jeszcze tego gatunku w bazie”.
6. Wstecz / zamknięcie wraca do Home (drzewo bez utraty kontekstu przeglądania).
7. Skróty, ostatnie zwierzęta i wpisy rozmnażania — **kolejna iteracja** (nie w v1).

## Hierarchia ekranu Home (v1)

```text
┌─────────────────────────────┐
│ SpiderZone                  │
│ N gatunków w bazie…         │
│ ┌─────────────────────────┐ │
│ │ 🔍 Szukaj gatunku…      │ │  ← tap → overlay
│ └─────────────────────────┘ │
│ Przeglądaj bazę             │
│ [Terrarium › Pajęczaki …]   │  ← okruszki
│ ┌─────────────────────────┐ │
│ │ Gromada / Rząd / …      │ │  ← karty kategorii
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ Gatunek (liść)          │ │  → karta szczegółów
│ └─────────────────────────┘ │
└─────────────────────────────┘
```

## Przepływ wyszukiwania

1. Użytkownik klika pole na Home.
2. Overlay fullscreen (fade + lekki slide), focus na polu.
3. Wpisuje litery → live search (łacińska, polska, rodzaj, rodzina).
4. Lista kart gatunków; klik → `/species/:id`.
5. Puste wyniki → empty state.
6. Strzałka wstecz / clear+zamknięcie → powrót do Home.

## Co jest w kodzie

- `HomeScreen` — search entry + `TaxonomyBrowser`
- `SpeciesSearchOverlay` — fullscreen search
- `SpeciesCard` — karta wyniku/liścia
- `buildTaxonomyTree` — drzewo z CSV

## Do dopracowania (kolejne szkice)

- [ ] Skróty (dodaj zwierzę / rozmnażanie / przypomnienia)
- [ ] Ostatnie zwierzęta i wpisy
- [ ] Zdjęcia na kartach gatunków
- [ ] Filtry w overlay (opcjonalnie)
- [ ] Puste stany nowej hodowli
- [ ] Animacja Hero bardziej „rozsuwająca” pole (dopracowanie)
