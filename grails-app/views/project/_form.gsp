<r:require modules="projectForm" />
<fieldset>
  <header><h3><g:message code="project.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${projectInstance}" property="number" />
        <f:field bean="${projectInstance}" property="title" />
        <f:field bean="${projectInstance}" property="phase" />
        <f:field bean="${projectInstance}" property="status" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${projectInstance}" property="organization" />
        <f:field bean="${projectInstance}" property="person" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="project.fieldset.description.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${projectInstance}" property="description" />
  </div>
</fieldset>
