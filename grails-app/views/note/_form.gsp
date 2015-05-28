<section>
  <header>
    <h3><g:message code="note.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${noteInstance}" property="number" />
      <f:field bean="${noteInstance}" property="title" />
    </div>
    <div class="column">
      <f:field bean="${noteInstance}" property="organization" />
      <f:field bean="${noteInstance}" property="person" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="note.fieldset.content.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${noteInstance}" property="content" />
    </div>
  </div>
</section>
