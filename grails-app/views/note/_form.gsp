<r:require modules="noteForm" />
<fieldset>
  <header><h3><g:message code="note.fieldset.general.label" /></h3></header>
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
  <header><h3><g:message code="note.fieldset.content.label" /></h3></header>
  <div class="form-fragment">
    <div class="row">
      <f:field bean="${noteInstance}" property="content" />
    </div>
  </div>
</fieldset>
