<html>
<head>
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
</head>

<body>
  <header>
    <h1><g:message code="install.createAdmin.title" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="clientData" color="white" icon="arrow-left"
          message="install.btn.previous.label" /></li>
        <li><g:button color="green" class="submit-btn" icon="arrow-right"
          message="install.btn.next.label" data-form="user-form" /></li>
      </ul>
    </nav>
  </header>
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
        <header><h3><g:message code="install.createAdmin.fieldset.general.label" /></h3></header>
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
                  <g:passwordField name="passwordRepeat" />
                  <ul class="field-msgs">
                    <li class="info-msg"><g:message code="default.required" default="required" /></li>
                  </ul>
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
