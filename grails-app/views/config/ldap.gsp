<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="config.ldap.title" default="LDAP server" /></title>
</head>

<body>
  <header>
    <h1><g:message code="config.ldap.title" default="LDAP server" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button color="green" class="submit-btn" icon="save"
          data-form="config-form" message="default.button.save.label" /></li> 
        <li><g:button action="index" back="true" color="red"
          icon="remove-circle" message="default.button.cancel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:form name="config-form" action="save" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <header><h3><g:message code="config.fieldset.ldapHost.label" default="LDAP host settings" /></h3></header>
        <div>
          <div class="row">
            <div class="label">
              <label for="ldapHost"><g:message code="config.ldapHost.label" default="Host name" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapHost', ' error')}">
              <div class="field-text">
                <span class="input"><g:textField name="config.ldapHost" value="${configData.ldapHost}" size="40" /></span>
                <span class="hint"><g:message code="config.ldapHost.hint" /></span>
              </div>
              <ul class="field-msgs">
                <li class="info-msg"><g:message code="default.required" default="required" /></li>
                <g:eachError bean="${configData}" field="ldapHost">
                <li class="error-msg"><g:message error="${it}" /></li>
                </g:eachError>
              </ul>
            </div>
          </div>
          <div class="row">
            <div class="label">
              <label for="ldapPort"><g:message code="config.ldapPort.label" default="Port" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapPort', ' error')}">
              <div class="field-text">
                <span class="input"><g:textField name="config.ldapPort" value="${configData.ldapPort}" size="5" /></span>
                <span class="hint"><g:message code="config.ldapPort.hint" /></span>
              </div>
              <ul class="field-msgs">
                <g:eachError bean="${configData}" field="ldapPort">
                <li class="error-msg"><g:message error="${it}" /></li>
                </g:eachError>
              </ul>
            </div>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <header><h3><g:message code="config.fieldset.ldapBind.label" default="LDAP binding" /></h3></header>
        <div>
          <p><g:message code="config.ldapBind.description" /></p>
          <div class="row">
            <div class="label">
              <label for="ldapBindDn"><g:message code="config.ldapBindDn.label" default="Bind DN" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapBindDn', ' error')}">
              <g:textField name="config.ldapBindDn" value="${configData.ldapBindDn}" size="100" />
              <ul class="field-msgs">
                <g:eachError bean="${configData}" field="ldapBindDn">
                <li class="error-msg"><g:message error="${it}" /></li>
                </g:eachError>
              </ul>
            </div>
          </div>
          <div class="row">
            <div class="label">
              <label for="ldapBindPasswd"><g:message code="config.ldapBindPasswd.label" default="Bind password" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapBindPasswd', ' error')}">
              <g:passwordField name="config.ldapBindPasswd" size="20" />
              <ul class="field-msgs">
                <g:eachError bean="${configData}" field="ldapBindPasswd">
                <li class="error-msg"><g:message error="${it}" /></li>
                </g:eachError>
              </ul>
            </div>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <header><h3><g:message code="config.fieldset.ldapNodes.label" default="LDAP nodes" /></h3></header>
        <div>
          <p><g:message code="config.ldapNodes.description" /></p>
          <div class="row">
            <div class="label">
              <label for="ldapContactDn"><g:message code="config.ldapContactDn.label" default="DN für Kontakte" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapContactDn', ' error')}">
              <g:textField name="config.ldapContactDn" value="${configData.ldapContactDn}" size="100" />
              <ul class="field-msgs">
                <g:eachError bean="${configData}" field="ldapContactDn">
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
