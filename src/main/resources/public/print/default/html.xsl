<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:html="http://www.w3.org/1999/xhtml">

  <!--===========================================

    SELECTOR TEMPLATES

  ============================================-->

  <xsl:template match="html:html">
    <xsl:apply-templates select="html:body"/>
    <xsl:if test="count(.//html:a) > 0">
      <fo:block-container inline-progression-dimension="75%">
        <fo:block border-before-width="{$border.width.default}pt"
          border-before-style="dotted"
          border-before-color="{$color.fg.default}"
          space-before="{$space.default}mm"
          padding-before="{$space.paragraph}mm"
          font-size="{$font.size.small}">
          <xsl:apply-templates select=".//html:a" mode="list"/>
        </fo:block>
      </fo:block-container>
    </xsl:if>
  </xsl:template>

  <xsl:template match="html:body">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="html:h1">
    <fo:block font-family="{$font.header}" font-size="{$font.size.big}"
      font-weight="bold" space-before="{$space.default}mm"
      space-after="{$space.default}mm">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="html:h2">
    <fo:block font-family="{$font.header}" font-size="{$font.size.big}"
      space-before="{$space.default}mm" space-after="{$space.default}mm">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="html:h3">
    <fo:block font-family="{$font.header}" font-size="{$font.size.default}"
      font-weight="bold" space-after="{$space.paragraph}mm">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="html:h4">
    <fo:block font-family="{$font.header}" font-size="{$font.size.default}"
      font-style="italic" space-after="{$space.paragraph}mm">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="html:h5|html:h6">
    <fo:block font-family="{$font.header}" font-size="{$font.size.default}"
      space-after="{$space.paragraph}mm">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="html:p">
    <fo:block space-after="{$space.paragraph}mm" text-align="justify">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="html:blockquote">
    <fo:block space-start="{$indent.blockquote}mm">
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

  <xsl:template match="html:table">
    <fo:table table-layout="auto" space-after="{$space.default}mm"
      border-width="{$border.width.thick}pt"
      border-after-width.conditionality="retain"
      border-color="{$color.fg.default}" border-before-style="solid"
      border-after-style="solid" table-omit-footer-at-break="true">
      <xsl:apply-templates/>
    </fo:table>
  </xsl:template>

  <xsl:template match="html:thead">
    <fo:table-header border-after-width="{$border.width.default}pt"
      border-after-style="solid" border-after-color="{$color.fg.default}">
      <xsl:apply-templates/>
    </fo:table-header>
  </xsl:template>

  <xsl:template match="html:tfoot">
    <fo:table-footer>
      <xsl:apply-templates/>
    </fo:table-footer>
  </xsl:template>

  <xsl:template match="html:tbody">
    <fo:table-body>
      <xsl:apply-templates/>
    </fo:table-body>
  </xsl:template>

  <xsl:template match="html:tr">
    <fo:table-row>
      <xsl:apply-templates/>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="html:th|html:td">
    <fo:table-cell padding="{$table.cell.padding.default}">
      <fo:block>
        <xsl:if test="local-name() = 'th'">
          <xsl:attribute name="font-weight">bold</xsl:attribute>
        </xsl:if>
        <xsl:choose>
          <xsl:when test="@align = 'center'">
            <xsl:attribute name="text-align">center</xsl:attribute>
          </xsl:when>
          <xsl:when test="@align = 'right'">
            <xsl:attribute name="text-align">right</xsl:attribute>
          </xsl:when>
        </xsl:choose>
        <xsl:apply-templates/>
      </fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="html:pre">
    <fo:block border-width="{$border.width.default}pt" border-style="solid"
      border-color="{$color.fg.default}" start-indent="{$indent.blockquote}mm"
      space-before="{$space.default}mm" space-after="{$space.default}mm"
      padding="{$space.paragraph}mm" font-family="{$font.monospace}"
      white-space-treatment="preserve" linefeed-treatment="preserve">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="html:hr">
    <fo:block border-before-width="{$border.width.default}pt"
      border-before-style="solid" border-before-color="{$color.fg.default}"
      space-before="{$space.default}mm" padding-before="{$space.default}mm"/>
  </xsl:template>

  <xsl:template match="html:br">
    <fo:block/>
  </xsl:template>

  <xsl:template match="html:a">
    <fo:inline text-decoration="underline"><xsl:apply-templates/></fo:inline>
    <xsl:text> [</xsl:text>
    <xsl:number level="any" from="html:html"/>
    <xsl:text>]</xsl:text>
  </xsl:template>

  <xsl:template match="html:a" mode="list">
    <fo:block space-after="{$space.paragraph}mm">
      <xsl:text>[</xsl:text>
      <xsl:number level="any" from="html:html"/>
      <xsl:text>] </xsl:text>
      <xsl:value-of select="@href"/>
    </fo:block>
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

  <xsl:template match="html:code">
    <fo:inline font-family="{$font.monospace}">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="html:img">
    <fo:external-graphic src="{@src}"/>
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

