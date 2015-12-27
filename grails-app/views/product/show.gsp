<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: productInstance]">
      <section>
        <header>
          <h3><g:message code="salesItem.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${productInstance}" property="number" />
            <f:display bean="${productInstance}" property="name" />
            <f:display bean="${productInstance}" property="category" />
            <f:display bean="${productInstance}" property="manufacturer" />
            <f:display bean="${productInstance}" property="retailer" />
            <f:display bean="${productInstance}" property="quantity" />
            <f:display bean="${productInstance}" property="unit" />
            <f:display bean="${productInstance}" property="unitPrice" />
          </div>
          <div class="column">
            <f:display bean="${productInstance}" property="weight" />
            <f:display bean="${productInstance}" property="taxRate" />
            <f:display bean="${productInstance}" property="purchasePrice" />
            <f:display bean="${productInstance}" property="salesStart" />
            <f:display bean="${productInstance}" property="salesEnd" />
          </div>
        </div>
      </section>
      <g:if test="${productInstance?.description}">
      <section>
        <header>
          <h3><g:message code="salesItem.fieldset.description.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${productInstance}" property="description" />
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="salesItem" value="${productInstance}" />
      <g:applyLayout name="salesItemPricingShow" />
    </g:applyLayout>
  </body>
</html>
