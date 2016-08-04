<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: workInstance]">
      <section>
        <header>
          <h3><g:message code="salesItem.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${workInstance}" property="number" />
            <f:display bean="${workInstance}" property="name" />
            <f:display bean="${workInstance}" property="category" />
            <f:display bean="${workInstance}" property="salesStart" />
            <f:display bean="${workInstance}" property="salesEnd" />
          </div>
          <div class="column">
            <f:display bean="${workInstance}" property="quantity" />
            <f:display bean="${workInstance}" property="unit" />
            <f:display bean="${workInstance}" property="unitPrice" />
            <f:display bean="${workInstance}" property="taxRate" />
          </div>
        </div>
      </section>
      <g:if test="${workInstance?.description}">
      <section>
        <header>
          <h3><g:message code="salesItem.fieldset.description.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${workInstance}" property="description" />
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="salesItem" value="${workInstance}" />
      <g:applyLayout name="salesItemPricingShow" />
    </g:applyLayout>
  </body>
</html>
