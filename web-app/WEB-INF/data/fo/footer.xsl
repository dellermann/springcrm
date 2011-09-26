<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:barcode="http://barcode4j.krysalis.org/ns">
  <xsl:template name="footer-berlin">
    <xsl:param name="barcode"/>
    
    <fo:static-content flow-name="first-page-footer">
      <fo:table table-layout="fixed" width="158mm"
                font-family="Frutiger LT 57 Cn" font-size="7pt"
                line-height="140%" color="#333">
        <fo:table-column column-number="1" column-width="71mm"/>
        <fo:table-column column-number="2" column-width="26mm"/>
        <fo:table-column column-number="3" column-width="8mm"/>
        <fo:table-column column-number="4" column-width="22mm"/>
        <fo:table-column column-number="5" column-width="14mm"/>
        <fo:table-column column-number="6" column-width="17mm"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell number-rows-spanned="3">
              <fo:block>
                <xsl:if test="$barcode != ''">
                  <fo:instream-foreign-object>
                    <barcode:barcode message="{$barcode}">
                      <barcode:code128>
                        <barcode:height>12mm</barcode:height>
                        <barcode:human-readable>
                          <barcode:placement>none</barcode:placement>
                        </barcode:human-readable>
                      </barcode:code128>
                    </barcode:barcode>
                  </fo:instream-foreign-object>
                </xsl:if>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block color="#39F" font-weight="bold">
                Geschäftsführer
              </fo:block>
            </fo:table-cell>
            <fo:table-cell number-columns-spanned="2">
              <fo:block color="#39F" font-weight="bold">
                Berliner Volksbank
              </fo:block>
            </fo:table-cell>
            <fo:table-cell><fo:block>Steuernr.</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>30/190/00706</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell>
              <fo:block>Daniel Ellermann</fo:block>
            </fo:table-cell>
            <fo:table-cell><fo:block>BLZ</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>100 900 00</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Handelsreg.</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>HRB 111687 B</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell>
              <fo:block>Robert Kirchner</fo:block>
            </fo:table-cell>
            <fo:table-cell><fo:block>Konto</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>2067829002</fo:block></fo:table-cell>
            <fo:table-cell number-columns-spanned="2">
              <fo:block>Amtsg. Berlin Charlottenbg.</fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:static-content>
	</xsl:template>
</xsl:stylesheet>