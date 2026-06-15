# Problemy i wyzwania

## Baza gatunków — rzetelne dane

**Status:** temat do **przedyskutowania** z właścicielem (brak finalnej decyzji).

### Dlaczego to problem

- Błędne parametry (temp/wilgotność) mogą zaszkodzić zwierzętom.
- Duża liczba gatunków = dużo pracy przy weryfikacji.
- Źródła internetowe często się sprzeczają.

### Opcje (do dyskusji)

| Opcja | Plusy | Minusy |
|-------|-------|--------|
| Własny CSV + ekspertyza | Kontrola jakości | Czasochłonne |
| Import z otwartych źródeł | Szybki start | Licencje, błędy |
| Społeczność + moderatorzy | Skalowalne | Wymaga moderacji |
| **Hybryda (rekomendacja robocza)** | CSV top 50–100 + Firestore + moderatorzy | Nadal wymaga kuratora |

### Co mamy teraz

- `terrarium_species_care_template.csv` w assets.
- Taksonomia ptasników (`TheraphosidaeTaxonomy.kt`).
- Merge z Firestore `species` + seed (`SeedData.kt`).

### Co brakuje

- Proces weryfikacji danych (kto zatwierdza zmiany).
- Zdjęcia gatunków w jakości i z licencją.
- Pokrycie gadów i płazów.
- Porównywarka gatunków.

---

## Dobra konstrukcja UI

**Problem:** monolityczny `MainActivity.kt`, niespójne stany pustych ekranów, brak design systemu w osobnych plikach.

**Kierunek:** moduły per feature, wspólne komponenty, Flutter ma lepszy start struktury (`flutter_app/lib/features/`).

---

## Prawo i prywatność

**Nie jest to porada prawna.** Przed publikacją w sklepach potrzebne:

| Dokument | Zawartość (skrót) |
|----------|-------------------|
| Polityka prywatności | RODO, Firebase/Google jako procesor, jakie dane, prawo usunięcia |
| Regulamin | Zasady treści, odpowiedzialność użytkownika, zakazy |
| Zgoda przy rejestracji | Checkbox regulamin + polityka |
| Usunięcie konta | Wymóg Google Play — usuń profil, zwierzęta, Storage |

**Status:** nie napisane — zadanie w [11-zadania-stan-projektu.md](11-zadania-stan-projektu.md).

---

## Koszty

### Start (0 zł możliwe)

| Usługa | Koszt |
|--------|-------|
| Firebase Spark | 0 zł przy małym ruchu |
| GitHub | 0 zł |
| Google Play (jednorazowo) | ~25 USD |
| Apple Developer (iOS) | 99 USD/rok |

### Kiedy rosną koszty Firebase

- Dużo odczytów bez paginacji.
- Duże zdjęcia bez kompresji.
- Cloud Functions, ML.

**Szacunek:** setki aktywnych userów przy optymalizacji często 0–50 USD/mies.

---

## Inne ryzyka techniczne

- `google-services.json` / klucze — nie commitować do publicznego repo.
- Indeksy Firestore — trzeba deployować po nowych zapytaniach.
- Flutter SDK nie był w PATH na maszynie dev — szkielet wymaga `flutter create` + `flutterfire configure`.
