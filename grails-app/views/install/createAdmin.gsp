<html>
  <head>
    <meta name="layout" content="install" />
    <title><g:message code="install.createAdmin.title" /></title>
  </head>

  <body>
    <content tag="toolbar">
      <g:button action="clientData" color="default" icon="arrow-left"
        class="hidden-xs" message="install.btn.previous.label" />
      <button type="submit" form="user-form" class="btn btn-success">
        <i class="fa fa-arrow-right"></i>
        <g:message code="install.btn.next.label" />
      </button>
      <g:button action="finish" color="info" icon="share"
        class="hidden-xs" message="install.btn.skip.label" />
    </content>

    <div class="install-description">
      <p><g:message code="install.createAdmin.description" /></p>
    </div>
    <g:form action="createAdminSave" method="post"
      elementId="user-form" name="user-form"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="install.createAdmin.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:field bean="${user}" property="username" />
            <f:field bean="${user}" property="password" />
            <div class="form-group">
              <label for="password-repeat" class="control-label">
                <g:message code="user.passwordRepeat.label" />
              </label>
              <div class="control-container">
                <g:passwordField id="password-repeat" name="passwordRepeat"
                  class="form-control" />
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                ></ul>
              </div>
            </div>
            <f:field bean="${user}" property="firstName" />
            <f:field bean="${user}" property="lastName" />
          </div>
          <div class="column">
            <f:field bean="${user}" property="phone" />
            <f:field bean="${user}" property="phoneHome" />
            <f:field bean="${user}" property="mobile" />
            <f:field bean="${user}" property="fax" />
            <f:field bean="${user}" property="email" />
          </div>
        </div>
      </section>
    </g:form>
  </body>
</html>
