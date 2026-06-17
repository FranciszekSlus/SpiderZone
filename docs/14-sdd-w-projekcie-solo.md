# Czy SDD jest potrzebne w projekcie jednoosobowym?

Krótko: **tak, ale lekkie SDD**.

## Kiedy SDD ma sens solo

SDD (Specification-Driven Development) pomaga nawet jednej osobie, bo:

- porządkuje decyzje zanim wejdziesz w kod,
- zmniejsza ryzyko przepisywania feature po tygodniu,
- ułatwia pracę z AI (Cursor), bo model ma jasny cel i kryteria,
- dokumentuje „dlaczego”, nie tylko „co jest w kodzie”.

## Kiedy pełne SDD jest przesadą

Przy małych poprawkach (copy, kolor przycisku, 1 bugfix) pisanie pełnej specyfikacji to strata czasu.

## Rekomendacja dla SpiderZone

W projekcie solo stosuj **mini-SDD** dla średnich i dużych funkcji.

### Minimalny szablon mini-SDD (1 strona)

1. Cel biznesowy (po co)
2. Zakres (co wchodzi / co nie)
3. UX flow (ekrany + nawigacja)
4. Model danych (kolekcje, pola, reguły)
5. Kryteria ukończenia (Definition of Done)
6. Ryzyka i koszty (Spark limits, media, legal)

## Proponowane użycie w SpiderZone

- **Wymagane mini-SDD:** rozmnażanie (statusy + prywatność), komentarze, moderacja, usuwanie konta, migracja do Flutter.
- **Niewymagane mini-SDD:** drobne poprawki UI i bugfixy.

## Praktyczna zasada 30 minut

Jeśli nie umiesz opisać feature w 30 minut w mini-SDD, to znak, że zakres jest za duży i trzeba go podzielić.

## Podsumowanie

- Solo projekt: **SDD tak**, ale **lekkie i pragmatyczne**.
- Dla SpiderZone to da większą przewidywalność i mniej chaosu, zwłaszcza przy migracji Flutter + Firebase.