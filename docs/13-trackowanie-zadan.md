# Śledzenie zadań (Notion + kopia w repo)

## Decyzja

- **Główne narzędzie:** Notion
- **Baza:** `SpiderZone Tasks`
- **URL:** https://app.notion.com/p/5bca70934bbf471d8592f55823020b5d
- **Kopia techniczna dla Cursor:** `docs/tasks/TASKS.md`

## Dlaczego nie GitHub Issues

Dla tego projektu solo Issues były mało wygodne: słaba czytelność priorytetów i kolejności oraz słaby przepływ pracy z AI.

## Jak pracujemy teraz

### Warstwa 1 — źródło prawdy: Notion

W Notion utrzymujemy:

- status (`Todo`, `In Progress`, `Blocked`, `Done`),
- priorytet (`P0`..`P3`),
- kolejność (`Order`),
- obszar (`Data`, `UI/UX`, `Legal`, `Breeding`, `Firebase`, `Tech Decision`),
- notatki wykonawcze.

### Warstwa 2 — mirror w repo

`docs/tasks/TASKS.md` jest kopią roboczą dla Cursor i fallbackiem offline.

## Workflow z Cursor

1. Przed pracą sprawdź Notion i `docs/tasks/TASKS.md`.
2. W promptach używaj ID zadania, np. `BREED-01`.
3. Po ukończeniu aktualizuj Notion, a potem mirror w repo.

Przykład:

```text
Zrób zadanie BREED-01 z Notion (SpiderZone Tasks) i zaktualizuj docs/tasks/TASKS.md.
```

## Konwencja ID

| Prefiks | Obszar |
|---------|--------|
| `DATA-` | Baza gatunków |
| `UI-` | Wygląd / refaktoryzacja |
| `LEGAL-` | Prawo, RODO |
| `BREED-` | Rozmnażanie |
| `REM-` | Przypomnienia |
| `SOC-` | Społeczność |
| `FB-` | Firebase / infra |
| `TECH-` | Decyzje technologiczne |

## Zasada spójności

Nie duplikuj niezależnie w wielu miejscach.

- Najpierw aktualizuj **Notion**,
- potem synchronizuj `docs/tasks/TASKS.md`.