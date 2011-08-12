<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:str="http://exslt.org/strings"
                version="1.0">

  <!-- ==========================================

    Prolog

  =========================================== -->

  <xsl:output method="text"/>


  <!-- ==========================================

    Main

  =========================================== -->

  <xsl:template match="/">
    <xsl:apply-templates select="//table"/>
  </xsl:template>


  <!-- ==========================================

    Insert statements for tables

  =========================================== -->

  <xsl:template match="table[@name = 'vtiger_account']">
    <xsl:call-template name="render-inserts">
      <xsl:with-param name="table">organization</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="table[@name = 'vtiger_contactdetails']">
    <xsl:call-template name="render-inserts">
      <xsl:with-param name="table">person</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="table[@name = 'vtiger_activity']">
    <xsl:call-template name="render-inserts">
      <xsl:with-param name="table">phone_call</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="table[@name = 'vtiger_quotes' or @name = 'vtiger_salesorder' or @name = 'vtiger_invoice']">
    <xsl:call-template name="render-inserts">
      <xsl:with-param name="table">invoicing_transaction</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="table[@name = 'vtiger_inventoryproductrel']">
    <xsl:call-template name="render-inserts">
      <xsl:with-param name="table">invoicing_item</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="table[@name = 'vtiger_service']">
    <xsl:call-template name="render-inserts">
      <xsl:with-param name="table">service</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="table[@name = 'vtiger_products']">
    <xsl:call-template name="render-inserts">
      <xsl:with-param name="table">product</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="table[@name = 'vtiger_notes']">
    <xsl:call-template name="render-inserts">
      <xsl:with-param name="table">note</xsl:with-param>
    </xsl:call-template>
  </xsl:template>


  <!-- ==========================================

    Update statements for tables

  =========================================== -->

  <xsl:template match="table[@name = 'vtiger_senotesrel']">
    <xsl:text>UPDATE note
  SET </xsl:text>
    <xsl:apply-templates select="column[@name = 'organization_id' or @name = 'person_id']"/>
    <xsl:text>
  WHERE id = </xsl:text>
    <xsl:value-of select="column[@name = 'id']"/>
    <xsl:text>;
</xsl:text>
  </xsl:template>

  <xsl:template match="column[@name = 'organization_id' or @name = 'person_id']">
    <xsl:value-of select="@name"/>
    <xsl:text> = </xsl:text>
    <xsl:value-of select="."/>
  </xsl:template>


  <!-- ==========================================

    Named templates

  =========================================== -->

  <!-- 
  
  Renders the INSERT commands for the current table.
  
  Parameters:
  
    - table. the real table name
  
  -->
  <xsl:template name="render-inserts">
    <xsl:param name="table"/>

    <xsl:text>INSERT INTO </xsl:text>
    <xsl:value-of select="$table"/>
    <xsl:text>
  (</xsl:text>
    <xsl:for-each select="column">
      <xsl:if test="position() > 1">
        <xsl:text>, </xsl:text>
      </xsl:if>
      <xsl:value-of select="@name"/>
    </xsl:for-each>
    <xsl:text>)
  VALUES (</xsl:text>
    <xsl:for-each select="column">
      <xsl:if test="position() > 1">
        <xsl:text>, </xsl:text>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="string() = 'NULL'">
          <xsl:value-of select="."/>
        </xsl:when>
        <xsl:when test="number()">
          <xsl:value-of select="number()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>'</xsl:text>
          <xsl:value-of select="str:replace(str:replace(str:replace(str:replace(., '\', '\\'), '&#10;', '\n'), '&#13;', '\r'), &quot;&#39;&quot;, &quot;\&#39;&quot;)"/>
          <xsl:text>'</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
    <xsl:text>);
</xsl:text>
  </xsl:template>
</xsl:stylesheet>
