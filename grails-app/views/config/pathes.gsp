<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.pathes.title" default="Pathes" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.pathes.title" default="Pathes" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green submit-btn" data-form="config-form"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="index" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:form name="config-form" action="save" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <h4><g:message code="config.fieldset.pathes.documents.label" default="Document pathes" /></h4>
        <div class="fieldset-content">
          <p><g:message code="config.pathes.documents.description" args="${[grailsApplication.config.springcrm.dir.documents]}" /></p>
          <div class="row">
            <div class="label">
              <label for="number"><g:message code="config.pathes.documents.label" default="Path to documents" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'pathDocumentByOrg', ' error')}">
              <g:textField name="config.pathDocumentByOrg" value="${configData.pathDocumentByOrg ?: '%o'}" size="70" />
              <g:hasErrors bean="${configData}" field="pathDocumentByOrg">
                <span class="error-msg"><g:eachError bean="${configData}" field="pathDocumentByOrg"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
