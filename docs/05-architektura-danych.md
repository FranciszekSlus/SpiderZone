# Architektura danych (Firebase)

## Kolekcje Firestore

```text
species/                    # baza gatunków (read: wszyscy, write: zablokowany z klienta)
publicProfiles/{uid}        # profil publiczny hodowcy
publicAnimals/{animalId}    # publiczne zwierzęta → feed społeczności
breedingPosts/{postId}      # wpisy rozmnażania (plan: pole isPublic)

users/{uid}                 # profil prywatny
users/{uid}/animals/        # hodowla
users/{uid}/reminders/      # przypomnienia
users/{uid}/molt_history/   # planowane
users/{uid}/growth_logs/    # planowane
```

## Storage

Ścieżki typu: `users/{uid}/animals/{animalId}/photos|videos/{file}`

## Reguły (skrót)

- `users/{uid}/**` — tylko właściciel.
- `species` — odczyt publiczny.
- `publicAnimals`, `publicProfiles`, `breedingPosts` — odczyt dla zalogowanych; zapis tylko właściciel danych.

Pliki: [`firestore.rules`](../firestore.rules), [`storage.rules`](../storage.rules).

## Indeksy

- `breedingPosts` — sort po `createdAt` DESC (`firestore.indexes.json`).

## Optymalizacja kosztów (Spark)

- Paginacja feedów (społeczność, rozmnażanie).
- Kompresja zdjęć przed uploadem.
- Cache CSV gatunków lokalnie.
- Realtime listenery tylko na aktywnych ekranach.
- Limity zapytań (`limit(60)` itd.).

## Do zrobienia w Firebase Console

- [ ] Opublikować reguły Firestore i Storage.
- [ ] Wdrożyć indeksy (`firebase deploy --only firestore:indexes` lub link z błędu).
- [ ] Włączyć Auth Email/Password, Firestore, Storage.
