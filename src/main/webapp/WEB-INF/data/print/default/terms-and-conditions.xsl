<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!--===========================================

    NAMED TEMPLATES

  ============================================-->

  <xsl:template name="terms-and-conditions">
    <xsl:apply-templates select="key('entries', 'transaction')/termsAndConditions/termsAndConditions">
      <xsl:sort select="@id" data-type="number"/>
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="termsAndConditions[@id=700]">
    <fo:page-sequence master-reference="terms-and-conditions" language="de"
      hyphenate="true">
      <fo:static-content flow-name="xsl-region-before">
        <fo:block font-family="{$font.header}" font-size="{$font.size.big}"
          font-weight="bold" text-align="center" color="{$color.fg.default}">
          <xsl:text>Allgemeine Geschäftsbedingungen für
          Dienstleistungen</xsl:text>
          <fo:block/><!-- line break -->
          <xsl:value-of select="key('client', 'name')"/>
        </fo:block>
      </fo:static-content>                 

      <xsl:call-template name="page-number-footer"/>

      <fo:flow flow-name="xsl-region-body">
        <fo:block font-family="{$font.default}" font-size="{$font.size.small}"
          color="{$color.fg.default}">
          <fo:block space-after="{$space.paragraph.tac}mm">
            <xsl:text>Hier folgen die Allgemeinen Geschäftsbedingungen
            von </xsl:text> 
            <xsl:value-of select="key('client', 'name')"/>
            <xsl:text>. Sie können den Text in der Datei
            „WEB-INF/data/print/default/terms-and-conditions.xsl” 
            ändern.</xsl:text> 
          </fo:block>
          <fo:block space-after="{$space.paragraph.tac}mm">
            <xsl:text>Oder Sie wenden sich an die AMC World Technologies GmbH,
            erreichbar telefonisch über +49 30 8321475-0 oder per E-Mail unter
            springcrm@amc-world.de.</xsl:text>
          </fo:block>
        </fo:block>
      </fo:flow>
    </fo:page-sequence>
  </xsl:template>
  
  <xsl:template match="termsAndConditions[@id=701]">
    <fo:page-sequence master-reference="terms-and-conditions" language="de"
      hyphenate="true">
      <fo:static-content flow-name="xsl-region-before">
        <fo:block font-family="{$font.header}" font-size="{$font.size.big}"
          font-weight="bold" text-align="center" color="{$color.fg.default}">
          <xsl:text>Allgemeine Geschäftsbedingungen für Waren</xsl:text>
          <fo:block/><!-- line break -->
          <xsl:value-of select="key('client', 'name')"/>
        </fo:block>
      </fo:static-content>

      <xsl:call-template name="page-number-footer"/>

      <fo:flow flow-name="xsl-region-body">
        <fo:block font-family="{$font.default}" font-size="{$font.size.small}"
          color="{$color.fg.default}">
          <fo:block space-after="{$space.paragraph.tac}mm">
            <xsl:text>Hier folgen die Allgemeinen Geschäftsbedingungen
            von </xsl:text> 
            <xsl:value-of select="key('client', 'name')"/>
            <xsl:text>. Sie können den Text in der Datei
            „WEB-INF/data/print/default/terms-and-conditions.xsl” 
            ändern.</xsl:text> 
          </fo:block>
          <fo:block space-after="{$space.paragraph.tac}mm">
            <xsl:text>Oder Sie wenden sich an die AMC World Technologies GmbH,
            erreichbar telefonisch über +49 30 8321475-0 oder per E-Mail unter
            springcrm@amc-world.de.</xsl:text>
          </fo:block>
        </fo:block>
      </fo:flow>
    </fo:page-sequence>
  </xsl:template>
</xsl:stylesheet>

