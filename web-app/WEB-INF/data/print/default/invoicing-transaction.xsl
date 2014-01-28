<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:html="http://www.w3.org/1999/xhtml">

  <!--===========================================

    IMPORTS

  ============================================-->

  <xsl:import href="servlet-context:/WEB-INF/data/print/default/config.xsl"/>
  <xsl:import href="servlet-context:/WEB-INF/data/print/default/header.xsl"/>
  <xsl:import href="servlet-context:/WEB-INF/data/print/default/footer.xsl"/>
  <xsl:import href="servlet-context:/WEB-INF/data/print/default/terms-and-conditions.xsl"/>
  <xsl:import href="servlet-context:/WEB-INF/data/print/default/html.xsl"/>
  <xsl:import href="servlet-context:/WEB-INF/data/print/default/utilities.xsl"/>


  <!--===========================================

    DEFINITIONS

  ============================================-->

  <xsl:key name="entries" match="/map/entry" use="@key"/>
  <xsl:key name="values" match="/map/entry[@key='values']/entry" use="@key"/>
  <xsl:key name="items" match="/map/entry[@key='items']/invoicingItem"
    use="@id"/>
  <xsl:key name="descriptions-html" match="/map/itemsHtml/descriptionHtml"
    use="@id"/>
  <xsl:key name="client" match="/map/entry[@key='client']/entry" use="@key"/>

  <xsl:decimal-format decimal-separator="," grouping-separator="."/>


  <!--===========================================

    SELECTOR TEMPLATES

  ============================================-->

  <xsl:template match="/">
    <fo:root>
      <xsl:call-template name="page-layout"/>

      <fo:page-sequence master-reference="default" language="de"
        hyphenate="true">
        <xsl:call-template name="header"/>
        <xsl:call-template name="footer">
          <xsl:with-param name="barcode">
            <xsl:value-of select="key('entries', 'fullNumber')"/>
          </xsl:with-param>
        </xsl:call-template>

        <fo:static-content flow-name="first-page-margin">
          <fo:block margin-left="{$fold-marker.left}mm"
            margin-top="{$fold-marker.top}mm">
            <fo:external-graphic src="url('{$path.img.fold-marker}')"
              content-width="{$fold-marker.width}mm"
              content-height="{$fold-marker.height}mm"/>
          </fo:block>
        </fo:static-content>

        <xsl:call-template name="page-number-footer"/>

        <fo:flow flow-name="xsl-region-body">
          <fo:block font-family="{$font.default}"
            font-size="{$font.size.default}" color="{$color.fg.default}">
            <xsl:apply-templates select="key('entries', 'transaction')"/>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>

      <xsl:call-template name="terms-and-conditions"/>
    </fo:root>
  </xsl:template>

  <xsl:template match="entry[@key='organization']">
    <fo:block space-after="{$space.paragraph}mm">
      <xsl:value-of select="name"/>
    </fo:block>
  </xsl:template>

  <xsl:template match="entry[@key='person']">
    <xsl:if test="lastName">
      <fo:block space-after="{$space.paragraph}mm">
        <xsl:text>z. Hd. </xsl:text>
        <xsl:choose>
          <xsl:when test="salutation/@id = 1">
            <xsl:text>Herrn </xsl:text>
          </xsl:when>
          <xsl:when test="salutation/@id = 2">
            <xsl:text>Frau </xsl:text>
          </xsl:when>
        </xsl:choose>
        <xsl:value-of select="firstName"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="lastName"/>
      </fo:block>
    </xsl:if>
  </xsl:template>

  <xsl:template match="entry[@key='transaction']/billingAddr">
    <xsl:apply-templates select="/map/billingAddrStreetHtml/html:html"/>
    <xsl:apply-templates select="poBox"/>
    <xsl:apply-templates select="location"/>
    <xsl:apply-templates select="country"/>
  </xsl:template>

  <xsl:template match="poBox">
    <fo:block space-after="{$space.paragraph}mm">
      <xsl:value-of select="."/>
    </fo:block>
  </xsl:template>

  <xsl:template match="location">
    <fo:block>
      <xsl:value-of select="preceding-sibling::postalCode"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="."/>
    </fo:block>
  </xsl:template>

  <xsl:template match="country">
    <xsl:if test="string() != 'Deutschland' or string() != 'D'">
      <fo:block space-before="{$space.paragraph}mm">
        <xsl:value-of select="."/>
      </fo:block>
    </xsl:if>
  </xsl:template>

  <xsl:template match="headerText">
    <xsl:if test="string() != ''">
      <fo:block space-after="{$space.default}mm">
        <xsl:text>Sehr geehrte Damen und Herren,</xsl:text>
      </fo:block>
      <fo:block>
        <xsl:apply-templates select="/map/headerTextHtml/html:html"/>
      </fo:block>
    </xsl:if>
  </xsl:template>

  <xsl:template match="entry[@key='transaction']/items">
    <xsl:apply-templates select="invoicingItem"/>
  </xsl:template>

  <xsl:template match="items/invoicingItem">
    <xsl:variable name="item" select="key('items', @id)"/>
    <fo:table-row>
      <fo:table-cell padding="{$table.cell.padding.default}"
        text-align="right">
        <fo:block><xsl:value-of select="position()"/>.</fo:block>
      </fo:table-cell>
      <xsl:apply-templates select="$item/quantity"/>
      <xsl:apply-templates select="$item/name"/>
      <xsl:apply-templates select="$item/unitPrice"/>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="invoicingItem/quantity">
    <fo:table-cell padding="{$table.cell.padding.default}" text-align="right">
      <fo:block>
        <xsl:value-of select="format-number(number(), '#.##0,##')"/>
      </fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="invoicingItem/name">
    <fo:table-cell padding="{$table.cell.padding.default}"
      padding-before="{$table.cell.padding.before}"
      padding-before.conditionality="retain">
      <fo:block>
        <xsl:if test="../unit">
          <xsl:text>[</xsl:text>
          <xsl:value-of select="../unit"/>
          <xsl:text>] </xsl:text>
        </xsl:if>
        <xsl:value-of select="."/>
      </fo:block>
      <fo:block>
        <xsl:apply-templates
          select="key('descriptions-html', ../@id)/html:html"/>
      </fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="invoicingItem/unitPrice">
    <fo:table-cell padding="{$table.cell.padding.default}" text-align="right">
      <fo:block>
        <xsl:value-of select="format-number(number(), '#.##0,00')"/>
        <xsl:text> €</xsl:text>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell padding="{$table.cell.padding.default}" text-align="right">
      <fo:block>
        <xsl:value-of
          select="format-number(number(../quantity) * number(), '#.##0,00')"/>
        <xsl:text> €</xsl:text>
      </fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="entry[@key='subtotalNet']">
    <fo:table-row border-before-width="{$border.width.default}pt"
      border-before-style="solid" border-before-color="{$color.fg.default}">
      <fo:table-cell number-columns-spanned="2">
        <fo:block></fo:block>
      </fo:table-cell>
      <fo:table-cell padding="{$table.cell.padding.default}">
        <fo:block font-weight="bold">
          <xsl:value-of select="$sum-label"/>
          <xsl:text> zzgl. MwSt.</xsl:text>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell><fo:block></fo:block></fo:table-cell>
      <fo:table-cell padding="{$table.cell.padding.default}"
        text-align="right">
        <fo:block font-weight="bold">
          <xsl:value-of select="format-number(number(), '#.##0,00')"/>
          <xsl:text> €</xsl:text>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="entry[@key='subtotalGross']">
    <fo:table-row border-before-width="{$border.width.default}pt"
      border-before-style="solid" border-before-color="{$color.fg.default}">
      <fo:table-cell number-columns-spanned="2">
        <fo:block></fo:block>
      </fo:table-cell>
      <fo:table-cell padding="{$table.cell.padding.default}">
        <fo:block font-weight="bold">Zwischensumme einschl. MwSt.</fo:block>
      </fo:table-cell>
      <fo:table-cell><fo:block></fo:block></fo:table-cell>
      <fo:table-cell padding="{$table.cell.padding.default}"
        text-align="right">
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
        <fo:table-cell padding="{$table.cell.padding.default}">
          <fo:block>
            <xsl:value-of select="format-number(number(), '0,##')"/>
            <xsl:text> % Rabatt</xsl:text>
          </fo:block>
        </fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <fo:table-cell padding="{$table.cell.padding.default}"
          text-align="right">
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
        <fo:table-cell padding="{$table.cell.padding.default}">
          <fo:block>Rabatt</fo:block>
        </fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <fo:table-cell padding="{$table.cell.padding.default}"
          text-align="right">
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
        <fo:table-cell padding="{$table.cell.padding.default}">
          <fo:block>Versandkosten</fo:block>
        </fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <fo:table-cell padding="{$table.cell.padding.default}"
          text-align="right">
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
        <fo:table-cell padding="{$table.cell.padding.default}">
          <fo:block>Preisanpassung</fo:block>
        </fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <fo:table-cell padding="{$table.cell.padding.default}"
          text-align="right">
          <fo:block>
            <xsl:value-of
              select="format-number(number(), '#.##0,00;-#.##0,00')"/>
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
      <fo:table-cell padding="{$table.cell.padding.default}">
        <fo:block font-weight="bold">
          <xsl:value-of select="$sum-label"/>
          <xsl:text> einschl. MwSt.</xsl:text>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell><fo:block></fo:block></fo:table-cell>
      <fo:table-cell padding="{$table.cell.padding.default}"
        text-align="right">
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
      <fo:table-cell padding="{$table.cell.padding.default}">
        <fo:block>
          <xsl:value-of select="format-number(number(@key), '#.##0,##')"/>
          <xsl:text> % MwSt.</xsl:text>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell><fo:block></fo:block></fo:table-cell>
      <fo:table-cell padding="{$table.cell.padding.default}"
        text-align="right">
        <fo:block>
          <xsl:value-of select="format-number(number(), '#.##0,00')"/>
          <xsl:text> €</xsl:text>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="footerText">
    <xsl:if test="string() != ''">
      <fo:block space-after="{$space.default}mm">
        <xsl:apply-templates select="/map/footerTextHtml/html:html"/>
      </fo:block>
    </xsl:if>
  </xsl:template>


  <!--===========================================

    NAMED TEMPLATES

  ============================================-->

  <xsl:template name="page-layout">
    <fo:layout-master-set>
      <fo:simple-page-master master-name="first-page"
        margin-top="{$page.margin.top}mm"
        margin-right="{$page.margin.right}mm"
        margin-bottom="{$page.margin.bottom}mm"
        margin-left="{$page.margin.left}mm">
        <fo:region-body
          space-before="{$header.height.first + $header.gap.first}mm"
          space-after="{$footer.height.first + $footer.gap.first}mm">
          <xsl:if test="key('entries', 'watermark') = 'duplicate'">
            <xsl:attribute name="background-image">
              <xsl:text>url(</xsl:text>
              <xsl:value-of select="$path.img.copy-watermark"/>
              <xsl:text>)</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="background-position-horizontal">center</xsl:attribute>
            <xsl:attribute name="background-repeat">repeat-y</xsl:attribute>
          </xsl:if>
        </fo:region-body>
        <fo:region-before extent="{$header.height.first}mm"/>
        <fo:region-after region-name="first-page-footer"
          extent="{$footer.height.first}mm"/>
        <fo:region-end region-name="first-page-margin"
          extent="{$aside.width}mm"/>
      </fo:simple-page-master>

      <fo:simple-page-master master-name="rest-page"
        margin-top="{$page.margin.top}mm"
        margin-right="{$page.margin.right}mm"
        margin-bottom="{$page.margin.bottom}mm"
        margin-left="{$page.margin.left}mm">
        <fo:region-body
          space-after="{$footer.height.rest + $footer.gap.rest}mm">
          <xsl:if test="key('entries', 'watermark') = 'duplicate'">
            <xsl:attribute name="background-image">
              <xsl:text>url(</xsl:text>
              <xsl:value-of select="$path.img.copy-watermark"/>
              <xsl:text>)</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="background-position-horizontal">center</xsl:attribute>
            <xsl:attribute name="background-repeat">repeat-y</xsl:attribute>
          </xsl:if>
        </fo:region-body>
        <fo:region-after region-name="rest-page-footer"
          extent="{$footer.height.rest}mm"/>
      </fo:simple-page-master>

      <fo:simple-page-master master-name="terms-and-conditions"
        margin-top="{$page.margin.top}mm"
        margin-right="{$page.margin.right}mm"
        margin-bottom="{$page.margin.bottom}mm"
        margin-left="{$page.margin.left}mm">
        <fo:region-body space-before="{$header.height.tac + $header.gap.tac}mm"
          space-after="{$footer.height.rest + $footer.gap.tac}mm"
          column-count="2" column-gap="{$column.gap}mm"/>
        <fo:region-before extent="{$header.height.tac}mm"/>
        <fo:region-after region-name="rest-page-footer"
          extent="{$footer.height.rest}mm"/>
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
    <fo:block-container absolute-position="absolute"
      top="{$address-field.top}mm" left="{$address-field.left}mm"
      width="{$address-field.width}mm" height="{$address-field.height}mm">
      <fo:block font-size="{$font.size.small}"
        border-after-color="{$color.fg.default}" border-after-style="solid"
        border-after-width="{$border.width.default}pt"
        space-after="{$space.default}mm" padding-after="0.5mm"
        text-align="center">
        <xsl:value-of select="key('client', 'name')"/>
        <xsl:text> · </xsl:text>
        <xsl:value-of select="key('client', 'street')"/>
        <xsl:text> · </xsl:text>
        <xsl:value-of select="key('client', 'postalCode')"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="key('client', 'location')"/>
      </fo:block>

      <xsl:apply-templates select="key('entries', 'organization')"/>
      <xsl:apply-templates select="key('entries', 'person')"/>
      <xsl:apply-templates select="key('entries', 'transaction')/billingAddr"/>
    </fo:block-container>
  </xsl:template>

  <xsl:template name="transaction-specification">
    <xsl:param name="number-label"/>
    <xsl:param name="additional-specifications"/>

    <fo:block-container absolute-position="absolute"
      top="{$tx-spec-field.top}mm" right="{$tx-spec-field.right}mm"
      width="{$tx-spec-field.width}mm" height="{$tx-spec-field.height}mm">
      <fo:table table-layout="fixed" inline-progression-dimension="100%">
        <fo:table-column column-number="1" column-width="30mm"/>
        <fo:table-column column-number="2" column-width="30mm"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell padding-after="{$space.paragraph}mm">
              <fo:block>
                <xsl:value-of select="$number-label"/>
                <xsl:text>:</xsl:text>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-after="{$space.paragraph}mm"
              text-align="right">
              <fo:block>
                <xsl:value-of select="key('entries', 'fullNumber')"/>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          <xsl:if test="$additional-specifications">
            <xsl:copy-of select="$additional-specifications"/>
          </xsl:if>
          <fo:table-row>
            <fo:table-cell padding-after="{$space.paragraph}mm">
              <fo:block>Ansprechpartner:</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-after="{$space.paragraph}mm"
              text-align="right">
              <fo:block>
                <xsl:value-of select="key('entries', 'user')/firstName"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="key('entries', 'user')/lastName"/>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          <xsl:if test="key('entries', 'user')/mobile or key('entries', 'user')/phone">
            <fo:table-row>
              <fo:table-cell padding-after="{$space.paragraph}mm">
                <fo:block>Hotlines:</fo:block>
              </fo:table-cell>
              <fo:table-cell padding-after="{$space.paragraph}mm"
                text-align="right">
                <xsl:if test="key('entries', 'user')/mobile">
                  <fo:block>
                    <xsl:value-of select="key('entries', 'user')/mobile"/>
                  </fo:block>
                </xsl:if>
                <xsl:if test="key('entries', 'user')/phone">
                  <fo:block>
                    <xsl:value-of select="key('entries', 'user')/phone"/>
                  </fo:block>
                </xsl:if>
              </fo:table-cell>
            </fo:table-row>
          </xsl:if>
        </fo:table-body>
      </fo:table>
    </fo:block-container>
  </xsl:template>

  <xsl:template name="header-text">
    <xsl:param name="transaction-type-label"/>
    <xsl:param name="additional-text"/>

    <fo:block space-before="{$main-content.top}mm" text-align="right">
      <xsl:value-of select="key('client', 'location')"/>
      <xsl:text>, den </xsl:text>
      <xsl:call-template name="format-date-long">
        <xsl:with-param name="date" select="docDate"/>
      </xsl:call-template>
    </fo:block>

    <fo:block space-after="{$space.default}mm">
      <fo:block space-before="{$caption.space.before}mm"
        space-after="{$caption.space.after}mm" font-weight="bold">
        <xsl:value-of select="$transaction-type-label"/>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="/map/subjectHtml"/>
      </fo:block>
      <xsl:apply-templates select="headerText"/>
      <xsl:if test="$additional-text">
        <fo:block>
          <xsl:value-of select="$additional-text"/>
        </fo:block>
      </xsl:if>
    </fo:block>
  </xsl:template>

  <xsl:template name="items">
    <fo:table table-layout="fixed" inline-progression-dimension="100%"
      space-after="{$space.default}mm" border-color="{$color.fg.default}"
      border-width="{$border.width.thick}pt"
      border-after-width.conditionality="retain" border-before-style="solid"
      border-after-style="solid" table-omit-footer-at-break="true">
      <fo:table-column column-number="1" column-width="9mm"/>
      <fo:table-column column-number="2" column-width="14mm"/>
      <fo:table-column column-number="3" column-width="91mm"/>
      <fo:table-column column-number="4" column-width="22mm"/>
      <fo:table-column column-number="5" column-width="22mm"/>
      <fo:table-header font-weight="bold" text-align="center">
        <fo:table-row
          border-after-width="{$border.width.default}pt"
          border-after-style="solid" border-after-color="{$color.fg.default}">
          <fo:table-cell padding="{$table.cell.padding.default}">
            <fo:block>Pos.</fo:block>
          </fo:table-cell>
          <fo:table-cell padding="{$table.cell.padding.default}">
            <fo:block>Menge</fo:block>
          </fo:table-cell>
          <fo:table-cell padding="{$table.cell.padding.default}">
            <fo:block>Artikel/Leistung</fo:block>
          </fo:table-cell>
          <fo:table-cell padding="{$table.cell.padding.default}">
            <fo:block>Einzelpreis</fo:block>
          </fo:table-cell>
          <fo:table-cell padding="{$table.cell.padding.default}">
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
    <fo:block><xsl:value-of select="key('client', 'name')"/></fo:block>
  </xsl:template>

  <xsl:template name="page-number-footer">
    <fo:static-content flow-name="rest-page-footer">
      <fo:block color="{$color.fg.default}" font-family="{$font.default}"
        font-size="{$font.size.small}" text-align="center">
        <xsl:text>— Seite </xsl:text>
        <fo:page-number/>
        <xsl:text> —</xsl:text>
      </fo:block>
    </fo:static-content>
  </xsl:template>
</xsl:stylesheet>
