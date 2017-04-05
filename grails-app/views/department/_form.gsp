<section>
  <header>
    <h3><g:message code="department.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${departmentInstance}" property="name"/>
      <f:field bean="${departmentInstance}" property="costCenter"/>
    </div>
    <div class="column">
      <f:field bean="${departmentInstance}" property="manager"/>
    </div>
  </div>
</section>
