# Moderacja i bezpieczeństwo

## MVP (obecnie)

- Społeczność i rozmnażanie tylko dla **zalogowanych**.
- Weryfikacja **email** przed pełnym dostępem.
- Firestore Rules — użytkownik zapisuje tylko własne dane.
- Brak komentarzy = mniejsze ryzyko treści szkodliwych.

## Docelowo

| Mechanizm | Opis |
|-----------|------|
| Role | `user`, `moderator`, `admin` (Custom Claims lub pole w profilu) |
| Komentarze | Rate limiting, zgłoszenia (`reports`) |
| Moderatorzy | Usuwanie komentarzy, edycja `species`, ban |
| Upload | Limit rozmiaru, kompresja, opcjonalnie Cloud Functions |

## Limity techniczne (propozycja)

- Max ~2 MB na zdjęcie po kompresji.
- Max 10 zdjęć na zwierzę.
- Throttling zapisów po stronie klienta.

## Treści zakazane (regulamin)

- Treści nielegalne, nękanie, spam.
- Sprzedaż zwierząt bez zgodności z prawem (szczegóły w regulaminie — do napisania).

## CITES / gatunki chronione

**Do przedyskutowania** — czy oznaczać lub blokować gatunki wymagające pozwoleń.
