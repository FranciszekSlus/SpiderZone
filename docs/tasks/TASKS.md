# SpiderZone — aktywne zadania

> Mirror zadań z Notion.
> Główna tablica: https://app.notion.com/p/5bca70934bbf471d8592f55823020b5d

**Ostatnia aktualizacja:** 2026-06-18

**Notion:** zaktualizowane 2026-06-18 przez MCP (`SpiderZone Tasks`).

---

## In Progress

| ID | Zadanie | Notatki |
|----|---------|---------|
| FLUT-01 | Dokończyć konfigurację Flutter/Firebase | Flutter SDK działa, platformy wygenerowane, `flutter run` działa; zostało `flutterfire configure` i Firebase options |
| MEDIA-02 | Flutter: upload Cloudinary multipart + zapis `secure_url` w Firestore | Testowy multipart upload działa w `Hodowla` przez `spiderzone_dev_unsigned`; zostało zapisanie `secure_url` w modelach Firestore |

---

## Todo (priorytet)

| ID | Zadanie | Priorytet |
|----|---------|-----------|
| DATA-01 | Strategia rzetelnej bazy gatunków — dyskusja + plan | P0 |
| UX-01 | Docelowy design system Flutter: komponenty, typografia, spacing, stany | P0 |
| UX-02 | Zaprojektować Home z wyszukiwarką gatunków, skrótami i ostatnimi aktywnościami | P0 |
| UX-03 | Zaprojektować profil i ustawienia (publiczny profil vs prywatne ustawienia) | P1 |
| UX-04 | Opisać animacje: przejścia, bottom sheets, karty, loading/skeleton | P1 |
| FLUT-04 | Uporządkować routing Flutter i nawigację bez osobnego ekranu wyszukiwarki | P0 |
| FLUT-02 | Przenieść Auth + gate weryfikacji email do Flutter | P0 |
| FLUT-03A | Flutter: Home + wyszukiwanie gatunków + karta gatunku | P0 |
| FLUT-03B | Flutter: Moja hodowla (lista, dodawanie, edycja, media, isPublic) | P1 |
| FLUT-03C | Flutter: Rozmnażanie (statusy, daty, media, filtry) | P1 |
| FLUT-03D | Flutter: Społeczność (publicAnimals, profile publiczne) | P1 |
| FLUT-03E | Flutter: Profil i ustawienia (motyw, konto, przypomnienia) | P1 |
| DATA-02 | Stworzyć 1 pełny gatunek wzorcowy z kompletem danych i legalnym zdjęciem | P0 |
| DATA-03 | Uporządkować `species.cites` jako boolean i format pól CSV/Firestore | P1 |
| LEGAL-01 | Polityka prywatności (RODO) | P1 |
| LEGAL-02 | Regulamin użytkowania | P1 |
| LEGAL-03 | Usuwanie konta (UI + kasowanie danych) | P1 |
| FB-01 | Deploy `firestore.rules`, `storage.rules`, indeksów | P1 |
| FB-02 | Migracja `breedingPostsFallback` do `users/{uid}/breedingPosts` i usunięcie fallbacku | P1 |
| MEDIA-03 | Kompresja zdjęć/filmów i limity rozmiaru przed uploadem | P1 |
| COST-01 | Paginacja feedów, cache obrazów i limity zapytań | P1 |
| TEST-01 | Flutter smoke tests: start, routing, theme, podstawowe ekrany | P1 |
| TEST-02 | Checklist testów na emulatorze i fizycznym Androidzie | P1 |
| IOS-01 | Plan testowania iOS: Mac/Xcode, Apple Developer, Firebase iOS app | P1 |
| IOS-02 | Pierwszy build iOS na Macu lub usłudze cloud Mac | P2 |
| UI-01 | Nie rozwijać dalej dużych modułów w Kotlin; traktować Android Compose jako referencję | P1 |
| BREED-02 | Filtrowanie rozmnażania po statusie, datach i mediach | P2 |
| REM-01 | Flutter: przypomnienia lokalne + później FCM | P2 |
| SET-01 | Rozszerzyć ustawienia: konto, prywatność, motyw, powiadomienia, usuwanie konta | P2 |
| SOC-01 | Krótkie wpisy i pytania społeczności (post/question) | P2 |
| MOD-01 | Role moderatorów i panel/proces zatwierdzania danych gatunków | P2 |

---

## Done

| ID | Zadanie | Data |
|----|---------|------|
| MEDIA-01 | Cloudinary Free + unsigned upload preset `spiderzone_dev_unsigned` | 2026-06-18 |
| BREED-01 | `isPublic` na wpisach rozmnażania (prywatne/publiczne) | 2026-06-17 |
| TECH-01 | Potwierdzic i utrwalić decyzję Flutter jako główny klient | 2026-06-17 |
| — | Dokumentacja w `docs/` (katalog tematyczny) | 2026-04-26 |
| — | Zakładka Rozmnażanie zamiast Przypomnienia | 2026-04-26 |
| — | Przypomnienia w ustawieniach profilu | 2026-04-26 |

---

## Zasada

Najpierw aktualizuj Notion, potem ten plik.