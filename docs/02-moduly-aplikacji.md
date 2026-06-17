# Moduły aplikacji

## Nawigacja główna

| Zakładka | Moduł |
|----------|--------|
| Home | Podsumowanie, skróty, wyszukiwarka gatunków, ostatnie aktywności |
| Gatunki | Baza wiedzy / szczegóły po wejściu z Home lub listy |
| Hodowla | Własne zwierzęta |
| Społeczność | Publiczni hodowcy i pupile |
| Rozmnażanie | Dokumentacja breeding |
| Profil | Konto + **ustawienia** (w tym przypomnienia) |

---

## Baza gatunków

**Cel:** encyklopedia z parametrami hodowlanymi.

**Dane:** nazwa łacińska i potoczna, zdjęcia, trudność, temperament, wilgotność, temperatura, długość życia, rozmiar, pochodzenie, zalecenia, ciekawostki.

**Funkcje:**
- Wyszukiwanie po nazwie dostępne bezpośrednio na Home.
- Filtry (trudność — zaimplementowane; temperament, region — częściowo / do rozbudowy).
- Taksonomia ptasników (drzewo kategorii).
- Docelowo: porównywarka 2 gatunków, wyszukiwanie po zdjęciu (AI).

**Źródła danych:** patrz [07-problemy-i-wyzwania.md](07-problemy-i-wyzwania.md#baza-gatunków-rzetelne-dane).

---

## Home

**Cel:** ekran startowy, który od razu pomaga znaleźć gatunek i wrócić do najważniejszych działań.

**Docelowe elementy:**
- Wyszukiwarka gatunków w bazie.
- Skróty: dodaj zwierzę, dodaj rozmnażanie, przypomnienia, profil.
- Ostatnie zwierzęta z hodowli.
- Ostatnie wpisy rozmnażania.
- Status danych / puste stany dla nowych użytkowników.

**Decyzja:** nie robimy osobnego okna tylko do wyszukiwarki; wyszukiwanie gatunków ma być częścią Home.

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

**Widoczność:** wpis może być **prywatny** lub **publiczny** (`isPublic` na `breedingPosts`).

**Dane wpisu (obecne + planowane):**
- Gatunek wybierany przez wyszukiwanie w bazie gatunków.
- Samica / samiec (etykiety).
- Status: planowane / w trakcie / udane / nieudane.
- Data rozpoczęcia i data kokonu.
- Notatki.
- Zdjęcia i filmy.
- **Do dodania:** bardziej szczegółowe warunki (temp/wilgotność), zachowanie, filtrowanie po statusie/datach.

**Feed:** własne wpisy zawsze widoczne dla autora; wpisy innych tylko jeśli są publiczne; filtr po gatunkach z własnej hodowli.

**Kolekcje:**
- `users/{uid}/breedingPosts/{postId}` — wpisy własne, także prywatne.
- `breedingPosts/{postId}` — tylko publiczny feed.

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

---

## Krótkie wpisy i pytania (pomysł na później)

**Cel:** szybkie posty społecznościowe w stylu tweetów oraz pytania otwarte do innych hodowców.

**Status:** zaakceptowany kierunek, ale nie część MVP rozmnażania.

**Docelowo:** osobna kolekcja, np. `communityPosts`, z typem wpisu (`post` / `question`), widocznością, komentarzami i moderacją.
