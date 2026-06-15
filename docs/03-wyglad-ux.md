# Wygląd i UX

## Kierunek wizualny

- **Dark-first**, klimat terrarium: natura + noc + egzotyka.
- Unikać jasnego, „szpitalnego” UI.
- Karty z dużymi zdjęciami, tagi (łatwy, agresywny…).

## Paleta

| Token | Hex |
|-------|-----|
| Tło | `#121212` |
| Karty | `#1E1E1E` |
| Primary | `#4CAF50` |
| Primary hover | `#81C784` |
| Akcent pomarańczowy | `#FF7043` |
| Tekst główny | `#E0E0E0` |
| Tekst drugorzędny | `#A0A0A0` |

Implementacja: `AppTheme.kt` (Android), `flutter_app/lib/core/theme/` (Flutter).

## Komponenty

- Karty: radius 12–16 px, lekki cień, scale ~1.03 przy interakcji.
- Przyciski: primary (zielony), secondary (obramowanie), danger (czerwony).
- Animacje: 200–300 ms, fade + slide między ekranami.
- Modal dodawania zwierzęcia: fade + lekki zoom (docelowo ujednolicić).

## Responsywność

- Mobile-first, bottom navigation.
- Tablet: docelowo sidebar lub większa siatka kart.

## Problem otwarty: „dobra konstrukcja UI”

Obecnie duża część UI jest w jednym pliku (`MainActivity.kt` ~3800 linii). Docelowo:

- Podział na moduły / pliki per feature.
- Wspólna biblioteka komponentów (`SzCard`, pola formularzy).
- Spójne stany ładowania, błędów i pustych list.

Patrz zadania w [11-zadania-stan-projektu.md](11-zadania-stan-projektu.md).
