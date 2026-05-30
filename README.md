# SpiderZone (MVP)

SpiderZone to mobilna aplikacja dla pasjonatow egzotycznych zwierzat, z naciskiem na ptaszniki i mozliwoscia rozszerzenia o gady oraz plazy.

## Co jest gotowe w MVP

- Logowanie, rejestracja i reset hasla przez Firebase Authentication.
- Baza gatunkow z Firestore (`species`) z filtrowaniem po poziomie trudnosci.
- Modul "Moja hodowla" (`users/{uid}/animals`) z notatkami i stadium.
- Modul przypomnien (`users/{uid}/reminders`) z lokalnymi powiadomieniami.
- Profil i podstawowe statystyki kolekcji.
- Ciemny motyw zgodny z kierunkiem SpiderZone.

## Co musisz zrobic lokalnie

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
- `users/{uid}/reminders` - przypomnienia.

## Tanie uruchomienie (bez budzetu)

- Korzystaj z planu Firebase Spark.
- Kompresuj zdjecia przed wyslaniem.
- Uzywaj paginacji i limitow zapytan.
- Trzymaj realtime listenery tylko na aktywnych ekranach.
