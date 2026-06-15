# Flutter vs Android (Kotlin Compose) — za i przeciw

> **Status decyzji:** nierozstrzygnięte (2026-04-26).  
> **Kontekst:** działająca aplikacja w Kotlin Compose; szkielet Flutter w `flutter_app/`.

---

## Podsumowanie jednym zdaniem

- **Zostań przy Kotlin Compose**, jeśli priorytetem jest **szybki MVP tylko na Android** i nie chcesz przepisywać kodu.
- **Przejdź na Flutter**, jeśli priorytetem jest **iOS + jeden codebase** i akceptujesz migrację / okres równoległego utrzymania.

---

## Kotlin + Jetpack Compose (obecny stan)

### Za ✅

| Argument | Szczegóły |
|----------|-----------|
| **Już działa** | Auth, hodowla, społeczność, rozmnażanie, gatunki, media — setki godzin kodu gotowe |
| **Natywne API Android** | Powiadomienia (`AlarmManager`), Storage, uprawnienia — bez mostków |
| **Wydajność** | Pełna natywność, brak warstwy Flutter engine |
| **Android Studio** | Debugging, profiler, emulator — dojrzałe narzędzia |
| **Mniejsze ryzyko migracji** | Nie tracisz postępu na przepisywaniu |
| **Compose** | Nowoczesny UI, zbliżony model do Flutter widgetów |

### Przeciw ❌

| Argument | Szczegóły |
|----------|-----------|
| **Tylko Android** | iOS wymaga osobnej aplikacji (Swift/SwiftUI) lub Flutter |
| **Monolit w kodzie** | `MainActivity.kt` bardzo duży — trzeba refaktoryzować |
| **Dwa stacki w repo** | Jeśli dorobisz Flutter, przez jakiś czas masz dublowanie |
| **Koszt iOS później** | Apple Developer 99 USD/rok + osobny development |
| **Zatrudnienie** | Trudniej znaleźć jednego deva na Android + iOS |

---

## Flutter

### Za ✅

| Argument | Szczegóły |
|----------|-----------|
| **Jeden kod → Android + iOS** | Zgodne z pierwotnym planem projektu |
| **Spójny UI** | Ten sam wygląd na platformach |
| **Szybki prototyp UI** | Hot reload, bogaty ekosystem widgetów |
| **Firebase** | Oficjalne pluginy (auth, firestore, storage) |
| **Struktura w repo** | `flutter_app/` już ma feature-first layout |
| **Długoterminowo taniej** | Jedna aplikacja zamiast dwóch natywnych |

### Przeciw ❌

| Argument | Szczegóły |
|----------|-----------|
| **Migracja od zera** | Trzeba przenieść logikę z Kotlin (tygodnie pracy) |
| **Flutter nie skonfigurowany** | Na dev machine brak `flutter` w PATH — dodatkowy setup |
| **Rozmiar APK** | Większy niż czysty natywny |
| **Natywne edge case** | Czasem wymaga platform channels (notyfikacje, tło) |
| **Dwa produkty równolegle** | Ryzyko rozjazdu funkcji Android vs Flutter |
| **iOS nadal kosztuje** | Mac do buildów, Apple Developer Program |

---

## Porównanie pod kątem SpiderZone

| Kryterium | Kotlin Compose | Flutter |
|-----------|----------------|---------|
| Czas do kolejnego release na Android | ⭐⭐⭐⭐⭐ | ⭐⭐ |
| iOS w przyszłości | ⭐ | ⭐⭐⭐⭐⭐ |
| Jakość UI / animacje | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Firebase | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Utrzymanie solo | ⭐⭐⭐⭐ (1 platforma) | ⭐⭐⭐⭐⭐ (2 platformy) |
| Stan obecny repozytorium | ⭐⭐⭐⭐⭐ | ⭐⭐ |

---

## Rekomendacja robocza (do akceptacji)

### Wariant A — „Android first” (mniej ryzyka)

1. Dokończ MVP w **Kotlin Compose**.
2. Refaktoryzuj `MainActivity.kt` na moduły.
3. Opublikuj na Google Play.
4. Gdy będzie traction i budżet na iOS → start migracji Flutter lub osobny iOS.

### Wariant B — „Cross-platform now” (zgodnie z pierwotnym planem)

1. Zainstaluj Flutter + `flutterfire configure`.
2. Migruj moduły po kolei: auth → gatunki → hodowla → społeczność.
3. Android Kotlin utrzymuj tylko do momentu parity, potem wygaszaj.

### Wariant C — hybryda (niezalecane długoterminowo)

Utrzymuj oba — tylko jeśli bardzo krótki okres przejściowy (max 1–2 miesiące).

---

## Co zrobić, żeby podjąć decyzję

1. Czy **iOS w ciągu 12 miesięcy** jest must-have? (Tak → Flutter; Nie → Kotlin)
2. Ile czasu tygodniowo na development?
3. Czy masz dostęp do Maca pod iOS build?

**Po odpowiedzi:** zaktualizuj [08-decyzje-produktowe.md](08-decyzje-produktowe.md) i [11-zadania-stan-projektu.md](11-zadania-stan-projektu.md).
