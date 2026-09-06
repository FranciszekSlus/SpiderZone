# Zadania i stan projektu

> Aktualizacja: 2026-08-28. Oparte na kodzie w repo (Android Kotlin jako referencja + Flutter jako klient docelowy).
> Aktywna tablica: docs/tasks/TASKS.md · Technika: docs/12-dzialanie-techniczne.md

Legenda: ✅ zrobione · 🟡 czesciowo · ❌ brakuje · ⚠️ problem / blokada

## 1. Moduly funkcjonalne

### Gatunki / Home search

| Element | Stan | Uwagi |
|---------|------|-------|
| Wyszukiwarka na Home | ✅ | Live search po nazwie łacińskiej i polskiej |
| Filtry (ptaszniki, nazwa PL, rodzina) | ✅ | Chipy + bottom sheet rodziny |
| Karta gatunku (szczegóły) | ✅ | Route `/species/:id`, taksonomia + parametry hodowlane |
| Lokalny CSV w assets | ✅ | `terrarium_species.csv` (~280+ gatunków) |
| Osobna zakładka Gatunki | ❌ | Celowo — wyszukiwarka jest na Home (FLUT-04) |

### Rozmnazanie

| Element | Stan | Uwagi |
|---------|------|-------|
| Zakladka w nawigacji | ✅ | |
| Dodawanie wpisu | ✅ | Gatunek z hodowli |
| Feed innych hodowcow | ✅ | breedingPosts |
| Filtr po gatunkach z hodowli | ✅ | |
| Prywatne / publiczne wpisy | ✅ | `isPublic` w `breedingPosts`, własne wpisy zawsze dla autora, cudze tylko publiczne |
| Status (plan / w trakcie / sukces / fail) | ✅ | `status` |
| Data rozpoczecia / data kokonu | ✅ | `startDate`, `cocoonDate` |
| Zdjecia i filmy | ✅ | `photoUrls`, `videoUrls` |
| Warunki i zachowanie | ❌ | Do rozbudowy filtrowania i notatek strukturalnych |

### Media

| Element | Stan | Uwagi |
|---------|------|-------|
| Zdjecia i filmy | 🟡 | Docelowo Cloudinary Free; czesc kodu nadal pod Firebase Storage |
| Kompresja przed uploadem | ❌ | Zadanie COST-01 |
| Paginacja feedow | ❌ | Zadanie COST-01 |

## 2. Infrastruktura i jakosc

| Element | Stan | Uwagi |
|---------|------|-------|
| firestore.rules w repo | ✅ | Deploy w Console do potwierdzenia |
| firestore.indexes.json | ✅ | breedingPosts dodany |
| storage.rules | 🟡 | Fallback, bo media idziemy w Cloudinary |
| Refaktoryzacja UI (moduly) | ❌ | MainActivity ~3800 linii |
| Flutter SDK | ✅ | Flutter 3.44.2 działa lokalnie |
| Flutter platformy | ✅ | `flutter create .` wygenerował `android/` i `ios/` |
| Flutter analyze/test/run | ✅ | Analyze/test OK, run działa na Android emulator |
| FlutterFire | ❌ | Do wykonania `flutterfire configure` |
| iOS test plan | ❌ | Wymaga Mac/Xcode lub cloud Mac |

## 3. Problemy jako zadania (priorytet)

### P1 — przed publikacja

| ID | Zadanie | Opis |
|----|---------|------|
| DATA-01 | Strategia bazy gatunkow | Dyskusja + plan weryfikacji danych |
| DATA-02 | Gatunek wzorcowy | 1 pełny gatunek z kompletem danych i legalnym zdjęciem |
| UX-01 | Design system Flutter | Komponenty, typografia, kolory, puste stany |
| UX-02 | Home z wyszukiwarką | Bez osobnego ekranu wyszukiwania |
| FLUT-02 | Auth we Flutter | Logowanie, rejestracja, email gate |
| FLUT-03A | Gatunki we Flutter | ✅ Home search + karta gatunku |
| MEDIA-01 | Migracja uploadu mediow do Cloudinary Free | Zastapic upload Firebase Storage, zapisywac URL w Firestore |
| MEDIA-02 | Cloudinary Flutter service | Unsigned upload preset + multipart upload |
| COST-01 | Kompresja zdjec + paginacja feedow | Limity Spark + transfer Cloudinary |
| UI-01 | Refaktoryzacja MainActivity | Podzial na pliki/moduly |
| LEGAL-01 | Polityka prywatnosci | RODO |
| LEGAL-02 | Regulamin | Tresci i odpowiedzialnosc |
| LEGAL-03 | Usuniecie konta | UI + kasowanie danych |
| FB-01 | Deploy reguly i indeksow Firebase | Console / CLI |
| FB-02 | Migracja fallbacku breeding | `breedingPostsFallback` -> `users/{uid}/breedingPosts` |

### P2 — po MVP

| ID | Zadanie |
|----|---------|
| FLUT-01 | Dokończyć FlutterFire |
| FLUT-03B | Hodowla we Flutter |
| FLUT-03C | Rozmnażanie we Flutter |
| FLUT-03D | Społeczność we Flutter |
| FLUT-03E | Profil i ustawienia we Flutter |
| IOS-01 | Plan testowania iOS |
| REM-01 | Przypomnienia cykliczne + FCM |
| SOC-01 | Krotkie wpisy i pytania spolecznosci |

## 4. Nastepne kroki (kolejnosc)

1. FLUT-01 — `flutterfire configure`
2. UX-01 / UX-02 — design system i Home z wyszukiwarką
3. FLUT-02 — Auth we Flutter
4. DATA-02 — gatunek wzorcowy
5. MEDIA-01 / MEDIA-02 — Cloudinary
6. FLUT-03A — gatunki we Flutter
7. FLUT-03B / FLUT-03C — hodowla i rozmnażanie we Flutter
8. FB-01 / FB-02 — reguły i migracja fallbacku
