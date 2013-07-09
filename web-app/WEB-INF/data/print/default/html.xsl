<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:html="http://www.w3.org/1999/xhtml">

  <!--===========================================

    SELECTOR TEMPLATES

  ============================================-->

  <xsl:template match="html:html">
    <xsl:apply-templates select="html:body"/>
  </xsl:template>

  <xsl:template match="html:body">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="html:p">
    <fo:block space-after="{$space.paragraph}mm" text-align="justify">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="html:ol|html:ul">
    <fo:list-block provisional-distance-between-starts="{$list.distance}mm"
      start-indent="{$list.indent.start}mm">
      <xsl:apply-templates/>
    </fo:list-block>
  </xsl:template>

  <xsl:template match="html:ol/html:li">
    <xsl:call-template name="render-li">
      <xsl:with-param name="label"><xsl:number format="1."/></xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="html:ul/html:li">
    <xsl:call-template name="render-li"/>
  </xsl:template>

  <xsl:template match="html:br">
    <fo:block/>
  </xsl:template>

  <xsl:template match="html:strong">
    <fo:inline font-weight="bold">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="html:em">
    <fo:inline font-style="italic">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>


  <!--===========================================

    NAMED TEMPLATES

  ============================================-->

  <xsl:template name="render-li">
    <xsl:param name="label" select="'&#x2022;'"/>

    <fo:list-item>
      <fo:list-item-label end-indent="label-end()">
        <fo:block><xsl:value-of select="$label"/></fo:block>
      </fo:list-item-label>
      <fo:list-item-body start-indent="body-start()">
        <fo:block text-align="justify">
          <xsl:apply-templates/>
        </fo:block>
      </fo:list-item-body>
    </fo:list-item>
  </xsl:template>
</xsl:stylesheet>

