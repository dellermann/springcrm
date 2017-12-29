<html>
  <g:applyLayout name="show" model="[instance: roleGroup]">
    <section>
      <header>
        <h3><g:message code="default.fieldset.general.label"/></h3>
      </header>
      <div class="column-group">
        <div class="column">
          <f:display bean="${roleGroup}" property="name"/>
          <f:display bean="${roleGroup}" property="authorities"/>
        </div>
      </div>
    </section>
  </g:applyLayout>
</html>
