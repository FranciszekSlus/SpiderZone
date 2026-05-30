# Release checklist MVP

## Android Internal Testing

- [ ] Ustaw `applicationId` produkcyjny.
- [ ] Dodaj podpisywanie aplikacji (keystore).
- [ ] Podmien ikony i splash.
- [ ] Wlacz Crashlytics (opcjonalnie, gdy przejdziesz na plan platny).
- [ ] Sprawdz logowanie, dodawanie zwierzecia i przypomnienia na fizycznym urzadzeniu.
- [ ] Zbuduj pakiet `AAB` i wyslij do Google Play Internal Testing.

## iOS (pozniej)

- [ ] Przenies kod do Flutter lub utrzymuj osobna implementacje iOS.
- [ ] Dodaj APNs i uprawnienia powiadomien.
- [ ] Wyslij build do TestFlight.
