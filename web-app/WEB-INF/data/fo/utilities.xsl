<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:template name="format-date-long">
    <xsl:param name="date" select="."/>

    <xsl:value-of select="format-number(number(substring($date, 9, 2)), '#0')"/>
    <xsl:text>. </xsl:text>
    <xsl:call-template name="month-name">
      <xsl:with-param name="m" select="substring($date, 6, 2)"/>
    </xsl:call-template>
    <xsl:text> </xsl:text>
    <xsl:value-of select="substring($date, 1, 4)"/>
  </xsl:template>

  <xsl:template name="month-name">
    <xsl:param name="m"/>

    <xsl:choose>
      <xsl:when test="$m = 1">Januar</xsl:when>
      <xsl:when test="$m = 2">Februar</xsl:when>
      <xsl:when test="$m = 3">März</xsl:when>
      <xsl:when test="$m = 4">April</xsl:when>
      <xsl:when test="$m = 5">Mai</xsl:when>
      <xsl:when test="$m = 6">Juni</xsl:when>
      <xsl:when test="$m = 7">Juli</xsl:when>
      <xsl:when test="$m = 8">August</xsl:when>
      <xsl:when test="$m = 9">September</xsl:when>
      <xsl:when test="$m = 10">Oktober</xsl:when>
      <xsl:when test="$m = 11">November</xsl:when>
      <xsl:when test="$m = 12">Dezember</xsl:when>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>