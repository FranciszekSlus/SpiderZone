# SpiderZone — dokumentacja projektu

> Katalog wiedzy o produkcie i technologii. Asystent AI (Cursor) korzysta z tych plików przed większymi zmianami.

**Ostatnia aktualizacja:** 2026-06-16
**Właściciel:** Franczesco
**Firebase:** `spiderzone-d112d`
**Nazwa produktu:** SpiderZone (na razie bez rebrandu)

---

## Spis treści

| Plik | Opis |
|------|------|
| [01-wizja-i-produkt.md](01-wizja-i-produkt.md) | Założenia, grupa docelowa, metafora produktu |
| [02-moduly-aplikacji.md](02-moduly-aplikacji.md) | Jak działają moduły (gatunki, hodowla, społeczność…) |
| [03-wyglad-ux.md](03-wyglad-ux.md) | Motyw, kolory, komponenty, nawigacja |
| [04-technologie.md](04-technologie.md) | Stack, narzędzia, rekomendacje |
| [05-architektura-danych.md](05-architektura-danych.md) | Firestore, Storage, reguły |
| [06-moderacja-i-bezpieczenstwo.md](06-moderacja-i-bezpieczenstwo.md) | Treści, role, limity |
| [07-problemy-i-wyzwania.md](07-problemy-i-wyzwania.md) | Baza gatunków, prawo, koszty |
| [08-decyzje-produktowe.md](08-decyzje-produktowe.md) | Ustalone decyzje (źródło prawdy) |
| [09-flutter-vs-android.md](09-flutter-vs-android.md) | Za i przeciw — wybór technologii mobilnej |
| [10-git-i-workflow.md](10-git-i-workflow.md) | Branchy, commity, SDD |
| [11-zadania-stan-projektu.md](11-zadania-stan-projektu.md) | Co mamy / czego brakuje / problemy jako zadania |
| [12-dzialanie-techniczne.md](12-dzialanie-techniczne.md) | Jak działa technicznie: sekrety, Firebase, build, przepływy |
| [13-trackowanie-zadan.md](13-trackowanie-zadan.md) | Notion-first: trackowanie zadań + sync do repo |
| [14-sdd-w-projekcie-solo.md](14-sdd-w-projekcie-solo.md) | Czy SDD ma sens w projekcie jednoosobowym |
| [15-struktura-aplikacji.md](15-struktura-aplikacji.md) | Gdzie znajdują się główne pliki aplikacji, motywu, danych i reguł |
| [16-audyt-i-roadmapa-flutter-first.md](16-audyt-i-roadmapa-flutter-first.md) | Audyt braków i szczegółowa roadmapa Flutter-first |
| [tasks/TASKS.md](tasks/TASKS.md) | Aktywna kopia zadań w repo (mirror) |
| [future/backlog-produktowy.md](future/backlog-produktowy.md) | Funkcje na później (czaty, eventy…) |
| [future/i18n-angielski.md](future/i18n-angielski.md) | Wielojęzyczność — etap po MVP PL |

---

## Szybkie linki w repo

- [`README.md`](../README.md) — build i uruchomienie
- [`firestore.rules`](../firestore.rules) — reguły bazy
- [`flutter_app/README.md`](../flutter_app/README.md) — setup Flutter
- [`RELEASE_CHECKLIST.md`](../RELEASE_CHECKLIST.md) — publikacja