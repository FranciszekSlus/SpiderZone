# Wygląd i UX

## Kierunek wizualny

- **Dark-first**, klimat terrarium: natura + noc + egzotyka.
- Unikać jasnego, „szpitalnego” UI.
- Karty z dużymi zdjęciami, tagi (łatwy, agresywny…).

## Paleta

| Token | Hex |
|-------|-----|
| Tło | `#0E1411` |
| Karty | `#17211B` |
| Karty / warstwa 2 | `#203027` |
| Primary | `#6FBF73` |
| Primary hover | `#A5D6A7` |
| Akcent bursztynowy | `#D98E32` |
| Danger | `#D65A4A` |
| Tekst główny | `#E7EEE9` |
| Tekst drugorzędny | `#9DAEA4` |

Implementacja Android: `app/src/main/java/com/example/spiderzone/SpiderZoneColors.kt` (paleta) + `AppTheme.kt` (Material theme).
Implementacja Flutter: docelowo `flutter_app/lib/core/theme/`.

## Komponenty

- Karty: radius 12–16 px, lekki cień, scale ~1.03 przy interakcji.
- Przyciski: primary (zielony), secondary (obramowanie), danger (czerwony).
- Animacje: 200–300 ms, fade + slide między ekranami.
- Modal dodawania zwierzęcia: fade + lekki zoom (docelowo ujednolicić).

## Główne ekrany — kierunek

| Ekran | Kierunek UX |
|-------|-------------|
| Home | Wyszukiwarka gatunków, skróty, ostatnie zwierzęta/wpisy, puste stany |
| Gatunek | Duże zdjęcie, nazwy, tagi, parametry w kartach, źródła/weryfikacja |
| Hodowla | Karty zwierząt ze zdjęciem, szybkie filtry, CTA „Dodaj zwierzę” |
| Rozmnażanie | Timeline/karty prób, statusy kolorami, filtry po statusie i gatunku |
| Społeczność | Feed kart publicznych zwierząt i później pytania/posty |
| Profil | Publiczny profil hodowcy + prywatne ustawienia rozdzielone wizualnie |
| Ustawienia | Konto, motyw, przypomnienia, prywatność, usuwanie konta |

## Animacje

- Zmiana zakładek: 200–250 ms fade + lekki slide.
- Otwieranie formularzy: bottom sheet lub pełna karta z 250 ms fade/scale.
- Karty na listach: delikatny press/scale tylko przy interakcji.
- Snackbar/toasty: floating, bez zasłaniania bottom navigation.
- Skeleton/loading: shimmer tylko dla feedów z obrazami, zwykłe progress dla prostych ekranów.

## Responsywność

- Mobile-first, bottom navigation.
- Tablet: docelowo sidebar lub większa siatka kart.

## Problem otwarty: „dobra konstrukcja UI”

Obecnie duża część UI jest w jednym pliku (`MainActivity.kt` ~3800 linii). Docelowo:

- Podział na moduły / pliki per feature.
- Wspólna biblioteka komponentów (`SzCard`, pola formularzy).
- Spójne stany ładowania, błędów i pustych list.

Patrz zadania w [11-zadania-stan-projektu.md](11-zadania-stan-projektu.md).
