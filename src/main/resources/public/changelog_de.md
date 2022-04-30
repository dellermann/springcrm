## 2.1.10

* Feature: laufende Nummer zu Eingangsrechnungen hinzugefügt
* Feature: Seitenauswähler hinzugefügt
* Feature: Jahreswähler für Angebote, Verkaufsbestellungen, Rechnungen, 
  Mahnungen, Gutschriften und Einkaufsrechnungen implementiert
* Fehlerbehebung: Ladegeschwindigkeit des Rechnungsausgangsbuchs erhöht
* Fehlerbehebung: Rechnungspositionen verschwanden beim Erstellen einer Mahnung
* Fehlerbehebung: Adressfelder konnten aufgrund eines JavaScript-Fehlers nicht
  von links nach rechts oder umgekehrt kopiert werden
* Autovervollständigung für Datumsfelder deaktiviert

## 2.1.8

* Aktualisierung auf Grails 3.2.11, Groovy 2.4.15 und Gradle 3.5
* Fehlerbehebung: Suchfunktion war für Nicht-Administratoren nicht verfügbar
* Fehlerbehebung: Laufende Nummern wurden nicht korrekt berechnet

## 2.1.7

* Erinnerung für Administratoren, das Nummernschema zu ändern, wenn es nicht zum
  aktuellen Jahr passt
  ([Problem #79](https://github.com/dellermann/springcrm/issues/79))
* Behebung des Fehlers, dass Formulare in Microsoft Edge nicht gespeichert
  werden konnten
  ([Problem #94](https://github.com/dellermann/springcrm/issues/94))
* Nummernschemata werden nun in der richtigen Reihenfolge angezeigt
* Unterstützung für Aufruf der Webanwendung unter Unix und Linux

## 2.1.6

* Unterstützung für Helpdesks für Endbenutzer
* Speichern- und Abbruch-Schaltflächen unterhalb von Formularen hinzugefügt
  ([Problem #58](https://github.com/dellermann/springcrm/issues/58))
* Abschicken von Formularen funktionierte nicht in Microsoft Edge
  ([Problem #94](https://github.com/dellermann/springcrm/issues/94))

[comment]: STOP

## 2.1.5

* Behebung der folgenden Probleme: [#19](https://github.com/dellermann/springcrm/issues/19), [#63](https://github.com/dellermann/springcrm/issues/63), [#88](https://github.com/dellermann/springcrm/issues/88), [#90](https://github.com/dellermann/springcrm/issues/90), [#91](https://github.com/dellermann/springcrm/issues/91) und [#93](https://github.com/dellermann/springcrm/issues/93)
* Fehlerbehebung: Inhalt wurde gelöscht nachdem bestimmte Felder mit der 
  Tabulatortaste verlassen wurden.

## 2.1.4

* Umsatzgesamtübersicht, optional filterbar anhand des Jahres.

## 2.1.3

* Einstellungen für die Liste der unbezahlten Rechnungen: Mindestwert für noch
  offenen Betrag, Sortierkriterium und Reihenfolge, Anzahl der Einträge in der
  Liste.
* Neues Panel mit aktiven Projekten.
* Textumbruch und falsche vertikale Ausrichtung im Mehrwertsteuerrechner
  behoben.

## 2.1.2

* Wiedereinbau der Suchfunktionalität

## 2.1.1

* Positive and negative Bewertungen von Organisationen und Personen
* Textbausteine für die Verwendung in Eingabefeldern
* Einstellbare Anzahl von Einträgen pro Seite in Listenansichten

## 2.1.0

* Aktualisierung auf Grails 3
* Aktualisierung des Unterbaus wie Bootstrap und FontAwesome
* Viele Fehlerbehebungen und Codeoptimierungen

## 2.0.20

* Fehler bei Darstellung der Änderungshistorie unter Windows
  ([Problem #84](https://github.com/dellermann/springcrm/issues/84))
* Fehler mit verwaister „Nächste“-Schaltfläche bei Listen mit einem Eintrag
  ([Problem #5](https://github.com/dellermann/springcrm/issues/5))

## 2.0.19 (RC 2)

* Anzeige der letzten Änderungen bei jeder neuen Version
  ([Problem #80](https://github.com/dellermann/springcrm/issues/80))
* Anzeige der Zahlungsdatums bei Rechnungen und Mahnungen
  ([Problem #72](https://github.com/dellermann/springcrm/issues/72))
* Fehlerbehebung: Eintragung des Versand- oder Zahlungsdatums in Rechnungen,
  Mahnungen und Gutschriften schaltete nicht auf "versendet" bzw. "bezahlt" um
  ([Problem #82](https://github.com/dellermann/springcrm/issues/82))

## 2.0.18 (RC 1)

* Hinzufügen von neuen Einträgen in Fakturavorgängen scrollt jetzt den neuen
  Eintrag in die Mitte
* Fehlerbehebung: größere Präzision beim Rechnen mit Währungsangaben
* Fehlerbehebung: Authentifizierungstokens bei Google-Synchronisation wurde
  nicht richtig aufgefrischt
* Fehlerbehebungen in Formular für Anrufe und in Adressfeldern

## 2.0.17

* Umsatzanzeige für Kunden
  ([Problem #71](https://github.com/dellermann/springcrm/issues/71))
* Fehlerbehebung: falsche Anzeige von Typen bei leeren Listen
  ([Problem #81](https://github.com/dellermann/springcrm/issues/81))
* Fehlerbehebung im Installationsbereich
