<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:import href="servlet-context:/WEB-INF/data/fo/header.xsl"/>
  <xsl:import href="servlet-context:/WEB-INF/data/fo/footer.xsl"/>
  <xsl:import href="servlet-context:/WEB-INF/data/fo/terms-and-conditions.xsl"/>
  <xsl:import href="servlet-context:/WEB-INF/data/fo/utilities.xsl"/>

  <xsl:key name="entries" match="/map/entry" use="@key"/>
  <xsl:key name="values" match="/map/entry[@key='values']/entry" use="@key"/>
  <xsl:key name="items" match="/map/entry[@key='items']/invoicingItem"
           use="@id"/>
  <xsl:decimal-format decimal-separator="," grouping-separator="."/>
  

  <!--== Selector templates ==================-->

  <xsl:template match="/">
    <fo:root>
      <xsl:call-template name="page-layout"/>
      
      <fo:page-sequence master-reference="default" language="de"
                        hyphenate="true">
        <xsl:call-template name="header-berlin"/>
        <xsl:call-template name="footer-berlin">
          <xsl:with-param name="barcode">
            <xsl:value-of select="key('entries', 'fullNumber')"/>
          </xsl:with-param>
        </xsl:call-template>
        <fo:static-content flow-name="first-page-margin">
          <fo:block margin-left="32mm" margin-top="80mm">
            <fo:external-graphic src="url('servlet-context:/WEB-INF/data/fo/img/fold-marker.png')"
                                 content-width="1.5mm" content-height="1.5mm"/>
          </fo:block>
        </fo:static-content>
        <fo:static-content flow-name="rest-page-footer"
                           font-family="Frutiger LT 57 Cn" font-size="7pt"
                           color="#333">
          <fo:block text-align="center">
            <xsl:text>— Seite </xsl:text>
            <fo:page-number/>
            <xsl:text> —</xsl:text>
          </fo:block>
        </fo:static-content>
        <fo:flow flow-name="xsl-region-body">
          <xsl:apply-templates select="key('entries', 'transaction')"/>
        </fo:flow>
      </fo:page-sequence>

      <xsl:call-template name="terms-and-conditions"/>
    </fo:root>
  </xsl:template>
  
  <xsl:template match="headerText">
    <xsl:if test="string() != ''">
      <fo:block space-after="5mm">
        Sehr geehrte Damen und Herren,
      </fo:block>
      <fo:block space-after="5mm">
        <xsl:value-of select="."/>
      </fo:block>
    </xsl:if>
  </xsl:template>

  <xsl:template match="entry[@key='organization']">
    <fo:block font-size="9pt" space-after="2mm">
      <xsl:value-of select="name"/>
    </fo:block>
  </xsl:template>

  <xsl:template match="entry[@key='transaction']/billingAddrStreet">
    <fo:block font-size="9pt" space-after="2mm">
      <xsl:value-of select="."/>
    </fo:block>
  </xsl:template>

  <xsl:template match="entry[@key='transaction']/billingAddrLocation">
    <fo:block font-size="9pt">
      <xsl:value-of select="../billingAddrPostalCode"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="."/>
    </fo:block>
  </xsl:template>

  <xsl:template match="entry[@key='transaction']/items">
    <xsl:apply-templates select="invoicingItem"/>
  </xsl:template>

  <xsl:template match="items/invoicingItem">
    <xsl:variable name="item" select="key('items', @id)"/>
    <fo:table-row>
      <fo:table-cell padding="0.5mm 1mm" text-align="right">
        <fo:block><xsl:value-of select="position()"/>.</fo:block>
      </fo:table-cell>
      <xsl:apply-templates select="$item/quantity"/>
      <xsl:apply-templates select="$item/name"/>
      <xsl:apply-templates select="$item/unitPrice"/>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="invoicingItem/quantity">
    <fo:table-cell padding="0.5mm 1mm" text-align="right">
      <fo:block>
        <xsl:value-of select="format-number(number(), '#.##0,##')"/>
      </fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="invoicingItem/name">
    <fo:table-cell padding="0.5mm 1mm">
      <fo:block>
        <xsl:if test="../unit">
          <xsl:text>[</xsl:text>
          <xsl:value-of select="../unit"/>
          <xsl:text>] </xsl:text>
        </xsl:if>
        <xsl:value-of select="."/>
      </fo:block>
      <fo:block><xsl:value-of select="../description"/></fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="invoicingItem/unitPrice">
    <fo:table-cell padding="0.5mm 1mm" text-align="right">
      <fo:block>
        <xsl:value-of select="format-number(number(), '#.##0,00')"/>
        <xsl:text> €</xsl:text>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell padding="0.5mm 1mm" text-align="right">
      <fo:block>
        <xsl:value-of select="format-number(number(../quantity) * number(), '#.##0,00')"/>
        <xsl:text> €</xsl:text>
      </fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="entry[@key='subtotalNet']">
    <fo:table-row border-before-color="#333" border-before-style="solid"
                  border-before-width="0.5pt">
      <fo:table-cell number-columns-spanned="2">
        <fo:block></fo:block>
      </fo:table-cell>
      <fo:table-cell padding="0.5mm 1mm">
        <fo:block font-weight="bold">
          <xsl:value-of select="$sum-label"/>
          <xsl:text> zzgl. MwSt.</xsl:text>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell><fo:block></fo:block></fo:table-cell>
      <fo:table-cell padding="0.5mm 1mm" text-align="right">
        <fo:block font-weight="bold">
          <xsl:value-of select="format-number(number(), '#.##0,00')"/>
          <xsl:text> €</xsl:text>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="entry[@key='subtotalGross']">
    <fo:table-row border-before-color="#333" border-before-style="solid"
                  border-before-width="0.5pt">
      <fo:table-cell number-columns-spanned="2">
        <fo:block></fo:block>
      </fo:table-cell>
      <fo:table-cell padding="0.5mm 1mm">
        <fo:block font-weight="bold">Zwischensumme einschl. MwSt.</fo:block>
      </fo:table-cell>
      <fo:table-cell><fo:block></fo:block></fo:table-cell>
      <fo:table-cell padding="0.5mm 1mm" text-align="right">
        <fo:block font-weight="bold">
          <xsl:value-of select="format-number(number(), '#.##0,00')"/>
          <xsl:text> €</xsl:text>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="discountPercent">
    <xsl:if test="number() != 0">
      <fo:table-row>
        <fo:table-cell number-columns-spanned="2">
          <fo:block></fo:block>
        </fo:table-cell>
        <fo:table-cell padding="0.5mm 1mm">
          <fo:block>
            <xsl:value-of select="format-number(number(), '0,##')"/>
            <xsl:text> % Rabatt</xsl:text>
          </fo:block>
        </fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <fo:table-cell padding="0.5mm 1mm" text-align="right">
          <fo:block>
            <xsl:text>-</xsl:text>
            <xsl:value-of select="format-number(number(key('values', 'discountPercentAmount')), '#.##0,00')"/>
            <xsl:text> €</xsl:text>
          </fo:block>
        </fo:table-cell>
      </fo:table-row>
    </xsl:if>
  </xsl:template>

  <xsl:template match="discountAmount">
    <xsl:if test="number() != 0">
      <fo:table-row>
        <fo:table-cell number-columns-spanned="2">
          <fo:block></fo:block>
        </fo:table-cell>
        <fo:table-cell padding="0.5mm 1mm">
          <fo:block>Rabatt</fo:block>
        </fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <fo:table-cell padding="0.5mm 1mm" text-align="right">
          <fo:block>
            <xsl:text>-</xsl:text>
            <xsl:value-of select="format-number(number(), '#.##0,00')"/>
            <xsl:text> €</xsl:text>
          </fo:block>
        </fo:table-cell>
      </fo:table-row>
    </xsl:if>
  </xsl:template>

  <xsl:template match="shippingCosts">
    <xsl:if test="number() != 0">
      <fo:table-row>
        <fo:table-cell number-columns-spanned="2">
          <fo:block></fo:block>
        </fo:table-cell>
        <fo:table-cell padding="0.5mm 1mm">
          <fo:block>Versandkosten</fo:block>
        </fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <fo:table-cell padding="0.5mm 1mm" text-align="right">
          <fo:block>
            <xsl:value-of select="format-number(number(), '#.##0,00')"/>
            <xsl:text> €</xsl:text>
          </fo:block>
        </fo:table-cell>
      </fo:table-row>
    </xsl:if>
  </xsl:template>

  <xsl:template match="adjustment">
    <xsl:if test="number() != 0">
      <fo:table-row>
        <fo:table-cell number-columns-spanned="2">
          <fo:block></fo:block>
        </fo:table-cell>
        <fo:table-cell padding="0.5mm 1mm">
          <fo:block>Preisanpassung</fo:block>
        </fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <fo:table-cell padding="0.5mm 1mm" text-align="right">
          <fo:block>
            <xsl:value-of select="format-number(number(), '#.##0,00;-#.##0,00')"/>
            <xsl:text> €</xsl:text>
          </fo:block>
        </fo:table-cell>
      </fo:table-row>
    </xsl:if>
  </xsl:template>

  <xsl:template match="entry[@key='total']">
    <fo:table-row>
      <fo:table-cell number-columns-spanned="2">
        <fo:block></fo:block>
      </fo:table-cell>
      <fo:table-cell padding="0.5mm 1mm">
        <fo:block font-weight="bold">
          <xsl:value-of select="$sum-label"/>
          <xsl:text> einschl. MwSt.</xsl:text>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell><fo:block></fo:block></fo:table-cell>
      <fo:table-cell padding="0.5mm 1mm" text-align="right">
        <fo:block font-weight="bold">
          <xsl:value-of select="format-number(number(), '#.##0,00')"/>
          <xsl:text> €</xsl:text>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="entry[@key='taxRates']">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="entry[@key='taxRates']/entry">
    <fo:table-row>
      <fo:table-cell number-columns-spanned="2">
        <fo:block></fo:block>
      </fo:table-cell>
      <fo:table-cell padding="0.5mm 1mm">
        <fo:block>
          <xsl:value-of select="format-number(number(@key), '#.##0,##')"/>
          <xsl:text> % MwSt.</xsl:text>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell><fo:block></fo:block></fo:table-cell>
      <fo:table-cell padding="0.5mm 1mm" text-align="right">
        <fo:block>
          <xsl:value-of select="format-number(number(), '#.##0,00')"/>
          <xsl:text> €</xsl:text>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
  
  <xsl:template match="footerText">
    <xsl:if test="string() != ''">
      <fo:block space-after="5mm">
        <xsl:value-of select="."/>
      </fo:block>
    </xsl:if>
  </xsl:template>


  <!--== Named templates =====================-->

  <xsl:template name="page-layout">
    <fo:layout-master-set>
      <fo:simple-page-master margin-top="16mm" margin-right="31mm"
                             margin-bottom="11mm" margin-left="21mm"
                             master-name="first-page">
        <fo:region-body space-before="20mm" space-after="16mm"
                        margin-left="2mm" width="156mm"/>
        <fo:region-before extent="15mm"/>
        <fo:region-after region-name="first-page-footer" extent="11mm"/>
        <fo:region-end region-name="first-page-margin" extent="17.5mm"/>
      </fo:simple-page-master>
      <fo:simple-page-master margin-top="25mm" margin-right="31mm"
                             margin-bottom="11mm" margin-left="23mm"
                             master-name="rest-page">
        <fo:region-body space-after="10mm"/>
        <fo:region-after region-name="rest-page-footer" extent="3mm"/>
      </fo:simple-page-master>
      <fo:simple-page-master margin-top="25mm" margin-right="31mm"
                             margin-bottom="11mm" margin-left="23mm"
                             master-name="terms-and-conditions">
        <fo:region-body space-before="14mm" space-after="15mm"
                        column-count="2" column-gap="3mm"/>
        <fo:region-before extent="10mm"/>
        <fo:region-after region-name="rest-page-footer" extent="3mm"/>
      </fo:simple-page-master>
      <fo:page-sequence-master master-name="default">
        <fo:repeatable-page-master-alternatives>
          <fo:conditional-page-master-reference master-reference="first-page"
                                                page-position="first"/>
          <fo:conditional-page-master-reference master-reference="rest-page"
                                                page-position="rest"/>
        </fo:repeatable-page-master-alternatives>
      </fo:page-sequence-master>
    </fo:layout-master-set>
  </xsl:template>
  
  <xsl:template name="address-field">
    <fo:block-container absolute-position="absolute" top="15mm"
                        left="0" width="60mm" height="35mm"
                        font-family="Frutiger LT 57 Cn"
                        color="#333">
      <fo:block font-size="7pt" border-after-color="#333"
                border-after-style="solid"
                border-after-width="0.05pt" space-after="5mm"
                padding-after="0.5mm" text-align="center">
        AMC World Technologies GmbH · Fischerinsel 1 · 10179 Berlin
      </fo:block>
      <xsl:apply-templates select="key('entries', 'organization')"/>
      <xsl:apply-templates select="billingAddrStreet"/>
      <xsl:apply-templates select="billingAddrLocation"/>
    </fo:block-container>
  </xsl:template>
  
  <xsl:template name="transaction-specification">
    <xsl:param name="number-label"/>

    <fo:block-container absolute-position="absolute" top="27mm"
                        right="0" width="60mm" height="26mm"
                        font-family="Frutiger LT 57 Cn"
                        font-size="9pt" color="#333">
      <fo:table table-layout="fixed" width="100%">
        <fo:table-column column-number="1" column-width="30mm"/>
        <fo:table-column column-number="2" column-width="30mm"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell padding-after="2mm">
              <fo:block>
                <xsl:value-of select="$number-label"/>
                <xsl:text>:</xsl:text>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-after="2mm" text-align="right">
              <fo:block>
                <xsl:value-of select="key('entries', 'fullNumber')"/>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell padding-after="2mm">
              <fo:block>Ansprechpartner:</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-after="2mm" text-align="right">
              <fo:block>
                <xsl:value-of select="key('entries', 'user')/firstName"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="key('entries', 'user')/lastName"/>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell padding-after="2mm">
              <fo:block>Hotlines:</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-after="2mm" text-align="right">
              <fo:block>
                <xsl:value-of select="key('entries', 'user')/mobile"/>
              </fo:block>
              <fo:block>030 8321475-0</fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:block-container>
  </xsl:template>
  
  <xsl:template name="header-text">
    <xsl:param name="transaction-type-label"/>

    <fo:block-container font-family="Frutiger LT 57 Cn"
                        font-size="9pt" space-before="79mm"
                        padding-start="2mm" color="#333">
      <fo:block text-align="right">
        <xsl:text>Berlin, den </xsl:text>
        <xsl:call-template name="format-date-long">
          <xsl:with-param name="date" select="docDate"/>
        </xsl:call-template>
      </fo:block>
      <fo:block space-before="6mm" space-after="10mm" font-weight="bold">
        <xsl:value-of select="$transaction-type-label"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="subject"/>
      </fo:block>
      <xsl:apply-templates select="headerText"/>
    </fo:block-container>
  </xsl:template>
  
  <xsl:template name="items">
    <fo:table table-layout="fixed" width="100%" space-after="5mm"
              font-family="Frutiger LT 57 Cn" font-size="9pt" color="#333"
              border-color="#333" border-width="1pt"
              border-before-style="solid" border-after-style="solid"
              table-omit-footer-at-break="true">
      <fo:table-column column-number="1" column-width="9mm"/>
      <fo:table-column column-number="2" column-width="14mm"/>
      <fo:table-column column-number="3" column-width="88mm"/>
      <fo:table-column column-number="4" column-width="22mm"/>
      <fo:table-column column-number="5" column-width="22mm"/>
      <fo:table-header font-weight="bold" text-align="center">
        <fo:table-row border-after-color="#333" border-after-style="solid"
                      border-after-width="0.5pt">
          <fo:table-cell padding="0.5mm 1mm">
            <fo:block>Pos.</fo:block>
          </fo:table-cell>
          <fo:table-cell padding="0.5mm 1mm">
            <fo:block>Menge</fo:block>
          </fo:table-cell>
          <fo:table-cell padding="0.5mm 1mm">
            <fo:block>Artikel/Leistung</fo:block>
          </fo:table-cell>
          <fo:table-cell padding="0.5mm 1mm">
            <fo:block>Einzelpreis</fo:block>
          </fo:table-cell>
          <fo:table-cell padding="0.5mm 1mm">
            <fo:block>Gesamtpreis</fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-header>
      <fo:table-footer>
        <xsl:apply-templates select="key('values', 'subtotalNet')"/>
        <xsl:apply-templates select="key('entries', 'taxRates')"/>
        <xsl:if test="discountPercent != 0 or discountAmount != 0 or adjustment != 0">
          <xsl:apply-templates select="key('values', 'subtotalGross')"/>
          <xsl:apply-templates select="discountPercent"/>
          <xsl:apply-templates select="discountAmount"/>
          <xsl:apply-templates select="adjustment"/>
        </xsl:if>
        <xsl:apply-templates select="key('values', 'total')"/>
      </fo:table-footer>
      <fo:table-body>
        <xsl:apply-templates select="items"/>
        <xsl:apply-templates select="shippingCosts"/>
      </fo:table-body>
    </fo:table>
  </xsl:template>
  
  <xsl:template name="signature">
    <fo:block>
      <xsl:value-of select="key('entries', 'user')/firstName"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="key('entries', 'user')/lastName"/>
    </fo:block>
    <fo:block>AMC World Technologies</fo:block>
  </xsl:template>
</xsl:stylesheet>
