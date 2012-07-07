<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="install.createAdmin.title" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="clientData" class="white"><g:message code="install.btn.previous.label" /></g:link></li>
        <li><a href="#" class="green submit-btn" data-form="user-form"><g:message code="install.btn.next.label" /></a></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div class="install-description">
      <p><g:message code="install.createAdmin.description" /></p>
    </div>
    <g:hasErrors bean="${userInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <g:form name="user-form" action="createAdminSave">
      <fieldset>
        <h4><g:message code="install.createAdmin.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="form">
              <f:field bean="${userInstance}" property="userName" />
              <f:field bean="${userInstance}" property="password" />
              <div class="row">
                <div class="label">
                  <label for="password-repeat"><g:message code="user.passwordRepeat.label" default="Repeat password" /></label>
                </div>
                <div class="field">
                  <g:passwordField name="passwordRepeat" size="40" /><br />
                  <span class="info-msg"><g:message code="default.required" default="required" /></span>
                </div>
              </div>
              <f:field bean="${userInstance}" property="firstName" />
              <f:field bean="${userInstance}" property="lastName" />
            </div>
          </div>
          <div class="col col-r">
            <div class="form">
              <f:field bean="${userInstance}" property="phone" />
              <f:field bean="${userInstance}" property="phoneHome" />
              <f:field bean="${userInstance}" property="mobile" />
              <f:field bean="${userInstance}" property="fax" />
              <f:field bean="${userInstance}" property="email" />
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
