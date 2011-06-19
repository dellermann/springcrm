<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="terms-and-conditions">
		<xsl:call-template name="terms-and-conditions-services"/>
		<xsl:call-template name="terms-and-conditions-wares"/>
	</xsl:template>
  
  <xsl:template name="terms-and-conditions-services">
    <fo:page-sequence master-reference="terms-and-conditions" language="de"
                      hyphenate="true">
      <fo:static-content flow-name="xsl-region-before"
                         font-family="Arial" font-size="12pt"
                         font-weight="bold" text-align="center"
                         color="#5F6A72">
        <fo:block>
          Allgemeine Geschäftsbedingungen für Dienstleistungen
          <fo:block/><!-- line break -->
          der AMC World Technologies GmbH
        </fo:block>
      </fo:static-content>                 
      <fo:static-content flow-name="rest-page-footer"
                         font-family="Frutiger LT 57 Cn" font-size="7pt"
                         color="#5F6A72">
        <fo:block text-align="center">
          <xsl:text>— Seite </xsl:text>
          <fo:page-number/>
          <xsl:text> —</xsl:text>
        </fo:block>
      </fo:static-content>
      <fo:flow flow-name="xsl-region-body">
        <fo:block-container font-family="Frutiger LT 57 Cn" font-size="7pt"
                            color="#5F6A72">
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            1. Sachlicher Geltungsbereich
          </fo:block>
          <fo:block space-after="1mm">
            Die nachstehenden Bedingungen gelten für Dienstleistungen, die
            von der AMC World Technologies GmbH, Fischerinsel 1, 10179 Berlin
            (nachfolgend „AMC World“ genannt) angeboten und erbracht werden.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            2. Art und Umfang der Leistungen
          </fo:block>
          <fo:block space-after="1mm">
            Art und Umfang der beiderseitigen Leistungen werden durch die
            vertraglichen Abmachungen geregelt. Maßgebend dafür sind:
          </fo:block>
          <fo:list-block space-after="1mm"
                         provisional-distance-between-starts="5mm">
            <fo:list-item>
              <fo:list-item-label start-indent="2.5mm"
                                  end-indent="label-end()">
                <fo:block>&#x2022;</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  der im Vertrag definierte Leistungsumfang, der als geeignet
                  für die vor­ausgesetzte Verwendung gilt,
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label start-indent="2.5mm"
                                  end-indent="label-end()">
                <fo:block>&#x2022;</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>die im Vertrag festgelegten Bedingungen,</fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label start-indent="2.5mm"
                                  end-indent="label-end()">
                <fo:block>&#x2022;</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>die nachstehenden Bedingungen,</fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label start-indent="2.5mm"
                                  end-indent="label-end()">
                <fo:block>&#x2022;</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  allgemein angewandte technische Richtlinien und Fachnormen,
                  insbesondere auch die internationalen Standards und
                  Vorschläge der Internet Engineering Task Force (IETF), wie
                  sie in den Request-for-Comments (RFC) dokumentiert sind,
                  und des W3C (World Wide Web Consortium)
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
          </fo:list-block>
          <fo:block space-after="1mm">
            Bei Unstimmigkeiten gelten die vertraglichen Abmachungen in der
            vorstehenden Reihenfolge. Weitergehende Bedingungen insbesondere
            allgemeine Geschäftsbedingungen des Vertragspartners kommen
            nicht zur Anwendung, auch wenn AMC World diesen nicht
            ausdrücklich widerspricht. Es gelten ausschließlich die AGB von
            AMC World.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            3. Organisation
          </fo:block>
          <fo:block space-after="1mm">
            Die Vertragspartner benennen bei Beginn der Leistungen
            entsprechende Ansprechpersonen auf beiden Seiten.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            4. Pflichten des Leistungsempfängers
          </fo:block>
          <fo:block space-after="1mm">
            Der Leistungsempfänger verpflichtet sich, die zur Erbringung der
            Leistungen notwendigen Voraussetzungen zu schaffen. Insbesondere
            trägt er Sorge, dass von AMC World beauftragte Mitarbeiter und
            Firmen die Installation der Hard- und Softwarekomponenten vor
            Ort durchführen können. Damit gilt eine mögliche
            Mitteilungspflicht von AMC World, dass derartige Maßnahmen
            durchgeführt werden müssen, als erfüllt. Weiterhin stellt der
            Leistungsempfänger AMC World die benötigten Dokumente und
            Informationen zur Verfügung. Sofern es sich hierbei um
            elektronische Dokumentation handelt, wird der Leistungsempfänger
            diese Dokumente in standardisierten und plattformübergreifend
            lesbaren Formaten (PDF, ASCII) zur Verfügung stellen.
          </fo:block>
          <fo:block font-family="Arial" font-weight="bold" space-after="1mm">
            4.1 Fachliche Begleitung
          </fo:block>
          <fo:block space-after="1mm">
            Der Leistungsempfänger hat, soweit notwendig, Personal aus den
            beteiligten Abteilungen zur fachlichen Begleitung freizustellen.
          </fo:block>
          <fo:block font-family="Arial" font-weight="bold" space-after="1mm">
            4.2 Bereitstellung von Hilfsmitteln für Leistungen vor Ort
          </fo:block>
          <fo:block space-after="1mm">
            Als Hilfsmittel werden für die Dauer der Leistungserbringung
            Arbeitsplätze für Mitarbeiter von AMC World beim
            Leistungsempfänger benötigt, wenn Arbeiten vor Ort durchgeführt
            werden. Die Arbeitsplätze sind vom Leistungsempfänger mit dem
            beim Leistungsempfänger üblichen und für die
            Leistungserbringungen technisch notwendigen
            Arbeitsplatz-Computersystemen auszustatten, die über eine
            Druckmöglichkeit und einen Internet-Anschluss für ssh (Secure
            Shell), http und https verfügen.
          </fo:block>
          <fo:block font-family="Arial" font-weight="bold" space-after="1mm">
            4.3 Bereitstellung von Hilfsmittel für Leistungen über
            Fernverbindung
          </fo:block>
          <fo:block space-after="1mm">
            Der Leistungsempfänger muss dafür Sorge tragen, dass AMC World
            mit Hilfe einer TCP/IP-Verbindung ein freier Zugriff auf das
            System ermöglicht wird. AMC World stellt auf Wunsch einen
            geeigneten Router bereit oder der Zugang wird über das Internet
            ermöglicht. Bei Nichtfunktion oder Nichtbereitstellung des
            Anschlusses durch den Leistungsempfänger können die Arbeiten ohne
            weitere Angabe von Gründen unterbrochen werden.
          </fo:block>
          <fo:block font-family="Arial" font-weight="bold" space-after="1mm">
            4.4 Nichtverfügbarkeit von Hilfsmitteln
          </fo:block>
          <fo:block space-after="1mm">
            Die Bereitstellung der Arbeitsplätze und der TCP/IP-Verbindung
            durch den Leistungsempfänger für AMC World erfolgt nach Beginn
            der Leistungserbringung. Werden mangels Verfügbarkeit der
            Arbeitsplätze oder der TC/IP-Verbindung, mit oder ohne Zutun oder
            Unterlassen durch den Leistungsempfänger, zur Erbringung von
            vertragsgemäßen Leistungen AMC World Zusatzaufwendungen
            notwendig, dann trägt der Leistungsempfänger alle damit
            verbundenen Kosten von AMC World und AMC World kann für Schäden,
            die aus zeitlicher Verzögerung bei der Leistungserbringung
            entstehen, nicht haftbar gemacht werden.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            5. Haftungsbeschränkung
          </fo:block>
          <fo:block space-after="1mm">
            In jedem Falle ist die vertragliche wie deliktische Haftung von
            AMC World außer bei Vorsatz und grober Fahrlässigkeit für
            Personenschäden auf 1.500.000,00 Euro, für Sach- und
            Vermögensschäden (auch Datenverlustschäden) auf 500.000 Euro
            beschränkt. AMC World haftet nicht für die Wiederbeschaffung von
            Daten, wenn der Leistungsempfänger keine regelmäßigen
            Datensicherungen durchgeführt hat.
          </fo:block>
          <fo:block space-after="1mm">
            Für Störungen auf Telekommunikationsverbindungen, für Störungen
            auf Leitungswegen innerhalb des Internet, bei höherer Gewalt, bei
            Verschulden Dritter oder des Kunden selbst wird von AMC World
            keine Haftung übernommen. Für Schäden, die entstehen, wenn der
            Leistungsempfänger Passwörter oder Benutzererkennungen an
            Nichtberechtigte weitergibt, übernimmt AMC World keine Haftung.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            6. Lieferumfang, Eigentumsvorbehalt
          </fo:block>
          <fo:block space-after="1mm">
            Sämtliche Lieferungen und Leistungen von AMC World erfolgen
            ausschließlich unter Eigentumsvorbehalt.
          </fo:block>
          <fo:block space-after="1mm">
            Ausgeliefert werden die ausführbaren Programmdateien und die
            vollständige Dokumentation der erbrachten Konfigurations- und
            Installationsleistungen. Source-Code ist nicht Bestandteil des
            Lieferumfanges.
          </fo:block>
          <fo:block space-after="1mm">
            Nach der vollständigen Bezahlung überträgt AMC World das Eigentum
            an den gelieferten Waren und das im Vertrag festgelegte
            Nutzungsrecht aus den erbrachten Leistungen an den
            Leistungsempfänger. Weiterhin überträgt AMC World nach der
            vollständigen Bezahlung das Nutzungsrecht und das Recht zur
            Bearbeitung der Leistungsergebnisse auf den Leistungsempfänger.
            AMC World behält jedoch das Recht der sonstigen beliebigen
            Verwendung der zugrunde liegenden Konzepte und
            Programm-Source-Codes.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            7. Zahlungsbedingungen
          </fo:block>
          <fo:block space-after="1mm">
            Mit Leistungserbringung erhält der Kunde von uns eine Rechnung
            mit einem kalendermäßigen Fälligkeitsdatum. Der ausgewiesene
            Rechnungsbetrag ist, wenn nicht anders vereinbart, ohne Abzug
            innerhalb von 7 Tagen nach Rechnungsdatum zahlbar. Teilzahlungen
            sind nach schriftlicher Vereinbarung möglich.
          </fo:block>
          <fo:block font-family="Arial" font-weight="bold" space-after="1mm">
            7.1 Zahlungsweise
          </fo:block>
          <fo:block space-after="1mm">
            Die Zahlung erfolgt wahlweise per Überweisung, Lastschrift,
            Barzahlung oder Vorauskasse, wobei dem Kunden von ihm zu
            vertretende Gebühren für auftretende Rücklastschriften zur Last
            fallen. AMC World behält sich vor, einzelne Zahlungsarten
            auszuschließen.
          </fo:block>
          <fo:block font-family="Arial" font-weight="bold" space-after="1mm">
            7.2 Zahlungsverzug
          </fo:block>
          <fo:block space-after="1mm">
            Zahlt der Kunde nicht innerhalb der kalendermäßig bestimmten
            Frist, behält sich AMC World vor, nochmals zu mahnen
            (Mahngebühren 5 Euro) oder gegebenenfalls die Forderung sofort an
            ein Inkassobüro oder einen Rechtsanwalt zu übergeben. Es fallen
            Verzugszinsen in Höhe von acht Prozent über dem jeweils geltenden
            Basiszinssatz an.
          </fo:block>
          <fo:block font-family="Arial" font-weight="bold" space-after="1mm">
            7.3 Aufrechnung und Zurückbehaltung
          </fo:block>
          <fo:block space-after="1mm">
            Ein Recht zur Aufrechnung steht dem Kunden nur dann zu, wenn
            seine Gegenansprüche rechtskräftig gerichtlich festgestellt oder
            schriftlich durch AMC World anerkannt wurden oder unbestritten
            sind. Der Kunde kann ein Zurückbehaltungsrecht nur ausüben,
            soweit die Ansprüche aus dem gleichen Vertragsverhältnis
            resultieren.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            8. Vertraulichkeit, Datenschutz
          </fo:block>
          <fo:block space-after="1mm">
            Die Vertragsparteien verpflichten sich, die im Rahmen des
            Vertragsgegenstandes gewonnenen Erkenntnisse – insbesondere
            technische oder wirtschaftliche Daten sowie sonstige Kenntnisse –
            geheimzuhalten und sie ausschließlich für die Zwecke des
            Gegenstands des Vertrages zu verwenden. Dies gilt nicht für
            Informationen, die öffentlich zugänglich sind oder ohne
            unberechtigtes Zutun oder Unterlassen der Vertragsparteien
            öffentlich zugänglich werden oder aufgrund richterlicher
            Anordnung oder eines Gesetzes zugänglich gemacht werden müssen.
          </fo:block>
          <fo:block space-after="1mm">
            Sofern im Rahmen des Vertragsgegenstandes personenbezogene Daten
            verarbeitet werden müssen, werden der Auftragnehmer und der
            Leistungsempfänger die gesetzlichen Datenschutzbestimmungen
            einhalten.
          </fo:block>
          <fo:block space-after="1mm">
            AMC World weist den Kunden gemäß Bundesdatenschutzgesetzt (BDSG)
            darauf hin, dass Daten des Kunden gespeichert werden.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            9. Subunternehmer
          </fo:block>
          <fo:block space-after="1mm">
            AMC World kann im Rahmen der Leistungserbringung Unteraufträge
            vergeben. In diesem Fall muss AMC World dem Unterauftragnehmer
            die den vorhergehenden Absätzen entsprechenden Pflichten
            auferlegen. Die Erteilung von derartigen Unter­aufträgen ist ohne
            Absprache mit dem Leistungsempfänger möglich. Die Haftung von AMC
            World für die gesamte Leistung bleibt hiervon unberührt.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            10. Schlussbestimmungen
          </fo:block>
          <fo:block space-after="1mm">
            Sollte eine oder mehrere der Bestimmungen dieser Allgemeinen
            Geschäftsbedingungen unwirksam sein, so bleibt der Vertrag im
            Übrigen wirksam. Anstelle der unwirksamen Bestimmung gelten die
            einschlägigen gesetzlichen Vorschriften. Es gilt deutsches Recht
            mit Ausnahme des UN-Kaufrechts. Erfüllungsort und Gerichtsstand
            ist für beide Teile des Vertrages Berlin, soweit zulässig
            vereinbar.
          </fo:block>
        </fo:block-container>
      </fo:flow>
    </fo:page-sequence>
  </xsl:template>
  
  <xsl:template name="terms-and-conditions-wares">
    <fo:page-sequence master-reference="terms-and-conditions" language="de"
                      hyphenate="true">
      <fo:static-content flow-name="xsl-region-before"
                         font-family="Arial" font-size="12pt"
                         font-weight="bold" text-align="center"
                         color="#5F6A72">
        <fo:block>
          Allgemeine Geschäftsbedingungen für Waren
          <fo:block/><!-- line break -->
          der AMC World Technologies GmbH
        </fo:block>
      </fo:static-content>                 
      <fo:static-content flow-name="rest-page-footer"
                         font-family="Frutiger LT 57 Cn" font-size="7pt"
                         color="#5F6A72">
        <fo:block text-align="center">
          <xsl:text>— Seite </xsl:text>
          <fo:page-number/>
          <xsl:text> —</xsl:text>
        </fo:block>
      </fo:static-content>
      <fo:flow flow-name="xsl-region-body">
        <fo:block-container font-family="Frutiger LT 57 Cn" font-size="7pt"
                            color="#5F6A72">
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            1. Geltungsbereich und Anbieter
          </fo:block>
          <fo:list-block space-after="1mm"
                         provisional-distance-between-starts="3mm">
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>1.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Diese Allgemeinen Geschäftsbedingungen gelten für alle 
                  Bestellungen Gewer­betreibender und Selbständiger, nicht
                  jedoch Verbraucher gemäß § 13 BGB, die gegenüber der AMC
                  World Technologies GmbH, Fischerinsel 1, 10179 Berlin
                  (nachfolgend „AMC World“) abgeben werden.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>2.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Ein Kaufvertrag kommt durch die Auftragsbestätigung von
                  AMC World, spätestens durch die vorbehaltlose Annahme der
                  Ware durch den Kunden zustande. Die Produktdarstellung im
                  Online-Shop dient zur Abgabe eines Kaufangebotes. Der Kunde
                  gibt ein verbindliches Angebot ab, wenn er den
                  Online-Bestellprozess unter Eingabe der dort verlangten
                  Angaben durchlaufen hat und im letzten Bestellschritt die
                  Schaltfläche „Bestellung absenden“ anklickt. Die Bestätigung
                  des Zugangs der Bestellung stellt noch keine Annahme des
                  Kaufangebotes dar. Sollte die Auftragsbestätigung Schreib-
                  oder Druckfehler enthalten oder sollte der Preisfestlegung
                  technisch bedingte Übermittlungsfehler zu Grunde liegen, so
                  ist AMC World zur Anfechtung berechtigt, (wobei wir Ihnen
                  unseren Irrtum beweisen müssen). Bereits erfolgte Zahlungen
                  werden dem Kunden unverzüglich erstattet.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>3.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Für Verträge zwischen dem Kunden und AMC World gelten
                  ausschließlich die nachstehenden AGB. Weitergehende
                  Bedingungen insbesondere allgemeine Geschäftsbedingungen des
                  Vertragspartners kommen nicht zur Anwendung, auch wenn AMC
                  World diesen nicht ausdrücklich widerspricht. Es gelten
                  ausschließ­lich die AGB von AMC World.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
          </fo:list-block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            2. Lieferung
          </fo:block>
          <fo:list-block space-after="1mm"
                         provisional-distance-between-starts="3mm">
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>1.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  AMC World liefert „solange der Vorrat reicht“. Wenn das
                  bestellte Produkt nicht verfügbar ist, weil AMC World mit
                  diesem Produkt von seinen Lieferanten ohne eigenes
                  Verschulden nicht beliefert wird, kann AMC World vom Vertrag
                  zurück­treten. In diesem Fall wird AMC World den Kunden
                  unverzüglich informieren und ihm ggf. die Lieferung eines
                  vergleichbaren Produktes vorschlagen. Wenn kein
                  vergleichbares Produkt verfügbar ist oder der Kunde keine
                  Lieferung eines vergleichbaren Produktes wünscht, wird AMC
                  World die bereits erbrachte Ge­genleistungen erstatten.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>2.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Sollte die Zustellung der Ware trotz dreimaligem
                  Auslieferungsversuchs schei­tern, kann AMC World vom Vertrag
                  zurücktreten. Bereits geleistete Zahlungen werden dem Kunden
                  erstattet. Über den Status der Bestellung wird der Kunde bei
                  nicht lieferbarer Ware rechtzeitig automatisch per E-Mail
                  informiert. Wenn alle Artikel ab Lager sind, verlässt die
                  Lieferung innerhalb von 3 Werktagen AMC World, sofern nicht
                  beim Angebot etwas anders angegeben wird.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>3.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Bei Lieferungen außerhalb der EU, insbesondere in die
                  Schweiz, fallen zusätzli­che Zölle und Gebühren an. Die Höhe
                  ist der jeweiligen Auftragbestätigung zu entnehmen.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>4.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Die Gefahr des zufälligen Untergangs, der zufälligen
                  Verschlechterung und des zufälligen Verlustes geht mit der
                  Übergabe auf den Kunden über. Beim Versen­dungskauf geht die
                  Gefahr mit der Auslieferung der Sache an die beauftragte
                  Transportperson auf den Kunden über. Eventuelle Ansprüche von
                  AMC World gegen die mit dem Transport beauftragte Person
                  werden hiermit an den Kunden abgetreten.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
          </fo:list-block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            3. Rücktritt
          </fo:block>
          <fo:block space-after="1mm">
            Nach Kundenspezifikation angefertigte Waren, Waren, die nach ihrer
            Beschaffen­heit für eine Rücksendung nicht geeignet sind oder
            Software, Audio- oder Videoauf­zeichnungen, sofern die gelieferten
            Datenträger von Ihnen entsiegelt worden sind, sind von der Rückgabe
            ausgeschlossen.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            4. Preise
          </fo:block>
          <fo:block space-after="1mm">
            Die auf den Produktseiten genannten Preise sind Nettopreise und
            verstehen sich zzgl. gesetzlicher Mehrwertsteuer,
            Portoversandkosten und ggf. gesondert ausge­wiesener
            Transportversicherung.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            5. Versandkosten
          </fo:block>
          <fo:block space-after="1mm">
            Für die Lieferung innerhalb Deutschland sowie in das Ausland
            berechnet AMC World die am Ende jeder Bestellung detailliert
            angegebenen Versandkosten pro Be­stellung. Bei Zahlung per Post AG
            Nachnahme wird eine zusätzliche Gebühr in Hö­he von 2,00 Euro
            fällig, die der Zusteller vor Ort erhebt.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            6. Zahlungsbedingungen
          </fo:block>
          <fo:block space-after="1mm">
            Mit der Lieferung der Ware erhält der Kunde von uns eine Rechnung
            mit einem ka­lendermäßigen Fälligkeitsdatum. Der ausgewiesene
            Rechnungsbetrag ist, wenn nicht anders vereinbart, ohne Abzug
            innerhalb von 7 Tagen nach Rechnungsdatum zahlbar. Teilzahlungen
            sind nach schriftlicher Vereinbarung möglich.
          </fo:block>
          <fo:list-block space-after="1mm"
                         provisional-distance-between-starts="3mm">
            <fo:list-item break-before="column">
              <fo:list-item-label end-indent="label-end()">
                <fo:block>1.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block space-after="1mm">
                  Die Zahlung erfolgt wahlweise per Überweisung, Lastschrift,
                  Nachnahme, Bar­zahlung, oder Vorkasse, wobei dem Kunden von
                  ihm zu vertretende Gebühren für auftretende Rücklastschriften
                  zur Last fallen. AMC World behält sich vor, einzelne
                  Zahlungsarten auszuschließen.
                </fo:block>
                <fo:list-block space-after="1mm"
                               provisional-distance-between-starts="5mm">
                  <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                      <fo:block>1.1</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                      <fo:block>Überweisung Vorkasse</fo:block>
                      <fo:block>
                        Nach Geldeingang wird die Ware umgehend versendet. Bei
                        Hardware­verkäufen kann AMC World eine Vorauszahlung
                        verlangen. Sollte eine Vorauszahlung bei
                        Bestellung/Auftragsbestätigung vereinbart sein, so ist
                        der Betrag per Vorkasse zu überweisen oder bei
                        Lieferung in bar zu über­geben.
                      </fo:block>
                    </fo:list-item-body>
                  </fo:list-item>
                  <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                      <fo:block>1.2</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                      <fo:block>Nachnahme (nur innerhalb Deutschlands)</fo:block>
                      <fo:block>
                        Bis zu einem Gesamtgewicht von 500 Gramm ist der
                        Versand per Nach­nahme mit einem Aufschlag von 4,50 Euro
                        zu den Maxibrief-Konditionen (2,95 Euro) möglich. Ab
                        einem Gewicht von 500 Gramm ist der Versand nur noch
                        per DPD-Nachnahme möglich. WICHTIG! Der Kunde sollte
                        diese Zahlungsweise nur dann auswählen, wenn
                        sichergestellt werden kann, dass die Ware
                        entgegengenommen wird. Nachnahme-Sendungen die nicht
                        zugestellt werden konnten, stellt AMC World in
                        Rechnung!
                      </fo:block>
                    </fo:list-item-body>
                  </fo:list-item>
                </fo:list-block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>2.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block space-after="1mm">
                  Zahlungsverzug: Zahlt der Kunde nicht innerhalb der
                  kalendermäßig bestimm­ten Frist, behält sich AMC World vor,
                  nochmals zu mahnen (Mahngebühren 5 Euro) oder gegebenenfalls
                  die Forderung sofort an ein Inkassobüro oder einen
                  Rechtsanwalt zu übergeben. Es fallen Verzugszinsen in Höhe
                  von acht Prozent über dem jeweils geltenden Basiszinssatz an.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>3.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block space-after="1mm">
                  Aufrechnung und Zurückbehaltung: Ein Recht zur Aufrechnung
                  steht dem Kun­den nur dann zu, wenn seine Gegenansprüche
                  rechtskräftig gerichtlich festge­stellt oder schriftlich durch
                  AMC World anerkannt wurden oder unbestritten sind. Der Kunde
                  kann ein Zurückbehaltungsrecht nur ausüben, soweit die
                  An­sprüche aus dem gleichen Vertragsverhältnis resultieren.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
          </fo:list-block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            7. Eigentumsvorbehalt
          </fo:block>
          <fo:block space-after="1mm">
            Die Ware bleibt bis zur vollständigen Zahlung Eigentum von AMC
            World. Vor Über­gang des Eigentums ist eine Weiterveräußerung,
            Verpfändung, Sicherungsübereig­nung, Verarbeitung oder Umgestaltung
            ohne Zustimmung von AMC World nicht gestattet.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            8. Transportschäden
          </fo:block>
          <fo:block space-after="1mm">
            Werden Waren mit offensichtlichen Transportschäden angeliefert, so
            ist der Kunde gehalten, diese Fehler unverzüglich bei der
            Transportperson zu reklamieren und schnellstmöglich Kontakt zu AMC
            World – bevorzugt per E-Mail – aufzunehmen. Eventuelle Ansprüche
            von AMC World gegen die Transportperson werden an den Kunden
            abgetreten.
          </fo:block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            9. Gewährleistung
          </fo:block>
          <fo:list-block space-after="1mm"
                         provisional-distance-between-starts="3mm">
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>1.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  AMC World gewährt für Neuware 12 Monate und für gebrauchte
                  Ware 6 Mona­te Gewährleistung für Mängel. Für das Vorliegen
                  von Sach- und Rechtsmängeln gelten die gesetzlichen
                  Bestimmungen. Ein Mängelanspruch setzt voraus, dass der Kunde
                  den Untersuchungs- und Rügepflichten des § 377 HGB
                  ordnungsge­mäß nachgekommen ist, soweit die Vorschrift auf den
                  Kauf Anwendung findet. Für unerhebliche Mängel ist ein
                  Nacherfüllungsrecht ausgeschlossen.
                </fo:block>
                <fo:block>
                  Unberührt hiervon bleiben Herstellergarantien.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>2.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Eine Haftung von AMC World für normale Abnutzung oder
                  Verschleiß ist ausge­schlossen. Gewährleistungsansprüche
                  bestehen ferner nicht für Verschleißteile wie Druckköpfe,
                  Farbbänder, Typenräder, Toner und andere
                  Verschleißmateriali­en.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>3.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Die mangelhafte oder zur Reparatur bestimmte Ware ist
                  ausschließlich an folgende Adresse zu senden: AMC World
                  Technologies GmbH, Kundendienst, Fischerinsel 1, 10179
                  Berlin.
                </fo:block>
                <fo:block>
                  Die Gefahr für die Versendung trägt der Kunde.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>4.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Entsorgung: AMC World weist darauf hin, dass die defekte
                  Hardware nicht in den Hausmüll gegeben werden darf und die
                  Anweisungen und Vorschriften der Hersteller zu beachten sind.
                  Insbesondere sind bei Laptops und anderen Akku enthaltenden
                  Geräten die Bestimmungen der Batterieschutzverordnung zu
                  be­achten. AMC World nimmt bei ihr gekaufte leere Akkus zurück
                  und benennt auf Anfrage gerne Batteriesammelstellen in Ihrer
                  Nähe.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
          </fo:list-block>
          <fo:block font-family="Arial" font-size="8pt" font-weight="bold"
                    space-after="1.5mm">
            10. Datenschutz
          </fo:block>
          <fo:list-block space-after="1mm"
                         provisional-distance-between-starts="3mm">
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>1.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Kundendaten werden ausschließlich für die Abwicklung der
                  Bestellung erfragt, gespeichert und verwendet. Grundlage
                  hierfür sind die einschlägigen Daten­schutzbestimmungen des
                  Bundesdatenschutzgesetzes (BDSG) und des
                  Tele­dienstedatenschutzgesetzes (TDDSG).
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
            <fo:list-item>
              <fo:list-item-label end-indent="label-end()">
                <fo:block>2.</fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  Weitergabe von Kundendaten: Um Bestellungen abwickeln und
                  ausliefern zu können, werden Kundendaten nur an den jeweils
                  mit der Auslieferung beauf­tragten Transportdienst
                  weitergegeben.
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
          </fo:list-block>
        </fo:block-container>
      </fo:flow>
    </fo:page-sequence>
  </xsl:template>
</xsl:stylesheet>