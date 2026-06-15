# SpiderZone (MVP)

SpiderZone to mobilna aplikacja dla pasjonatow egzotycznych zwierzat, z naciskiem na ptaszniki i mozliwoscia rozszerzenia o gady oraz plazy.

**Pelna dokumentacja projektu:** [`docs/README.md`](docs/README.md)  
**Działanie techniczne i sekrety:** [`docs/12-dzialanie-techniczne.md`](docs/12-dzialanie-techniczne.md)  
**Zadania:** [`docs/tasks/TASKS.md`](docs/tasks/TASKS.md)

## Co jest gotowe w MVP

- Logowanie, rejestracja i reset hasla przez Firebase Authentication.
- Baza gatunkow z Firestore (`species`) z filtrowaniem po poziomie trudnosci.
- Modul "Moja hodowla" (`users/{uid}/animals`) z notatkami i stadium.
- Modul rozmnażania (`breedingPosts`) z filtrowaniem po gatunkach z Twojej hodowli.
- Przypomnienia w ustawieniach profilu (`users/{uid}/reminders`) z lokalnymi powiadomieniami.
- Profil i podstawowe statystyki kolekcji.
- Ciemny motyw zgodny z kierunkiem SpiderZone.

## Flutter (iOS + Android)

Fundament Flutter (dark theme, routing, Firebase bootstrap) jest w katalogu [`flutter_app/`](flutter_app/README.md).

Po instalacji Flutter SDK:

```bash
cd flutter_app
flutter create . --org com.example.spiderzone --project-name spiderzone
flutter pub get
flutterfire configure --project=spiderzone-d112d
flutter run
```

## Co musisz zrobic lokalnie (Android natywny)

1. Dodaj Firebase do projektu Android:
   - skopiuj `google-services.json` do `app/`,
   - wlacz Authentication (Email/Password), Firestore i Storage.
2. (Opcjonalnie) Dodaj aplikacje iOS i ustaw `GoogleService-Info.plist`, jesli migrujesz ten projekt do Flutter.
3. Uruchom:
   - `./gradlew.bat test`
   - `./gradlew.bat assembleDebug`

## Struktura danych Firestore

- `species` - publiczna baza gatunkow.
- `users/{uid}` - profil.
- `users/{uid}/animals` - prywatna hodowla.
- `breedingPosts` - wpisy rozmnażania (czytelne dla zalogowanych).
- `users/{uid}/reminders` - przypomnienia (ustawienia profilu).

## Tanie uruchomienie (bez budzetu)

- Korzystaj z planu Firebase Spark.
- Kompresuj zdjecia przed wyslaniem.
- Uzywaj paginacji i limitow zapytan.
- Trzymaj realtime listenery tylko na aktywnych ekranach.
