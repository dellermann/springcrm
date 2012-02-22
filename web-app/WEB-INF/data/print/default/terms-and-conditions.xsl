<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:template name="terms-and-conditions">
    <xsl:apply-templates select="key('entries', 'transaction')/termsAndConditions/termsAndConditions">
      <xsl:sort select="@id" data-type="number"/>
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="termsAndConditions[@id=700]">
    <fo:page-sequence master-reference="terms-and-conditions" language="de"
                      hyphenate="true">
      <fo:static-content flow-name="xsl-region-before"
                         font-family="Arial" font-size="12pt"
                         font-weight="bold" text-align="center"
                         color="#000">
        <fo:block>
          Allgemeine Geschäftsbedingungen für Dienstleistungen
          <fo:block/><!-- line break -->
          <xsl:value-of select="key('client', 'name')"/>
        </fo:block>
      </fo:static-content>                 
      <fo:static-content flow-name="rest-page-footer"
                         font-family="Helvetica" font-size="7pt"
                         color="#000">
        <fo:block text-align="center">
          <xsl:text>— Seite </xsl:text>
          <fo:page-number/>
          <xsl:text> —</xsl:text>
        </fo:block>
      </fo:static-content>
      <fo:flow flow-name="xsl-region-body">
        <fo:block-container font-family="Helvetica" font-size="7pt"
                            color="#000">
          <fo:block space-after="1mm">
            <xsl:text>Hier folgen die Allgemeinen Geschäftsbedingungen
            von</xsl:text> 
            <xsl:value-of select="key('client', 'name')"/>.
            <xsl:text>Sie können den Text in der Datei
            „WEB-INF/data/print/default/terms-and-conditions.xsl” 
            ändern.</xsl:text> 
          </fo:block>
          <fo:block space-after="1mm">
            Oder Sie wenden sich an die AMC World Technologies GmbH, erreichbar
            telefonisch über +49 30 8321475-0 oder per E-Mail unter
            springcrm@amc-world.de.
          </fo:block>
        </fo:block-container>
      </fo:flow>
    </fo:page-sequence>
  </xsl:template>
  
  <xsl:template match="termsAndConditions[@id=701]">
    <fo:page-sequence master-reference="terms-and-conditions" language="de"
                      hyphenate="true">
      <fo:static-content flow-name="xsl-region-before"
                         font-family="Arial" font-size="12pt"
                         font-weight="bold" text-align="center"
                         color="#000">
        <fo:block>
          Allgemeine Geschäftsbedingungen für Waren
          <fo:block/><!-- line break -->
          <xsl:value-of select="key('client', 'name')"/>
        </fo:block>
      </fo:static-content>                 
      <fo:static-content flow-name="rest-page-footer"
                         font-family="Helvetica" font-size="7pt"
                         color="#000">
        <fo:block text-align="center">
          <xsl:text>— Seite </xsl:text>
          <fo:page-number/>
          <xsl:text> —</xsl:text>
        </fo:block>
      </fo:static-content>
      <fo:flow flow-name="xsl-region-body">
        <fo:block-container font-family="Helvetica" font-size="7pt"
                            color="#000">
          <fo:block space-after="1mm">
            <xsl:text>Hier folgen die Allgemeinen Geschäftsbedingungen
            von</xsl:text> 
            <xsl:value-of select="key('client', 'name')"/>.
            <xsl:text>Sie können den Text in der Datei
            „WEB-INF/data/print/default/terms-and-conditions.xsl” 
            ändern.</xsl:text> 
          </fo:block>
          <fo:block space-after="1mm">
            Oder Sie wenden sich an die AMC World Technologies GmbH, erreichbar
            telefonisch über +49 30 8321475-0 oder per E-Mail unter
            springcrm@amc-world.de.
          </fo:block>
        </fo:block-container>
      </fo:flow>
    </fo:page-sequence>
  </xsl:template>
</xsl:stylesheet>