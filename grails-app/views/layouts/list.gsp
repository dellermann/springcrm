<content tag="toolbar">
  <div>
    <a href="#top" class="btn btn-default go-top-btn" role="button">
      <i class="fa fa-arrow-up"></i>
      <span class="sr-only"
        ><g:message code="default.button.top.label"
      /></span>
    </a>
    <g:button action="create" params="${createParams}"
      color="success" icon="plus-circle"
      message="default.button.create.label" />
    <g:pageProperty name="page.additionalToolbarButtons" />
  </div>
</content>

<g:render template="/layouts/flashMessage" />
<g:if test="${list}">
  <g:layoutBody />
</g:if>
<g:else>
  <div class="well well-lg empty-list">
    <p><g:message code="default.list.empty" /></p>
    <div class="buttons">
      <g:button action="create" params="${createParams}" color="success"
        icon="plus-circle" message="default.new.label" args="[entityName]" />
    </div>
  </div>
</g:else>
