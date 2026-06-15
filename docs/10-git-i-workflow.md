# Git i workflow

## Branchy

```text
main              # stabilna wersja
feature/nazwa     # nowa funkcja
fix/nazwa         # poprawka błędu
docs/nazwa        # tylko dokumentacja
```

Opcjonalnie przy większym zespole: `develop` jako integracja przed `main`.

## Commity

```
feat: dodaj widocznosc prywatna/publiczna w rozmnazaniu
fix: crash Firebase init
docs: rozdziel dokumentacje na katalog docs/
chore: aktualizacja zaleznosci
```

## Sekrety

- `google-services.json` — repo prywatne OK; publiczne → `.gitignore`.
- Nie commituj haseł, kluczy API, plików `.env`.

## SDD (Specification-Driven Development)

Przy większych feature:

1. Utwórz `docs/sdd/NNN-nazwa.md` (cel, ekrany, model danych).
2. Akceptacja właściciela.
3. Branch `feature/NNN-nazwa`.
4. PR → merge do `main`.

## Release

Tagi: `v1.0.0`, `v1.1.0`. Checklist: [`RELEASE_CHECKLIST.md`](../RELEASE_CHECKLIST.md).
