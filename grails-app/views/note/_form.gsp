<r:require modules="noteForm" />
<fieldset>
  <h4><g:message code="note.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${noteInstance}" property="number" />
        <f:field bean="${noteInstance}" property="title" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${noteInstance}" property="organization" />
        <f:field bean="${noteInstance}" property="person" />
      </div>
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