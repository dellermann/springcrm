<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!--===========================================

    IMPORTS

  ============================================-->

  <xsl:import href="servlet-context:/WEB-INF/data/print/default/invoicing-transaction.xsl"/>


  <!--===========================================

    DEFINITIONS

  ============================================-->

  <xsl:param name="sum-label">Angebotssumme</xsl:param>


  <!--===========================================

    SELECTOR TEMPLATES

  ============================================-->

  <xsl:template match="entry[@key='transaction']">

    <!-- address field -->
    <xsl:call-template name="address-field"/>

    <!-- quote specifications -->
    <xsl:call-template name="transaction-specification">
      <xsl:with-param name="number-label">Angebotsnummer</xsl:with-param>
    </xsl:call-template>

    <!-- letter caption -->
    <xsl:call-template name="header-text">
      <xsl:with-param name="transaction-type-label">Angebot</xsl:with-param>
    </xsl:call-template>

    <!-- quote items -->
    <xsl:call-template name="items"/>

    <!-- footer text -->
    <fo:block-container line-height="{$line-height.default}"
      keep-together.within-page="always">
      <xsl:apply-templates select="footerText"/>

      <fo:block space-after="{$space.default}mm" text-align="justify">
        <xsl:text>Unser Angebot ist freibleibend</xsl:text>
        <xsl:if test="validUntil != ''">
          <xsl:text> und gilt bis einschließlich </xsl:text>
          <xsl:call-template name="format-date-long">
            <xsl:with-param name="date" select="validUntil"/>
          </xsl:call-template>
        </xsl:if>
        <xsl:text>. Es gelten unsere Allgemeinen Geschäftsbedingungen. Für
        weitere Fragen stehen wir gern zur Verfügung. Sie erreichen uns unter
        den oben angegebenen Hotline-Nummern.</xsl:text>
      </fo:block>

      <fo:block space-after="{$space.default}mm">
        <xsl:text>Vielen Dank für Ihr Interesse.</xsl:text>
      </fo:block>

      <xsl:call-template name="signature"/>
    </fo:block-container>
  </xsl:template>
</xsl:stylesheet>
