# Audyt i roadmapa Flutter-first

> Cel: ograniczyc dalsze inwestowanie w natywny Android i prowadzic nowe prace tak, zeby docelowa aplikacja Flutter dzialala na Android + iOS.

## Stan po FLUT-01

| Obszar | Stan | Uwagi |
|--------|------|-------|
| Flutter SDK | ✅ | Flutter 3.44.2 uruchomiony lokalnie |
| Platformy Flutter | ✅ | `flutter create .` wygenerowal `android/`, `ios/`, `web/`, `windows/`, `linux/`, `macos/` |
| Motyw Flutter | 🟡 | Jest Terrarium Night, ale wymaga dopracowania komponentow |
| Firebase Flutter | 🟡 | Jest placeholder `firebase_options.dart`; trzeba uruchomic `flutterfire configure` |
| Android Kotlin | 🟡 | Dzialajacy prototyp/referencja, nie docelowy klient |
| iOS | ❌ | Struktura `ios/` istnieje, ale testowanie wymaga Maca/Xcode albo uslugi CI/Mac cloud |

## Decyzje po analizie UI

- Nie potrzebujemy osobnej zakladki/ekranu tylko do wyszukiwarki.
- Home powinien laczyc: wyszukiwanie gatunkow, skroty do modulow, status hodowli i ostatnie aktywnosci.
- Zakladka Gatunki moze zostac jako baza/encyklopedia po kliknieciu z Home, ale glowna wyszukiwarka ma byc dostepna od razu na Home.
- Kotlin Compose zostaje jako prototyp funkcji, ale nowe duze UI projektujemy i budujemy we Flutter.
- Styl wymaga osobnej pracy UX: profil, ustawienia, karty zwierzat, karty gatunkow, feed, formularze i puste stany.

## Najwieksze braki produktu

### UX i wyglad

- Brak finalnej hierarchii ekranu Home.
- Brak pelnego design systemu komponentow: karty, chipy, pola formularzy, bottom sheets, dialogi, puste stany, loading/error.
- Profil i ustawienia nie maja docelowego ukladu.
- Brak spisanych animacji per przeplyw.
- Brak responsywnosci tablet/iPad.

### Dane i backend

- Firebase rules nie sa jeszcze stabilnie wdrozone z CLI.
- `breedingPostsFallback` jest tymczasowym obejściem, nie docelowa architektura.
- Media docelowo maja isc przez Cloudinary, ale nie ma jeszcze konta, `cloud_name`, unsigned upload preset ani serwisu uploadu.
- Baza gatunkow ma szkielet, ale brakuje pelnych profili i zdjec z licencja.
- `species.cites` docelowo ma byc boolean, ale CSV/model nadal wymaga uporzadkowania.

### Flutter

- Brak Auth we Flutter.
- Brak prawdziwych repozytoriow danych Firestore we Flutter.
- Ekrany sa placeholderami.
- Brak konfiguracji Firebase przez FlutterFire.
- Brak testow integracyjnych na Androidzie i planu testowania iOS.

### Funkcje

- Hodowla: brak pelnego dodawania/edycji zwierzat we Flutter.
- Rozmnazanie: brak filtrow po statusie/datach/mediach.
- Powiadomienia: lokalne Android dzialaja w Kotlin, ale Flutter potrzebuje `flutter_local_notifications`/FCM i wspolnego modelu harmonogramow.
- Spolecznosc: brak komentarzy, obserwowania, pytan otwartych i moderacji.

## Roadmapa robocza

### Etap 1 — Fundament Flutter

1. `FLUT-01` — zakonczyc setup: `flutterfire configure`, `flutter analyze`, `flutter test`, `flutter run` na emulatorze.
2. `FLUT-04` — ustalic routing i nawigacje docelowa: Home jako centrum + wyszukiwarka gatunkow.
3. `UX-01` — przygotowac design system Flutter: kolory, typografia, spacing, karty, przyciski, pola, chipy, bottom sheet.
4. `UX-02` — zaprojektowac Home: search, ostatnie zwierzeta, ostatnie wpisy, skroty, status Firebase tylko jako dev info.

### Etap 2 — Dane i Auth

1. `FLUT-02` — Auth: logowanie, rejestracja, reset hasla, email verification gate.
2. `FB-01` — wdrozyc Firestore/Storage rules i indeksy.
3. `FB-02` — usunac fallback `breedingPostsFallback` po wdrozeniu rules i zmigrowac dane do subkolekcji.
4. `DATA-02` — utworzyc 1 pelny gatunek wzorcowy z kompletem pol i legalnym zdjeciem.

### Etap 3 — Media i Cloudinary

1. `MEDIA-01` — zalozyc Cloudinary Free, utworzyc unsigned upload preset.
2. `MEDIA-02` — Flutter service do uploadu `multipart/form-data` na `https://api.cloudinary.com/v1_1/<cloud_name>/upload`.
3. `MEDIA-03` — kompresja/resize przed uploadem i zapis URL w Firestore.
4. `MEDIA-04` — cache obrazow i miniatury w feedach.

### Etap 4 — Moduly Flutter

1. `FLUT-03A` — gatunki: lokalny CSV, search na Home, karta gatunku.
2. `FLUT-03B` — hodowla: lista, dodawanie, edycja, media, prywatne/publiczne.
3. `FLUT-03C` — rozmnazanie: wpisy, statusy, daty, media, filtry.
4. `FLUT-03D` — spolecznosc: publiczne zwierzeta, profile publiczne.
5. `FLUT-03E` — profil i ustawienia: dane konta, avatar, motyw, przypomnienia.

### Etap 5 — Testy i iOS

1. `TEST-01` — smoke tests Flutter: app start, routing, theme.
2. `TEST-02` — manual test checklist Android emulator + fizyczne urzadzenie Android.
3. `IOS-01` — opisac wymagania iOS: Mac/Xcode, Apple Developer, bundle id, Firebase iOS app.
4. `IOS-02` — pierwszy build iOS na Macu albo w usludze CI/cloud Mac.

## Cloudinary — ustalenia techniczne

Cloudinary wspiera upload z Fluttera przez unsigned upload preset. W aplikacji nie wolno trzymac API secret. Minimalny model:

```text
Flutter picker -> kompresja -> multipart POST do Cloudinary
              -> secure_url
              -> Firestore photoUrls/videoUrls
```

Wymagane:

- `cloud_name`
- nazwa unsigned upload preset
- folder, np. `spiderzone/dev`
- limity rozmiaru pliku i typow MIME
- docelowo osobne presety dla obrazow i filmow

Dokumentacja referencyjna: [Cloudinary Flutter image and video upload](https://cloudinary.com/documentation/flutter_image_and_video_upload).

## Co nie robimy juz w Kotlinie

- Nie budujemy finalnego UI profilu, ustawien i Home.
- Nie rozwijamy duzych nowych modulow spolecznosci.
- Nie robimy docelowego uploadu Cloudinary w Kotlinie, chyba ze tylko testowo.
- Nie dopieszczamy pixel-perfect Android Compose.

Kotlin ma zostac jako referencja zachowania i awaryjny prototyp, a docelowa implementacja idzie do Flutter.
