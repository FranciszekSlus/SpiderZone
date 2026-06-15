# Śledzenie zadań (bez GitHub Issues)

## Dlaczego nie GitHub Issues

Dla solo projektu z Cursorem Issues często są niewygodne: słaba tablica, brak kontekstu dla AI, rozłączenie od kodu i dokumentacji.

## Rekomendacja dla SpiderZone

### Warstwa 1 — źródło prawdy w repo (dla Ciebie i Cursor)

| Plik | Rola |
|------|------|
| [tasks/TASKS.md](tasks/TASKS.md) | Aktywne zadania (Kanban w Markdown) |
| [11-zadania-stan-projektu.md](11-zadania-stan-projektu.md) | Pełny audyt: co mamy / czego brakuje |
| [future/backlog-produktowy.md](future/backlog-produktowy.md) | Pomysły na później |

**Cursor widzi pliki w repo** — w czacie możesz pisać: „weź zadanie BREED-01 z TASKS.md”.

### Warstwa 2 — wizualna tablica (opcjonalnie)

Wybierz **jedno** narzędzie zewnętrzne tylko jeśli chcesz Kanban na telefonie / przeglądarce.

| Narzędzie | Darmowy plan | Dla solo | Integracja z Cursor |
|-----------|--------------|----------|---------------------|
| **Notion** | Tak (limit) | ⭐⭐⭐⭐⭐ | Wklej link do bazy w regule Cursor; eksport → Markdown |
| **ClickUp** | Tak | ⭐⭐⭐⭐ | Brak natywnej; ręczna synchronizacja z TASKS.md |
| **Trello** | Tak | ⭐⭐⭐ | Proste; duplikacja ręczna |
| **Linear** | Ograniczony free | ⭐⭐⭐ | Świetne API, ale overkill na start |
| **Todoist** | Tak | ⭐⭐⭐ | Osobiste taski, słabe dla „projektu software” |

**Rekomendacja:** zostaw **TASKS.md w repo** + opcjonalnie **Notion** (jedna strona „SpiderZone Sprint”) jeśli lubisz UI.

---

## Workflow z Cursorem (ręczna konfiguracja)

### Krok 1 — reguła w Cursor

Utwórz plik `.cursor/rules/spiderzone-tasks.mdc` (lub regułę w ustawieniach):

```markdown
Przed rozpoczęciem pracy sprawdź docs/tasks/TASKS.md.
Po zakończeniu zadania przenieś je do sekcji Done w TASKS.md.
Przy nowym zadaniu dopisz ID (np. BREED-01) i link do docs/ jeśli dotyczy.
```

### Krok 2 — jak zgłaszać pracę AI

Przykłady promptów:

- „Zrób zadanie BREED-01 z `docs/tasks/TASKS.md`”
- „Zaktualizuj TASKS.md — LEGAL-01 ukończone”
- „Dodaj do TASKS.md zadanie: kompresja zdjęć przed uploadem”

### Krok 3 — synchronizacja z Notion (opcjonalnie)

1. Załóż stronę Notion „SpiderZone”.
2. Tabela: `ID | Tytuł | Status | Priorytet | Notatki`.
3. Raz w tygodniu skopiuj stan z `TASKS.md` (lub odwrotnie).
4. W Notion trzymaj **link do pliku GitHub** dla kontekstu.

Cursor **nie synchronizuje** Notion automatycznie — to świadomy kompromis.

### Krok 4 — co NIE robić

- Nie trzymaj zadań tylko w Issues jeśli ich nie używasz.
- Nie duplikuj w 3 miejscach bez synchronizacji (wybierz repo + max 1 zewnętrzne).

---

## Konwencja ID zadań

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

---

## Kiedy przejść na coś innego

- Zespół 2+ osób → Linear lub ClickUp z prawdziwym workflow.
- Płatny produkt z roadmapą publiczną → GitHub Issues / Discussions może wrócić.

Do tego czasu: **`docs/tasks/TASKS.md` + Cursor rules**.
