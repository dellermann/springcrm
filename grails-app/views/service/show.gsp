<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: serviceInstance]">
      <section>
        <header>
          <h3><g:message code="salesItem.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${serviceInstance}" property="number" />
            <f:display bean="${serviceInstance}" property="name" />
            <f:display bean="${serviceInstance}" property="category" />
            <f:display bean="${serviceInstance}" property="quantity" />
            <f:display bean="${serviceInstance}" property="unit" />
            <f:display bean="${serviceInstance}" property="unitPrice" />
          </div>
          <div class="column">
            <f:display bean="${serviceInstance}" property="taxRate" />
            <f:display bean="${serviceInstance}" property="salesStart" />
            <f:display bean="${serviceInstance}" property="salesEnd" />
          </div>
        </div>
      </section>
      <g:if test="${serviceInstance?.description}">
      <section>
        <header>
          <h3><g:message code="salesItem.fieldset.description.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${serviceInstance}" property="description" />
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="salesItem" value="${serviceInstance}" />
      <g:applyLayout name="salesItemPricingShow" />
    </g:applyLayout>
  </body>
</html>
