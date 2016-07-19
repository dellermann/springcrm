<g:set var="entityName"
  value="${message(code: type + '.label', default: type)}" />

<content tag="toolbar">
  <g:button action="create" params="${createParams}" color="success"
    icon="plus-circle" message="default.button.create.label" />
  <g:pageProperty name="page.additionalToolbarButtons" />
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
