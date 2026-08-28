# Problemy i wyzwania

## Baza gatunków — rzetelne dane

**Status:** decyzja robocza podjęta: budujemy własną bazę stopniowo, bez scrapingu treści z zewnętrznych serwisów.

### Dlaczego to problem

- Błędne parametry (temp/wilgotność) mogą zaszkodzić zwierzętom.
- Duża liczba gatunków = dużo pracy przy weryfikacji.
- Źródła internetowe często się sprzeczają.

### Decyzja robocza

- CSV zostaje szkieletem bazy: nazwa naukowa, nazwa polska, taksonomia.
- Parametry hodowlane (temperatura, wilgotność, wielkość, dieta, CITES itd.) będą uzupełniane etapami — **docelowo większość treści generowana z pomocą AI, a następnie weryfikowana i poprawiana ręcznie** (przez właściciela lub moderatorów).
- Nie kopiujemy masowo danych z Terrarium ani innych serwisów bez zgody/licencji.
- Linki zewnętrzne mogą być pomocniczą referencją, ale użytkownik ma zostawać w aplikacji.
- W późniejszej fazie możliwa pomoc ekspercka/moderatorska, np. znajomy sklep/hodowca, po pokazaniu okrojonej wersji aplikacji.

### Poziomy kompletności danych

| Poziom | Zakres | Cel |
|--------|--------|-----|
| A — szkielet | Nazwa naukowa, nazwa polska, taksonomia | Szybko pokryć dużą liczbę gatunków |
| B — parametry podstawowe | Występowanie, tryb życia, temp., wilgotność, wielkość | MVP dla popularnych gatunków |
| C — zweryfikowane | Dieta, CITES, temperament, rozmnażanie, notatki eksperta | Dane po weryfikacji przez właściciela/moderatora |

### Co mamy teraz

- `terrarium_species_care_template.csv` w assets.
- `terrarium_species.csv` jako główna lista gatunków w aplikacji.
- Taksonomia ptasników (`TheraphosidaeTaxonomy.kt`).
- Merge z Firestore `species` + seed (`SeedData.kt`).

### Co brakuje

- Proces weryfikacji danych (kto zatwierdza zmiany).
- Zdjęcia gatunków w jakości i z licencją.
- Większe pokrycie ptaszników oraz osobno gadów, płazów i owadów.
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
