<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:import href="servlet-context:/WEB-INF/data/fo/invoicing-transaction.xsl"/>

  <xsl:param name="sum-label">Rechnungssumme</xsl:param>

  <xsl:template match="entry[@key='transaction']">

    <!-- address field -->
    <xsl:call-template name="address-field"/>

    <!-- invoice specifications -->
    <xsl:call-template name="transaction-specification">
      <xsl:with-param name="number-label">Rechnungsnummer</xsl:with-param>
    </xsl:call-template>

    <!-- letter caption -->
    <xsl:call-template name="header-text">
        <xsl:with-param name="transaction-type-label">Rechnung</xsl:with-param>
    </xsl:call-template>

    <!-- invoice items -->
    <xsl:call-template name="items"/>

    <!-- footer text -->
    <fo:block-container font-family="Frutiger LT 57 Cn" font-size="9pt"
                        color="#5F6A72" line-height="140%"
                        keep-together.within-page="always">
      <xsl:apply-templates select="footerText"/>
      <fo:block space-after="5mm">
        <xsl:text>Bitte zahlen Sie den Betrag unter Angabe der o. g.
        Rechnungsnummer bis zum </xsl:text>
        <xsl:call-template name="format-date-long">
          <xsl:with-param name="date" select="dueDatePayment"/>
        </xsl:call-template>
        <xsl:text> auf das folgende Konto ein:</xsl:text>
      </fo:block>
      <fo:block font-weight="bold">Berliner Volksbank</fo:block>
      <fo:block>Kontonummer: 2067829002</fo:block>
      <fo:block space-after="5mm">Bankleitzahl: 100 900 00</fo:block>
      <fo:block>
        <xsl:text>Rechnungsdatum ist gleich Lieferdatum. Es gelten unsere
        Allgemeinen Geschäftsbedingungen. Für weitere Fragen stehen wir gern
        zur Verfügung. Sie erreichen uns unter den oben angegebenen
        Hotline-Nummern.</xsl:text>
      </fo:block>
      <fo:block space-after="5mm">Vielen Dank für Ihren Auftrag.</fo:block>
      <xsl:call-template name="signature"/>
    </fo:block-container>
  </xsl:template>
</xsl:stylesheet>
