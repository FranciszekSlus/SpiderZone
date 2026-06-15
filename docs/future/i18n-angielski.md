# Angielski (EN) — przyszły etap

## Decyzja

MVP tylko po **polsku**. Angielski po stabilnym release PL.

## Kiedy zacząć

- Po publikacji MVP na Google Play (PL).
- Gdy pojawią się użytkownicy z zagranicy lub plan na iOS globalnie.

## Jak wdrożyć (Flutter)

- `flutter_localizations` + `intl`
- Pliki `lib/l10n/app_pl.arb`, `app_en.arb`
- Wszystkie stringi z UI do ARB (duży refactor)

## Jak wdrożyć (Android Compose)

- `res/values/strings.xml` (PL)
- `res/values-en/strings.xml` (EN)
- Wyciągnięcie hardcoded stringów z `MainActivity.kt` (wymaga refaktoryzacji UI-01)

## Szacunek pracy

- Przy dobrej strukturze: 2–5 dni na ekstrakcję stringów + tłumaczenie.
- Przy monolicie `MainActivity.kt`: więcej — kolejny argument za refaktoryzacją najpierw.
