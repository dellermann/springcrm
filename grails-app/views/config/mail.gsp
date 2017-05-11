<html>
  <head>
    <meta name="layout" content="main"/>
    <title><g:message code="config.mail.title"/> -
    <g:message code="config.title"/></title>
    <meta name="caption" content="${message(code: 'config.title')}"/>
    <meta name="subcaption" content="${message(code: 'config.mail.title')}"/>
    <meta name="stylesheet" content="config"/>
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarFormSimple"
        model="[formName: 'config-form']"/>
    </content>

    <g:render template="/layouts/flashMessage"/>
    <g:render template="/layouts/errorMessage"/>

    <form id="config-form"
      action="${createLink(action: 'save', params: [returnUrl: params.returnUrl])}"
      method="post" class="form-horizontal data-form form-view">
      <section class="column-group">
        <div class="column">
          <header>
            <h3><g:message code="config.fieldset.mail.activation.label"/></h3>
          </header>
          <div class="column-content">
            <div class="form-group">
              <label for="mail-use-config-user" class="control-label">
                <g:message code="config.mail.activation.label"/>
              </label>
              <div class="control-container">
                <div class="radio">
                  <label>
                    <g:radio id="mail-use-config-none"
                      name="config.mailUseConfig" value="null"
                      checked="${configData.mailUseConfig == null}"/>
                    <g:message code="config.mail.activation.none"/>
                  </label>
                </div>
                <div class="radio">
                  <label>
                    <g:radio id="mail-use-config-system"
                      name="config.mailUseConfig" value="false"
                      checked="${configData.mailUseConfig == 'false'}"/>
                    <g:message code="config.mail.activation.system"/>
                  </label>
                </div>
                <div class="radio">
                  <label>
                    <g:radio id="mail-use-config-user"
                      name="config.mailUseConfig" value="true"
                      checked="${configData.mailUseConfig == 'true'}"/>
                    <g:message code="config.mail.activation.user"/>
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="column">
          <header>
            <h3><g:message code="config.fieldset.mail.host.label"/></h3>
          </header>
          <div class="column-content">
            <div class="form-group">
              <label for="host" class="control-label">
                <g:message code="config.mail.host.label"/>
              </label>
              <div class="control-container">
                <g:textField id="host" name="config.mailHost"
                  value="${configData.mailHost}" class="form-control"/>
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="mailHost"
                  ><li class="control-message-error"><g:message error="${it}"/></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="port" class="control-label">
                <g:message code="config.mail.port.label"/>
              </label>
              <div class="control-container">
                <input type="number" id="port" name="config.mailPort"
                  value="${configData.mailPort ?: 587}" class="form-control"
                  maxlength="5" min="0" max="65535"/>
                <p class="form-control-static">
                  <small>(<g:message code="config.mail.port.default"/>)</small>
                </p>
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="mailPort"
                  ><li class="control-message-error"><g:message error="${it}"/></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="column-group">
        <div class="column">
          <header>
            <h3><g:message code="config.fieldset.mail.auth.label"/></h3>
          </header>
          <div class="column-content">
            <div class="form-group">
              <label for="mail-auth-true" class="control-label">
                <g:message code="config.mail.auth.label"/>
              </label>
              <div class="control-container">
                <div class="radio">
                  <label>
                    <g:radio id="mail-auth-false" name="config.mailAuth"
                      value="false"
                      checked="${configData.mailAuth != 'true'}"/>
                    <g:message code="config.mail.auth.false"/>
                  </label>
                </div>
                <div class="radio">
                  <label>
                    <g:radio id="mail-auth-true" name="config.mailAuth"
                      value="true"
                      checked="${configData.mailAuth == 'true'}"/>
                    <g:message code="config.mail.auth.true"/>
                  </label>
                </div>
              </div>
            </div>
            <div class="form-group">
              <label for="user-name" class="control-label">
                <g:message code="config.mail.userName.label"/>
              </label>
              <div class="control-container">
                <g:textField id="user-name" name="config.mailUserName"
                  value="${configData.mailUserName}" class="form-control"/>
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="mailUserName"
                  ><li class="control-message-error"><g:message error="${it}"/></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="password" class="control-label">
                <g:message code="config.mail.password.label"/>
              </label>
              <div class="control-container">
                <g:passwordField id="password" name="config.mailPassword"
                  class="form-control"/>
                <ul class="control-messages"
                  ><g:eachError bean="${configData}" field="mailPassword"
                  ><li class="control-message-error"><g:message error="${it}"/></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
        </div>
        <div class="column">
          <header>
            <h3><g:message code="config.fieldset.mail.security.label"/></h3>
          </header>
          <div class="column-content">
            <div class="form-group">
              <label for="mail-encryption-starttls" class="control-label">
                <g:message code="config.mail.encryption.label"/>
              </label>
              <div class="control-container">
                <div class="radio">
                  <label>
                    <g:radio id="mail-encryption-none" name="config.mailEncryption"
                      value="none"
                      checked="${(configData.mailEncryption ?: 'none') == 'none'}"/>
                    <g:message code="config.mail.encryption.none"/>
                  </label>
                </div>
                <div class="radio">
                  <label>
                    <g:radio id="mail-encryption-ssl" name="config.mailEncryption"
                      value="ssl"
                      checked="${configData.mailEncryption == 'ssl'}"/>
                    <g:message code="config.mail.encryption.ssl"/>
                  </label>
                </div>
                <div class="radio">
                  <label>
                    <g:radio id="mail-encryption-starttls" name="config.mailEncryption"
                      value="starttls"
                      checked="${configData.mailEncryption == 'starttls'}"/>
                    <g:message code="config.mail.encryption.starttls"/>
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </form>

    <content tag="scripts">
      <asset:javascript src="config-mail"/>
    </content>
  </body>
</html>
