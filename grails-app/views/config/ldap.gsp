<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="config.ldap.title" /> -
    <g:message code="config.title" /></title>
    <meta name="caption" content="${message(code: 'config.title')}" />
    <meta name="subcaption" content="${message(code: 'config.ldap.title')}" />
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
          <h3><g:message code="config.fieldset.ldapHost.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="ldapHost" class="control-label">
                <g:message code="config.ldapHost.label" />
              </label>
              <div class="control-container">
                <g:textField name="config.ldapHost"
                  value="${configData.ldapHost}" class="form-control" />
                <p class="form-control-static">
                  <small><g:message code="config.ldapHost.hint" /></small>
                </p>
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${configData}" field="ldapHost"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="ldapPort" class="control-label">
                <g:message code="config.ldapPort.label" />
              </label>
              <div class="control-container">
                <input type="number" name="config.ldapPort"
                  value="${configData.ldapPort}" class="form-control"
                  maxlength="5" min="0" max="65535" />
                <p class="form-control-static">
                  <small><g:message code="config.ldapPort.hint" /></small>
                </p>
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="ldapPort"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="config.fieldset.ldapBind.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <p><g:message code="config.ldapBind.description" /></p>
            <div class="form-group">
              <label for="ldapBindDn" class="control-label">
                <g:message code="config.ldapBindDn.label" />
              </label>
              <div class="control-container">
                <g:textField name="config.ldapBindDn"
                  value="${configData.ldapBindDn}" class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="ldapBindDn"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="ldapBindPasswd" class="control-label">
                <g:message code="config.ldapBindPasswd.label" />
              </label>
              <div class="control-container">
                <g:passwordField name="config.ldapBindPasswd"
                  class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="ldapBindPasswd"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="config.fieldset.ldapNodes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <p><g:message code="config.ldapNodes.description" /></p>
            <div class="form-group">
              <label for="ldapContactDn" class="control-label">
                <g:message code="config.ldapContactDn.label" />
              </label>
              <div class="control-container">
                <g:textField name="config.ldapContactDn"
                  value="${configData.ldapContactDn}" class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="ldapContactDn"
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
