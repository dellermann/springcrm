<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="config.pathes.title" default="Pathes" /></title>
</head>

<body>
  <header>
    <h1><g:message code="config.pathes.title" default="Pathes" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button color="green" class="submit-btn" icon="floppy-o"
          data-form="config-form" message="default.button.save.label" /></li>
        <li><g:button action="index" back="true" color="red"
          icon="times-circle-o" message="default.button.cancel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:form name="config-form" action="save" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <header><h3><g:message code="config.fieldset.pathes.documents.label" default="Document pathes" /></h3></header>
        <div>
          <p><g:message code="config.pathes.documents.description" args="${[grailsApplication.config.springcrm.dir.documents]}" /></p>
          <div class="row">
            <div class="label">
              <label for="number"><g:message code="config.pathes.documents.label" default="Path to documents" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'pathDocumentByOrg', ' error')}">
              <g:textField name="config.pathDocumentByOrg" value="${configData.pathDocumentByOrg ?: '%o'}" size="70" />
              <ul class="field-msgs">
                <g:eachError bean="${configData}" field="pathDocumentByOrg">
                <li class="error-msg"><g:message error="${it}" /></li>
                </g:eachError>
              </ul>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </div>
</body>
</html>
