# Zadania i stan projektu

> Aktualizacja: 2026-04-26. Oparte na kodzie w repo (Android Kotlin + szkielet Flutter).  
> **Aktywna tablica:** [tasks/TASKS.md](tasks/TASKS.md) · **Technika:** [12-dzialanie-techniczne.md](12-dzialanie-techniczne.md)

Legenda: ✅ zrobione · 🟡 częściowo · ❌ brakuje · ⚠️ problem / blokada

---

## 1. Moduły funkcjonalne

### Auth i konto

| Element | Stan | Uwagi |
|---------|------|-------|
| Rejestracja email/hasło | ✅ | |
| Logowanie | ✅ | |
| Reset hasła | ✅ | |
| Weryfikacja email | ✅ | Blokada przed pełnym dostępem |
| Profil: pseudonim, imię, nazwisko, poziom | ✅ | |
| Avatar upload | ✅ | |
| Zapamiętaj mnie | ✅ | |
| Usunięcie konta (RODO) | ❌ | Wymóg Google Play |

### Baza gatunków

| Element | Stan | Uwagi |
|---------|------|-------|
| CSV lokalny + taksonomia | ✅ | `terrarium_species_care_template.csv` |
| Firestore `species` + seed | ✅ | |
| Wyszukiwanie po nazwie | ✅ | |
| Filtr trudności | ✅ | |
| Ekran szczegółów gatunku | ✅ | |
| Filtry: temperament, region, rozmiar | 🟡 | Do rozbudowy |
| Porównywarka 2 gatunków | ❌ | |
| Zdjęcia gatunków (jakościowe) | 🟡 | Często puste URL |
| **Rzetelna baza danych** | ⚠️ | **Problem otwarty — dyskusja** |
| Gady / płazy w bazie | 🟡 | Struktura jest, dane słabe |

### Hodowla

| Element | Stan | Uwagi |
|---------|------|-------|
| Dodawanie / edycja zwierząt | ✅ | |
| Zdjęcia i filmy | ✅ | Upload Storage |
| Publiczne / prywatne | ✅ | `isPublic` → `publicAnimals` |
| Stadium, płeć, notatki | ✅ | |
| Historia linienia (osobna) | ❌ | Tylko notatki w polu |
| Log wzrostu | ❌ | |

### Społeczność

| Element | Stan | Uwagi |
|---------|------|-------|
| Feed publicznych pupili | ✅ | Siatka |
| Profil publiczny hodowcy | ✅ | |
| Szczegóły pupila | ✅ | |
| Komentarze | ❌ | Backlog |
| Obserwowanie użytkowników | ❌ | |
| Czaty | ❌ | Backlog |

### Rozmnażanie

| Element | Stan | Uwagi |
|---------|------|-------|
| Zakładka w nawigacji | ✅ | |
| Dodawanie wpisu | ✅ | Gatunek z hodowli |
| Feed innych hodowców | ✅ | `breedingPosts` |
| Filtr po gatunkach z hodowli | ✅ | |
| **Prywatne / publiczne wpisy** | ❌ | **Decyzja: ma być — do kodu** |
| Status (plan / w trakcie / sukces / fail) | ❌ | |
| Data, warunki, zachowanie | ❌ | |
| Edycja / usuwanie wpisu | ❌ | |

### Przypomnienia

| Element | Stan | Uwagi |
|---------|------|-------|
| Sekcja w ustawieniach profilu | ✅ | |
| Dodawanie + lista | ✅ | |
| Lokalne powiadomienia Android | ✅ | |
| Powiązanie ze zwierzęciem | ❌ | |
| Harmonogram cykliczny | ❌ | |
| Push FCM | ❌ | |

### Profil i ustawienia

| Element | Stan | Uwagi |
|---------|------|-------|
| Statystyki hodowli | ✅ | |
| Bio | ✅ | |
| Motyw jasny/ciemny/system | ✅ | |
| Przypomnienia w ustawieniach | ✅ | |

---

## 2. Flutter (`flutter_app/`)

| Element | Stan | Uwagi |
|---------|------|-------|
| Dark theme + tokeny | ✅ | |
| Routing 5 zakładek | ✅ | |
| Firebase bootstrap | 🟡 | Placeholder `REPLACE_ME` |
| `flutter create` + platformy | ❌ | Flutter nie w PATH na dev |
| Parity z Android | ❌ | Tylko placeholdery ekranów |

---

## 3. Infrastruktura i jakość

| Element | Stan | Uwagi |
|---------|------|-------|
| `firestore.rules` w repo | ✅ | Deploy w Console? — sprawdzić |
| `storage.rules` | ✅ | |
| Indeksy Firestore | 🟡 | `breedingPosts` dodany |
| Testy jednostkowe | 🟡 | Kilka smoke testów |
| Refaktoryzacja UI (moduły) | ❌ | ⚠️ MainActivity ~3800 linii |
| CI (GitHub Actions) | ❌ | |
| Crashlytics | ❌ | |

---

## 4. Problemy jako zadania (priorytetowe)

Te nie są „feature”, tylko **blokery jakości / launchu**:

### ⚠️ P1 — przed publikacją

| ID | Zadanie | Opis |
|----|---------|------|
| LEGAL-01 | Polityka prywatności | RODO, Firebase, dane użytkownika |
| LEGAL-02 | Regulamin | Treści, odpowiedzialność, zakazy |
| LEGAL-03 | Usunięcie konta | UI + kasowanie Firestore + Storage |
| DATA-01 | **Strategia bazy gatunków** | Dyskusja + plan weryfikacji danych |
| UI-01 | **Refaktoryzacja MainActivity** | Podział na pliki/moduły |
| FB-01 | Deploy reguł i indeksów Firebase | Console / CLI |
| BREED-01 | `isPublic` na wpisach rozmnażania | Zgodnie z decyzją produktową |
| COST-01 | Kompresja zdjęć + paginacja feedów | Limity Spark |

### P2 — po MVP

| ID | Zadanie |
|----|---------|
| TECH-01 | Decyzja Flutter vs Android — [09-flutter-vs-android.md](09-flutter-vs-android.md) |
| BREED-02 | Statusy, daty, warunki, zachowanie w rozmnażaniu |
| REM-01 | Przypomnienia cykliczne + FCM |
| SOC-01 | Komentarze |
| MOD-01 | Role moderatorów |

---

## 5. Wykonane (skrót)

- Auth pełny cykl + weryfikacja email
- Gatunki: CSV, taksonomia, Firestore, szczegóły, wyszukiwanie
- Hodowla z mediami i widocznością publiczną
- Społeczność: feed + profile
- Rozmnażanie: podstawowy feed i dodawanie
- Przypomnienia w ustawieniach
- Dokumentacja w `docs/`
- Szkielet Flutter

---

## 6. Następne kroki (sugerowana kolejność)

1. **DATA-01** — spotkanie/decyzja: skąd bierzemy dane gatunków.
2. **BREED-01** — prywatne/publiczne wpisy rozmnażania.
3. **UI-01** — refaktoryzacja (łatwiejsze dalsze feature).
4. **LEGAL-01..03** — przed Google Play.
5. **TECH-01** — Flutter vs Android (po przeczytaniu [09](09-flutter-vs-android.md)).
6. **FB-01** — deploy Firebase.

---

*Po każdym większym merge zaktualizuj tabele w sekcji 1–3.*
