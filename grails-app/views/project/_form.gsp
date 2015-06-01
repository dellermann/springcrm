<section>
  <header>
    <h3><g:message code="project.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${projectInstance}" property="number" />
      <f:field bean="${projectInstance}" property="title" />
      <f:field bean="${projectInstance}" property="phase" />
      <f:field bean="${projectInstance}" property="status" />
    </div>
    <div class="column">
      <f:field bean="${projectInstance}" property="organization" />
      <f:field bean="${projectInstance}" property="person" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="project.fieldset.description.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${projectInstance}" property="description" />
    </div>
  </div>
</section>
