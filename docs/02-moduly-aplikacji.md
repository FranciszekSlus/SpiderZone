# Moduły aplikacji

## Nawigacja główna

| Zakładka | Moduł |
|----------|--------|
| Home | Podsumowanie, skróty, taksonomia |
| Gatunki | Baza wiedzy |
| Hodowla | Własne zwierzęta |
| Społeczność | Publiczni hodowcy i pupile |
| Rozmnażanie | Dokumentacja breeding |
| Profil | Konto + **ustawienia** (w tym przypomnienia) |

---

## Baza gatunków

**Cel:** encyklopedia z parametrami hodowlanymi.

**Dane:** nazwa łacińska i potoczna, zdjęcia, trudność, temperament, wilgotność, temperatura, długość życia, rozmiar, pochodzenie, zalecenia, ciekawostki.

**Funkcje:**
- Wyszukiwanie po nazwie (MVP).
- Filtry (trudność — zaimplementowane; temperament, region — częściowo / do rozbudowy).
- Taksonomia ptasników (drzewo kategorii).
- Docelowo: porównywarka 2 gatunków, wyszukiwanie po zdjęciu (AI).

**Źródła danych:** patrz [07-problemy-i-wyzwania.md](07-problemy-i-wyzwania.md#baza-gatunków-rzetelne-dane).

---

## Moja hodowla

**Cel:** własna kolekcja zwierząt.

**Dane:** imię, gatunek, data urodzenia/zakupu, płeć, stadium, zdjęcia, filmy, notatki.

**Widoczność:** **prywatne** lub **publiczne** (`isPublic`).

**Przepływ publiczny:**
```
users/{uid}/animals/{id}  →  (isPublic=true)  →  publicAnimals/{id}
```

**Docelowo:** osobna historia linienia (`molt_history`), log wzrostu (`growth_logs`).

---

## Społeczność

**Cel:** odkrywanie hodowców i ich publicznych zwierząt.

**MVP:** siatka `publicAnimals`, profile `publicProfiles`, szczegóły pupila, profil hodowcy.

**Docelowo:** komentarze, obserwowanie, spersonalizowany feed, czaty.

---

## Rozmnażanie

**Cel:** dokumentacja prób rozmnażania — planowane, w trakcie, udane, nieudane.

**Lokalizacja:** osobna zakładka (nie przypomnienia).

**Widoczność (decyzja):** wpis może być **prywatny** lub **publiczny** — do implementacji w UI i Firestore (`isPublic` na `breedingPosts`).

**Dane wpisu (obecne + planowane):**
- Gatunek (tylko z hodowli użytkownika).
- Samica / samiec (etykiety).
- Notatki.
- **Do dodania:** data, warunki (temp/wilgotność), zachowanie, status (planned / in_progress / success / failed).

**Feed:** publiczne wpisy innych; filtr po gatunkach z własnej hodowli.

**Kolekcja:** `breedingPosts`.

---

## Przypomnienia

**Cel:** informacja kiedy przypada karmienie, sprzątanie, zraszanie, linienie.

**Lokalizacja:** **Profil → Ustawienia** (nie osobna zakładka).

**MVP:** lokalne powiadomienia Android (`AlarmManager`).

**Kolekcja:** `users/{uid}/reminders`.

**Docelowo:** FCM, harmonogramy cykliczne, powiązanie ze zwierzęciem.

---

## Konta i profile

- Rejestracja: email, hasło, pseudonim, imię, nazwisko, poziom hodowcy, avatar.
- Weryfikacja email przed pełnym dostępem.
- Profil: avatar, bio, statystyki, motyw (system/jasny/ciemny).
- Profil publiczny: `publicProfiles/{uid}`.
