<r:require modules="noteForm" />
<fieldset>
  <h4><g:message code="note.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <f:field bean="${noteInstance}" property="number" />
      <f:field bean="${noteInstance}" property="title" />
    </div>
    <div class="col col-r">
      <f:field bean="${noteInstance}" property="organization" />
      <f:field bean="${noteInstance}" property="person" />
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="note.fieldset.content.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <f:field bean="${noteInstance}" property="content" />
    </div>
  </div>
</fieldset>