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

  <xsl:param name="sum-label">Gutschriftssumme</xsl:param>


  <!--===========================================

    SELECTOR TEMPLATES

  ============================================-->

  <xsl:template match="entry[@key='transaction']">

    <!-- address field -->
    <xsl:call-template name="address-field"/>

    <!-- credit memo specifications -->
    <xsl:call-template name="transaction-specification">
      <xsl:with-param name="number-label">Nr. der Gutschrift</xsl:with-param>
      <xsl:with-param name="additional-specifications">
        <xsl:if test="invoice/@id">
          <fo:table-row>
            <fo:table-cell padding-after="{$space.paragraph}mm">
              <fo:block>
                <xsl:text>Zu Rechnung:</xsl:text>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-after="{$space.paragraph}mm"
              text-align="right">
              <fo:block>
                <xsl:value-of select="key('entries', 'invoiceFullNumber')"/>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </xsl:if>
        <xsl:if test="dunning/@id">
          <fo:table-row>
            <fo:table-cell padding-after="{$space.paragraph}mm">
              <fo:block>
                <xsl:text>Zu Mahnung:</xsl:text>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-after="{$space.paragraph}mm"
              text-align="right">
              <fo:block>
                <xsl:value-of select="key('entries', 'dunningFullNumber')"/>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </xsl:if>
      </xsl:with-param>
    </xsl:call-template>

    <!-- letter caption -->
    <xsl:call-template name="header-text">
      <xsl:with-param name="transaction-type-label">
        <xsl:text>Gutschrift</xsl:text>
      </xsl:with-param>
      <xsl:with-param name="additional-text">
        <xsl:if test="invoice/@id or dunning/@id">
          <xsl:text>Diese Gutschrift bezieht sich auf </xsl:text>
          <xsl:if test="invoice/@id">
            <xsl:text>Rechnung Nr. </xsl:text>
            <xsl:value-of select="key('entries', 'invoiceFullNumber')"/>
            <xsl:text> vom </xsl:text>
            <xsl:call-template name="format-date-long">
              <xsl:with-param name="date"
                select="key('entries', 'invoice')/docDate"/>
            </xsl:call-template>
          </xsl:if>
          <xsl:if test="invoice/@id and dunning/@id">
            <xsl:text> und auf </xsl:text>
          </xsl:if>
          <xsl:if test="dunning/@id">
            <xsl:text>Mahnung Nr. </xsl:text>
            <xsl:value-of select="key('entries', 'dunningFullNumber')"/>
            <xsl:text> vom </xsl:text>
            <xsl:call-template name="format-date-long">
              <xsl:with-param name="date"
                select="key('entries', 'dunning')/docDate"/>
            </xsl:call-template>
          </xsl:if>
          <xsl:text>.</xsl:text>
        </xsl:if>
      </xsl:with-param>
    </xsl:call-template>

    <!-- credit memo items -->
    <xsl:call-template name="items"/>

    <!-- footer text -->
    <fo:block-container line-height="{$line-height.default}"
      keep-together.within-page="always">
      <xsl:apply-templates select="footerText"/>

      <fo:block space-after="{$space.default}mm" text-align="justify">
        <xsl:text>Der Gutschriftsbetrag </xsl:text>
        <xsl:choose>
          <xsl:when test="stage/@id >= 2503">
            <xsl:text> wurde am </xsl:text>
            <xsl:call-template name="format-date-long">
              <xsl:with-param name="date" select="paymentDate"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text> wird</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="paymentMethod/@id">
          <xsl:choose>
            <xsl:when test="paymentMethod/@id = 2400">
              <xsl:text> bar</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text> per </xsl:text>
              <xsl:value-of select="key('entries', 'paymentMethod')"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:if>
        <xsl:text> gezahlt.</xsl:text>
      </fo:block>

      <fo:block space-after="{$space.default}mm" text-align="justify">
        <xsl:text>Es gelten unsere Allgemeinen Geschäftsbedingungen. Für
        weitere Fragen stehen wir gern zur Verfügung. Sie erreichen uns unter
        den oben angegebenen Hotline-Nummern.</xsl:text>
      </fo:block>

      <xsl:call-template name="signature"/>
    </fo:block-container>
  </xsl:template>
</xsl:stylesheet>
