<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!--===========================================

    VARIABLES

  ============================================-->

  <!--== Pathes ==============================-->

  <xsl:variable name="path.template.root" select="'/public/print/default'"/>
  <xsl:variable name="path.template.images"
    select="concat($path.template.root, '/img')"/>
  <xsl:variable name="path.img.copy-watermark"
    select="concat($path.template.images, '/copy-watermark.png')"/>
  <xsl:variable name="path.img.fold-marker"
    select="concat($path.template.images, '/fold-marker.png')"/>
  <xsl:variable name="path.img.logo"
    select="concat($path.template.images, '/logo.png')"/>


  <!--== Fonts ===============================-->

  <xsl:variable name="font.default" select="'Helvetica'"/>
  <xsl:variable name="font.header" select="$font.default"/>
  <xsl:variable name="font.monospace" select="'monospace'"/>


  <!--== Font sizes ==========================-->

  <xsl:variable name="font.size.default" select="'9pt'"/>
  <xsl:variable name="font.size.small" select="'7pt'"/>
  <xsl:variable name="font.size.big" select="'12pt'"/>

  <xsl:variable name="line-height.default" select="'140%'"/>


  <!--== Colors ==============================-->

  <xsl:variable name="color.fg.default" select="'#000'"/>
  <xsl:variable name="color.fg.highlight" select="'#383'"/>


  <!--== Positions and dimensions ============-->

  <!-- Page margins in mm -->
  <xsl:variable name="page.margin.top" select="25"/>
  <xsl:variable name="page.margin.right" select="31"/>
  <xsl:variable name="page.margin.bottom" select="11"/>
  <xsl:variable name="page.margin.left" select="21"/>

  <!-- Dimensions of header and footer on the first page in mm -->
  <xsl:variable name="header.height.first" select="15"/>
  <xsl:variable name="footer.height.first" select="13"/>

  <!-- The gap between header/footer and body on the first page in mm -->
  <xsl:variable name="header.gap.first" select="5"/>
  <xsl:variable name="footer.gap.first" select="$header.gap.first"/>

  <!-- Dimension of footer on the rest pages in mm -->
  <xsl:variable name="footer.height.rest" select="3"/>

  <!-- The gap between footer and body on the rest pages in mm -->
  <xsl:variable name="footer.gap.rest" select="7"/>

  <!-- Dimension of header on the terms and conditions pages in mm -->
  <xsl:variable name="header.height.tac" select="10"/>

  <!--
    The gap between header/footer and body on the terms and conditions pages
    in mm
  -->
  <xsl:variable name="header.gap.tac" select="4"/>
  <xsl:variable name="footer.gap.tac" select="12"/>

  <!-- Top padding of text in header in mm -->
  <xsl:variable name="header.padding.top" select="8"/>

  <!-- Dimension of the aside column containing the fold marker in mm -->
  <xsl:variable name="aside.width" select="17.5"/>

  <!-- The gap between columns in mm -->
  <xsl:variable name="column.gap" select="3"/>

  <!-- Dimension of the logo in mm -->
  <xsl:variable name="logo.width" select="18.3"/>
  <xsl:variable name="logo.height" select="24.1"/>

  <!-- Position and dimension of the fold marker in mm -->
  <xsl:variable name="fold-marker.left" select="32"/>
  <xsl:variable name="fold-marker.top" select="80"/>
  <xsl:variable name="fold-marker.width" select="1.5"/>
  <xsl:variable name="fold-marker.height" select="$fold-marker.width"/>

  <!-- Dimension of the barcode in mm -->
  <xsl:variable name="barcode.height" select="$footer.height.first"/>

  <!-- Position and dimension of the address field in mm -->
  <xsl:variable name="address-field.left" select="0"/>
  <xsl:variable name="address-field.top" select="15"/>
  <xsl:variable name="address-field.width" select="73"/>
  <xsl:variable name="address-field.height" select="35"/>

  <!-- Position and dimension of the transaction specification field in mm -->
  <xsl:variable name="tx-spec-field.right" select="0"/>
  <xsl:variable name="tx-spec-field.top" select="23.5"/>
  <xsl:variable name="tx-spec-field.width" select="60"/>
  <xsl:variable name="tx-spec-field.height" select="26"/>

  <!-- Position of main content in mm -->
  <xsl:variable name="main-content.top" select="79"/>

  <!-- Spaces around caption in mm -->
  <xsl:variable name="caption.space.before" select="6"/>
  <xsl:variable name="caption.space.after" select="10"/>

  <!-- Border widths in pt -->
  <xsl:variable name="border.width.default" select="0.5"/>
  <xsl:variable name="border.width.thick" select="1"/>

  <!-- Default spaces in mm -->
  <xsl:variable name="space.default" select="5"/>
  <xsl:variable name="space.paragraph" select="2"/>
  <xsl:variable name="space.paragraph.tac" select="1"/>

  <!-- Indents in mm -->
  <xsl:variable name="indent.blockquote" select="10"/>

  <!-- List distances in mm -->
  <xsl:variable name="list.distance" select="4"/>
  <xsl:variable name="list.indent.start" select="3"/>

  <!-- Default table cell padding -->
  <xsl:variable name="table.cell.padding.before" select="'1mm'"/>
  <xsl:variable name="table.cell.padding.after" select="'.8mm'"/>
  <xsl:variable name="table.cell.padding.start" select="'1mm'"/>
  <xsl:variable name="table.cell.padding.end" select="'1mm'"/>
  <xsl:variable name="table.cell.padding.default"
    select="concat($table.cell.padding.before, ' ', $table.cell.padding.end,
    ' ', $table.cell.padding.after, ' ', $table.cell.padding.start)"/>
</xsl:stylesheet>

