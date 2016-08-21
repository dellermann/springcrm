<g:applyLayout name="field">
  <g:if test="${'textarea' == constraints?.widget}">
  <div class="textarea-container">
    <textarea name="${property}" id="${property}" class="form-control"
      rows="${rows ?: 5}"${required ? ' required="required"' : ''}
      aria-controls="${property}-toolbar" aria-live="polite"
      aria-relevant="text">${value}</textarea>
    <div id="${property}-toolbar" class="textarea-toolbar" role="toolbar"
      aria-hidden="true">
      <div class="textarea-toolbar-content">
        <select class="boilerplate-selector" style="width: 200px;"
          placeholder="${message(code: 'default.boilerplates')}"
          aria-controls="${property}"></select>
        <button type="button" class="btn btn-sm markdown-help-btn"
          title="${message(code: 'default.markdownHelp')}"
          aria-controls="markdown-help">
          <i class="fa fa-question-circle"></i>
          <span class="sr-only"><g:message code="default.markdownHelp"/></span>
        </button>
      </div>
    </div>
  </div>
  </g:if>
  <g:elseif test="${constraints?.password}">
  <f:widget type="password" bean="${bean}" property="${property}"
    class="form-control" />
  </g:elseif>
  <g:else>
  <f:widget type="${constraints?.widget ?: 'text'}" bean="${bean}"
    property="${property}" class="form-control" />
  </g:else>
</g:applyLayout>
