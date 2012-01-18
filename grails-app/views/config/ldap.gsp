<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.ldap.title" default="LDAP server" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.ldap.title" default="LDAP server" /></h2>
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
        <h4><g:message code="config.fieldset.ldapHost.label" default="LDAP host settings" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label">
              <label for="ldapHost"><g:message code="config.ldapHost.label" default="Host name" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapHost', ' error')}">
              <g:textField name="config.ldapHost" value="${configData.ldapHost}" size="40" /><span class="field-hint"><g:message code="config.ldapHost.hint" /></span>
              <g:hasErrors bean="${configData}" field="ldapHost">
                <span class="error-msg"><g:eachError bean="${configData}" field="ldapHost"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </div>
          </div>
          <div class="row">
            <div class="label">
              <label for="ldapPort"><g:message code="config.ldapPort.label" default="Port" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapPort', ' error')}">
              <g:textField name="config.ldapPort" value="${configData.ldapPort}" size="5" /><span class="field-hint"><g:message code="config.ldapPort.hint" /></span>
              <g:hasErrors bean="${configData}" field="ldapPort">
                <span class="error-msg"><g:eachError bean="${configData}" field="ldapPort"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </div>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <h4><g:message code="config.fieldset.ldapBind.label" default="LDAP binding" /></h4>
        <div class="fieldset-content">
          <p><g:message code="config.ldapBind.description" /></p>
          <div class="row">
            <div class="label">
              <label for="ldapBindDn"><g:message code="config.ldapBindDn.label" default="Bind DN" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapBindDn', ' error')}">
              <g:textField name="config.ldapBindDn" value="${configData.ldapBindDn}" size="100" />
              <g:hasErrors bean="${configData}" field="ldapBindDn">
                <span class="error-msg"><g:eachError bean="${configData}" field="ldapBindDn"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </div>
          </div>
          <div class="row">
            <div class="label">
              <label for="ldapBindPasswd"><g:message code="config.ldapBindPasswd.label" default="Bind password" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapBindPasswd', ' error')}">
              <g:passwordField name="config.ldapBindPasswd" size="20" />
              <g:hasErrors bean="${configData}" field="ldapBindPasswd">
                <span class="error-msg"><g:eachError bean="${configData}" field="ldapBindPasswd"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </div>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <h4><g:message code="config.fieldset.ldapNodes.label" default="LDAP nodes" /></h4>
        <div class="fieldset-content">
          <p><g:message code="config.ldapNodes.description" /></p>
          <div class="row">
            <div class="label">
              <label for="ldapContactDn"><g:message code="config.ldapContactDn.label" default="DN für Kontakte" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'ldapContactDn', ' error')}">
              <g:textField name="config.ldapContactDn" value="${configData.ldapContactDn}" size="100" />
              <g:hasErrors bean="${configData}" field="ldapContactDn">
                <span class="error-msg"><g:eachError bean="${configData}" field="ldapContactDn"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
