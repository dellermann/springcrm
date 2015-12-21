<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="config.paths.title" /> -
    <g:message code="config.title" /></title>
    <meta name="caption" content="${message(code: 'config.title')}" />
    <meta name="subcaption" content="${message(code: 'config.paths.title')}" />
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarForm" model="[formName: 'config']" />
    </content>

    <g:render template="/layouts/flashMessage" />
    <g:render template="/layouts/errorMessage" />

    <g:form action="save" elementId="config-form"
      params="[returnUrl: params.returnUrl]" method="post"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="config.fieldset.paths.documents.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <p>
              <g:message code="config.paths.documents.description"
                args="${[grailsApplication.config.springcrm.dir.documents]}"
                />
            </p>
            <div class="form-group">
              <label for="pathDocumentByOrg" class="control-label">
                <g:message code="config.paths.documents.label" />
              </label>
              <div class="control-container">
                <g:textField name="config.pathDocumentByOrg"
                  value="${configData.pathDocumentByOrg ?: '%o'}"
                  class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="pathDocumentByOrg"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
        </div>
      </section>
    </g:form>
  </body>
</html>
