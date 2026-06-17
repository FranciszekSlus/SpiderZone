# Architektura danych (Firebase + Cloudinary)

## Kolekcje Firestore

```text
species/                    # baza gatunkow (read: wszyscy, write: zablokowany z klienta)
publicProfiles/{uid}        # profil publiczny hodowcy
publicAnimals/{animalId}    # publiczne zwierzeta -> feed spolecznosci
breedingPosts/{postId}      # publiczny feed rozmnazania (tylko isPublic=true)

users/{uid}                 # profil prywatny
users/{uid}/animals/        # hodowla
users/{uid}/reminders/      # przypomnienia
users/{uid}/breedingPosts/  # wlasne wpisy rozmnazania, takze prywatne
users/{uid}.breedingPostsFallback # tymczasowy fallback Android, do migracji/usuniecia
users/{uid}/molt_history/   # planowane
users/{uid}/growth_logs/    # planowane
```

## Wybrane pola modelu

- `species.cites` — docelowo boolean (`true`/`false`), w UI jako Tak/Nie.
- `users/{uid}/breedingPosts/{postId}.isPublic` — boolean; `false` oznacza wpis prywatny widoczny tylko dla autora.
- `breedingPosts/{postId}` — kopia wpisu tylko dla publicznych wpisow (`isPublic=true`).
- `users/{uid}.breedingPostsFallback` — tymczasowy fallback, gdy aktywne reguly Firestore blokowaly subkolekcje; nie jest modelem docelowym dla Fluttera.
- `breedingPosts.status` — `planned`, `in_progress`, `success`, `failed`.
- `breedingPosts.startDate`, `breedingPosts.cocoonDate` — daty tekstowe na start (format `YYYY-MM-DD`).
- `breedingPosts.photoUrls`, `breedingPosts.videoUrls` — media wpisu.
- `publicAnimals.isPublic` / `users/{uid}/animals/{id}.isPublic` — prywatne/publiczne zwierzęta.

## Media (Cloudinary Free)

- Docelowo zdjecia i filmy trzymamy poza Firebase Storage (Cloudinary Free).
- W Firestore zapisujemy URL-e mediow (photoUrls, videoUrls).
- Kompresja i resize po stronie aplikacji przed uploadem.
- Flutter: upload przez unsigned upload preset i multipart POST do `https://api.cloudinary.com/v1_1/<cloud_name>/upload`.
- W aplikacji nie zapisujemy Cloudinary API secret.
- Firebase Storage zostaje tylko jako fallback/test historyczny Androida.

## Reguly (skrot)

- users/{uid}/** — tylko wlasciciel.
- species — odczyt publiczny.
- publicAnimals, publicProfiles — odczyt dla zalogowanych; zapis tylko wlasciciel danych.
- breedingPosts — odczyt tylko wpisow publicznych; zapis tylko wlasciciel danych i tylko dla `isPublic=true`.
- users/{uid}/breedingPosts — odczyt/zapis tylko wlasciciel.

Plik: firestore.rules.

> storage.rules zostaje w repo jako fallback, gdyby byl powrot do Firebase Storage.

## Indeksy

- breedingPosts — sort po createdAt DESC (firestore.indexes.json).

## Optymalizacja kosztow

- Paginacja feedow (spolecznosc, rozmnazanie).
- Kompresja zdjec przed uploadem.
- Cache CSV gatunkow lokalnie.
- Realtime listenery tylko na aktywnych ekranach.
- Limity zapytan (limit(60) itd.).

## Do zrobienia

- [ ] Opublikowac reguly Firestore.
- [ ] Wdrozyc indeksy (firebase deploy --only firestore:indexes lub link z bledu).
- [ ] Wlaczyc Auth Email/Password i Firestore.
- [ ] Zalozyc konto Cloudinary i przygotowac unsigned upload preset.
