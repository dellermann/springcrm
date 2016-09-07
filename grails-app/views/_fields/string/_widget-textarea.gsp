<g:set var="toolbarItems"
  value="${toolbar?.split(/\s+/) ?: ['boilerplate', 'markdown-help']}"/>
<g:set var="noToolbar" value="${toolbarItems.contains('none')}"/>
<div class="textarea-container">
  <textarea id="${property}" name="${property}"
    class="form-control${noToolbar ? ' no-toolbar' : ''}"
    rows="${rows ?: 5}" ${required ? raw('required="required"') : ''}
    ${toolbarItems.contains('none') ? '' : raw("""
        aria-controls="${property}-toolbar" aria-live="polite"
        aria-relevant="text" data-toggle="popover" data-placement="bottom"
        data-container="body" data-trigger="manual"
      """)}
    >${value}</textarea>
  <g:unless test="${noToolbar}">
    <div id="${property}-toolbar" class="textarea-toolbar" role="toolbar"
      aria-hidden="true">
      <div class="textarea-toolbar-content">
        <g:if test="${toolbarItems.contains('boilerplate')}">
          <select class="boilerplate-selector" style="width: 200px;"
            placeholder="${message(code: 'default.boilerplates')}"
            aria-controls="${property}"></select>
          <button type="button" class="btn btn-sm boilerplate-add-btn"
            title="${message(code: 'default.boilerplate.add')}"
            aria-controls="add-boilerplate-modal">
            <i class="fa fa-floppy-o"></i>
            <span class="sr-only"
              ><g:message code="default.boilerplate.add"
            /></span>
          </button>
        </g:if>
        <g:if test="${toolbarItems.contains('markdown-help')}">
          <button type="button" class="btn btn-sm markdown-help-btn"
            title="${message(code: 'default.markdownHelp')}"
            aria-controls="markdown-help">
            <i class="fa fa-question-circle"></i>
            <span class="sr-only"
              ><g:message code="default.markdownHelp"
            /></span>
          </button>
        </g:if>
      </div>
    </div>
  </g:unless>
</div>
