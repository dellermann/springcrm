<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="config.mail.title" default="Mail settings" /></title>
  <r:require modules="configMail" />
</head>

<body>
  <header>
    <h1><g:message code="config.mail.title" default="Mail settings" /></h1>
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
      <div class="multicol-content">
        <div class="col col-l">
          <fieldset>
            <header><h3><g:message code="config.fieldset.mail.activation.label" /></h3></header>
            <div>
              <div class="row">
                <div class="label">
                  <label for="mail-use-config-user"><g:message code="config.mail.activation.label" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'mailUseConfig', ' error')}">
                  <dl class="checkbox-area">
                    <dt>
                      <g:radio id="mail-use-config-none" name="config.mailUseConfig"
                        value="null" checked="${configData.mailUseConfig == null}" />
                    </dt>
                    <dd>
                      <div class="field-text">
                        <span class="label"><label for="mail-use-config-none"><g:message code="config.mail.activation.none" /></label></span>
                      </div>
                    </dd>
                    <dt>
                      <g:radio id="mail-use-config-system"
                        name="config.mailUseConfig" value="false"
                        checked="${configData.mailUseConfig == 'false'}" />
                    </dt>
                    <dd>
                      <div class="field-text">
                        <span class="label"><label for="mail-use-config-system"><g:message code="config.mail.activation.system" /></label></span>
                      </div>
                    </dd>
                    <dt>
                      <g:radio id="mail-use-config-user" name="config.mailUseConfig"
                        value="true" checked="${configData.mailUseConfig == 'true'}" />
                    </dt>
                    <dd>
                      <div class="field-text">
                        <span class="label"><label for="mail-use-config-user"><g:message code="config.mail.activation.user" /></label></span>
                      </div>
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
        <div class="col col-r">
          <fieldset>
            <header><h3><g:message code="config.fieldset.mail.host.label" /></h3></header>
            <div>
              <div class="row">
                <div class="label">
                  <label for="host"><g:message code="config.mail.host.label" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'mailHost', ' error')}">
                  <g:textField id="host" name="config.mailHost" value="${configData.mailHost}" size="50" />
                  <ul class="field-msgs">
                    <g:eachError bean="${configData}" field="mailHost">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="port"><g:message code="config.mail.port.label" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'mailPort', ' error')}">
                  <div class="field-text">
                    <span class="input"><g:textField id="port" name="config.mailPort" value="${configData.mailPort ?: 587}" size="5" /></span>
                    <span class="hint"><g:message code="config.mail.port.default" /></span>
                  </div>
                  <ul class="field-msgs">
                    <g:eachError bean="${configData}" field="mailPort">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
      </div>
      <div class="multicol-content">
        <div class="col col-l">
          <fieldset>
            <header><h3><g:message code="config.fieldset.mail.auth.label" /></h3></header>
            <div>
              <div class="row">
                <div class="label">
                  <label for="mail-auth-true"><g:message code="config.mail.auth.label" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'mailAuth', ' error')}">
                  <dl class="checkbox-area">
                    <dt>
                      <g:radio id="mail-auth-false" name="config.mailAuth"
                        value="false" checked="${configData.mailAuth != 'true'}" />
                    </dt>
                    <dd>
                      <div class="field-text">
                        <span class="label"><label for="mail-auth-false"><g:message code="config.mail.auth.false" /></label></span>
                      </div>
                    </dd>
                    <dt>
                      <g:radio id="mail-auth-true" name="config.mailAuth"
                        value="true" checked="${configData.mailAuth == 'true'}" />
                    </dt>
                    <dd>
                      <div class="field-text">
                        <span class="label"><label for="mail-auth-true"><g:message code="config.mail.auth.true" /></label></span>
                      </div>
                    </dd>
                  </dl>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="user-name"><g:message code="config.mail.userName.label" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'mailUserName', ' error')}">
                  <g:textField id="user-name" name="config.mailUserName" value="${configData.mailUserName}" size="50" />
                  <ul class="field-msgs">
                    <g:eachError bean="${configData}" field="mailUserName">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="password"><g:message code="config.mail.password.label" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'mailPassword', ' error')}">
                  <g:passwordField id="password" name="config.mailPassword" size="50" />
                  <ul class="field-msgs">
                    <g:eachError bean="${configData}" field="mailPassword">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
        <div class="col col-r">
          <fieldset>
            <header><h3><g:message code="config.fieldset.mail.security.label" /></h3></header>
            <div>
              <div class="row">
                <div class="label">
                  <label for="mail-encryption-starttls"><g:message code="config.mail.encryption.label" /></label>
                </div>
                <div class="field">
                  <dl class="checkbox-area">
                    <dt>
                      <g:radio id="mail-encryption-none" name="config.mailEncryption"
                        value="none" checked="${(configData.mailEncryption ?: 'none') == 'none'}" />
                    </dt>
                    <dd>
                      <div class="field-text">
                        <span class="label"><label for="mail-encryption-none"><g:message code="config.mail.encryption.none" /></label></span>
                      </div>
                    </dd>
                    <dt>
                      <g:radio id="mail-encryption-ssl" name="config.mailEncryption"
                        value="ssl" checked="${configData.mailEncryption == 'ssl'}" />
                    </dt>
                    <dd>
                      <div class="field-text">
                        <span class="label"><label for="mail-encryption-ssl"><g:message code="config.mail.encryption.ssl" /></label></span>
                      </div>
                    </dd>
                    <dt>
                      <g:radio id="mail-encryption-starttls" name="config.mailEncryption"
                        value="ssl" checked="${configData.mailEncryption == 'starttls'}" />
                    </dt>
                    <dd>
                      <div class="field-text">
                        <span class="label"><label for="mail-encryption-starttls"><g:message code="config.mail.encryption.starttls" /></label></span>
                      </div>
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
      </div>
    </g:form>
  </div>
</body>
</html>
