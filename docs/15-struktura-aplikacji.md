# Struktura aplikacji

## Android Kotlin (aktywny klient)

| Plik | Rola |
|------|------|
| `app/src/main/java/com/example/spiderzone/MainActivity.kt` | Główna aplikacja Compose; obecnie zawiera większość ekranów i repozytorium |
| `app/src/main/java/com/example/spiderzone/AppTheme.kt` | Składanie motywu Material 3 i wybór trybu jasny/ciemny/system |
| `app/src/main/java/com/example/spiderzone/SpiderZoneColors.kt` | Palety kolorów i tokeny `SpiderZoneColors` |
| `app/src/main/java/com/example/spiderzone/TerrariumSpeciesCatalog.kt` | Ładowanie CSV gatunków i budowanie drzewa taksonomii |
| `app/src/main/java/com/example/spiderzone/TheraphosidaeTaxonomy.kt` | Typy drzewa taksonomii |
| `app/src/main/java/com/example/spiderzone/SeedData.kt` | Przykładowy seed Firestore |
| `app/src/main/java/com/example/spiderzone/ReminderReceiver.kt` | Lokalne powiadomienia przypomnień |

## Dane i reguły

| Plik | Rola |
|------|------|
| `app/src/main/assets/terrarium_species.csv` | Główny lokalny katalog gatunków |
| `app/src/main/assets/terrarium_species_care_template.csv` | Szablon rozszerzonych pól opieki |
| `firestore.rules` | Reguły Firestore |
| `storage.rules` | Reguły Firebase Storage |
| `firestore.indexes.json` | Indeksy Firestore |
| `firebase.json` | Mapowanie reguł i indeksów dla Firebase CLI |

## Dokumentacja

| Plik | Rola |
|------|------|
| `docs/02-moduly-aplikacji.md` | Zachowanie modułów produktu |
| `docs/03-wyglad-ux.md` | Kierunek UI i paleta kolorów |
| `docs/05-architektura-danych.md` | Kolekcje Firestore, media i reguły |
| `docs/tasks/TASKS.md` | Mirror zadań z Notion |

## Kierunek refaktoryzacji

Docelowo `MainActivity.kt` powinien zostać podzielony na moduły:

- `features/auth/`
- `features/species/`
- `features/collection/`
- `features/community/`
- `features/breeding/`
- `features/profile/`
- `core/theme/`
- `core/firebase/`
- `core/ui/`
