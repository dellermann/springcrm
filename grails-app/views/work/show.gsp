<html>
  <body>
    <g:applyLayout name="show" model="[instance: work]">
      <section>
        <header>
          <h3><g:message code="salesItem.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${work}" property="number"/>
            <f:display bean="${work}" property="name"/>
            <f:display bean="${work}" property="category"/>
            <f:display bean="${work}" property="salesStart"/>
            <f:display bean="${work}" property="salesEnd"/>
          </div>
          <div class="column">
            <f:display bean="${work}" property="quantity"/>
            <f:display bean="${work}" property="unit"/>
            <f:display bean="${work}" property="unitPrice"/>
            <f:display bean="${work}" property="taxRate"/>
          </div>
        </div>
      </section>
      <g:if test="${work.description}">
      <section>
        <header>
          <h3><g:message code="salesItem.fieldset.description.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${work}" property="description"/>
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="salesItem" value="${work}"/>
      <g:applyLayout name="salesItemPricingShow"/>
    </g:applyLayout>
  </body>
</html>
