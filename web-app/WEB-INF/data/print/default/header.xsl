<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!--===========================================

    NAMED TEMPLATES

  ============================================-->

	<xsl:template name="header">
    <fo:static-content flow-name="xsl-region-before">
      <fo:table table-layout="fixed" width="100%" font-family="{$font.default}"
        font-size="{$font.size.small}" line-height="{$line-height.default}"
        color="{$color.fg.default}">
        <fo:table-column column-number="1" column-width="60mm"/>
        <fo:table-column column-number="2" column-width="26mm"/>
        <fo:table-column column-number="3" column-width="5mm"/>
        <fo:table-column column-number="4" column-width="28mm"/>
        <fo:table-column column-number="5" column-width="7mm"/>
        <fo:table-column column-number="6" column-width="32mm"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell number-rows-spanned="2">
              <fo:block>
                <fo:external-graphic src="url('{$path.img.logo}')"
                  content-width="{$logo.width}mm"
                  content-height="{$logo.height}mm"/>
               </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="{$header.padding.top}mm">
              <fo:block>
                <xsl:value-of select="key('client', 'street')"/>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="{$header.padding.top}mm">
              <fo:block color="{$color.fg.highlight}"
                font-weight="bold">tel</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="{$header.padding.top}mm">
              <fo:block>
                <xsl:value-of select="key('client', 'phone')"/>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="{$header.padding.top}mm"
              text-align="right">
              <fo:block color="{$color.fg.highlight}"
                font-weight="bold">web</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="{$header.padding.top}mm"
              text-align="right">
              <fo:block>
                <xsl:choose>
                  <xsl:when test="starts-with(key('client', 'website'), 'http://')">
                    <xsl:value-of select="substring-after(key('client', 'website'), 'http://')"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="key('client', 'website')"/>
                  </xsl:otherwise>
                </xsl:choose>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell>
              <fo:block>
                <xsl:value-of select="key('client', 'postalCode')"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="key('client', 'location')"/>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block color="{$color.fg.highlight}"
                font-weight="bold">fax</fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block>
                <xsl:value-of select="key('client', 'fax')"/>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="right">
              <fo:block color="{$color.fg.highlight}"
                font-weight="bold">mail</fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="right">
              <fo:block>
                <xsl:value-of select="key('client', 'email')"/>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:static-content>
	</xsl:template>
</xsl:stylesheet>
