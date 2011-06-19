<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="header-berlin">
    <fo:static-content flow-name="xsl-region-before">
      <fo:table table-layout="fixed" width="158mm"
                font-family="Frutiger LT 57 Cn" font-size="7pt"
                line-height="140%" color="#5F6A72">
        <fo:table-column column-number="1" column-width="71mm"/>
        <fo:table-column column-number="2" column-width="26mm"/>
        <fo:table-column column-number="3" column-width="5mm"/>
        <fo:table-column column-number="4" column-width="28mm"/>
        <fo:table-column column-number="5" column-width="7mm"/>
        <fo:table-column column-number="6" column-width="21mm"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell number-rows-spanned="2">
              <fo:block>
                <fo:external-graphic src="url('servlet-context:/WEB-INF/data/fo/img/amc-logo.png')"
                                     content-width="56mm"
                                     content-height="12mm"/>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="8mm">
              <fo:block>Fischerinsel 1</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="8mm">
              <fo:block color="#39F" font-weight="bold">tel</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="8mm">
              <fo:block>030 8321475-0</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="8mm" text-align="right">
              <fo:block color="#39F" font-weight="bold">web</fo:block>
            </fo:table-cell>
            <fo:table-cell padding-top="8mm" text-align="right">
              <fo:block>www.amc-world.de</fo:block>
            </fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell>
              <fo:block>10179 Berlin</fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block color="#39F" font-weight="bold">fax</fo:block>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block>030 8321475-90</fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="right">
              <fo:block color="#39F" font-weight="bold">mail</fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="right">
              <fo:block>info@amc-world.de</fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:static-content>
	</xsl:template>
</xsl:stylesheet>