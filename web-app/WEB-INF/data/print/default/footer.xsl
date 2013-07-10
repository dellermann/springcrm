<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:barcode="http://barcode4j.krysalis.org/ns">

  <!--===========================================

    NAMED TEMPLATES

  ============================================-->

  <xsl:template name="footer">
    <xsl:param name="barcode"/>
    
    <fo:static-content flow-name="first-page-footer">
      <fo:table table-layout="fixed" inline-progression-dimension="100%"
        font-family="{$font.default}" font-size="{$font.size.small}"
        line-height="{$line-height.default}" color="{$color.fg.default}">
        <fo:table-column column-number="1" column-width="80mm"/>
        <fo:table-column column-number="2" column-width="78mm"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell number-rows-spanned="3">
              <fo:block>
                <xsl:if test="$barcode != ''">
                  <fo:instream-foreign-object>
                    <barcode:barcode message="{$barcode}">
                      <barcode:code128>
                        <barcode:height>
                          <xsl:value-of select="$barcode.height"/>
                          <xsl:text>mm</xsl:text>
                        </barcode:height>
                        <barcode:human-readable>
                          <barcode:placement>none</barcode:placement>
                        </barcode:human-readable>
                      </barcode:code128>
                    </barcode:barcode>
                  </fo:instream-foreign-object>
                </xsl:if>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="right">
              <fo:block color="{$color.fg.highlight}" font-weight="bold">
                <xsl:value-of select="key('client', 'bankName')"/>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell text-align="right">
              <fo:block>
                <xsl:if test="key('client', 'bankCode') != ''">
                  <xsl:text>BLZ: </xsl:text>
                  <xsl:value-of select="key('client', 'bankCode')"/>
                </xsl:if>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell text-align="right">
              <fo:block>
                <xsl:if test="key('client', 'accountNumber') != ''">
                  <xsl:text>Kontonummer: </xsl:text>
                  <xsl:value-of select="key('client', 'accountNumber')"/>
                </xsl:if>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:static-content>
	</xsl:template>
</xsl:stylesheet>
