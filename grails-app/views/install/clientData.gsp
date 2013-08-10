<html>
<head>
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
</head>

<body>
  <header>
    <h1><g:message code="install.clientData.title" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="installBaseData" color="white" icon="arrow-left"
          message="install.btn.previous.label" /></li>
        <li><g:button color="green" class="submit-btn" icon="arrow-right"
          message="install.btn.next.label"
          data-form="client-data-form" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div class="install-description">
      <p><g:message code="install.clientData.description" /></p>
    </div>
    <g:hasErrors bean="${client}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <g:form name="client-data-form" action="clientDataSave">
      <fieldset>
        <header><h3><g:message code="install.clientData.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="form">
              <div class="row">
                <div class="label">
                  <label for="name"><g:message code="install.clientData.name.label" default="Name" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'name', ' error')}">
                  <g:textField name="name" value="${client.name}" />
                  <ul class="field-msgs">
                    <li class="info-msg"><g:message code="default.required" default="required" /></li>
                    <g:eachError bean="${client}" field="name">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="street"><g:message code="install.clientData.street.label" default="Street, no." /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'street', ' error')}">
                  <g:textField name="street" value="${client.street}" />
                  <ul class="field-msgs">
                    <li class="info-msg"><g:message code="default.required" default="required" /></li>
                    <g:eachError bean="${client}" field="street">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="postalCode"><g:message code="install.clientData.postalCode.label" default="Postal code" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'postalCode', ' error')}">
                  <g:textField name="postalCode" value="${client.postalCode}"
                    size="10" />
                  <ul class="field-msgs">
                    <li class="info-msg"><g:message code="default.required" default="required" /></li>
                    <g:eachError bean="${client}" field="postalCode">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="location"><g:message code="install.clientData.location.label" default="Location" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'location', ' error')}">
                  <g:textField name="location" value="${client.location}" />
                  <ul class="field-msgs">
                    <li class="info-msg"><g:message code="default.required" default="required" /></li>
                    <g:eachError bean="${client}" field="location">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <div class="col col-r">
            <div class="form">
              <div class="row">
                <div class="label">
                  <label for="phone"><g:message code="install.clientData.phone.label" default="Phone" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'phone', ' error')}">
                  <g:textField name="phone" value="${client.phone}" />
                  <ul class="field-msgs">
                    <li class="info-msg"><g:message code="default.required" default="required" /></li>
                    <g:eachError bean="${client}" field="phone">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="fax"><g:message code="install.clientData.fax.label" default="Fax" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'fax', ' error')}">
                  <g:textField name="fax" value="${client.fax}" />
                  <ul class="field-msgs">
                    <g:eachError bean="${client}" field="fax">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="email"><g:message code="install.clientData.email.label" default="E-mail" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'email', ' error')}">
                  <g:field type="email" name="email" value="${client.email}" />
                  <ul class="field-msgs">
                    <li class="info-msg"><g:message code="default.required" default="required" /></li>
                    <g:eachError bean="${client}" field="email">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="website"><g:message code="install.clientData.website.label" default="Website" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'website', ' error')}">
                  <g:field type="url" name="website"
                    value="${client.website}" />
                  <ul class="field-msgs">
                    <g:eachError bean="${client}" field="website">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <header><h3><g:message code="install.clientData.fieldset.bankData.label" /></h3></header>
        <div class="fieldset-content">
          <div class="form">
            <div class="row">
              <div class="label">
                <label for="bankName"><g:message code="install.clientData.bankName.label" default="Bank name" /></label>
              </div>
              <div class="field${hasErrors(bean: client, field: 'bankName', ' error')}">
                <g:textField name="bankName" value="${client.bankName}"
                  size="40"/>
                <ul class="field-msgs">
                  <g:eachError bean="${client}" field="bankName">
                  <li class="error-msg"><g:message error="${it}" /></li>
                  </g:eachError>
                </ul>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label for="bankCode"><g:message code="install.clientData.bankCode.label" default="Bank code" /></label>
              </div>
              <div class="field${hasErrors(bean: client, field: 'bankCode', ' error')}">
                <g:textField name="bankCode" value="${client.bankCode}"
                  size="40"/>
                <ul class="field-msgs">
                  <g:eachError bean="${client}" field="bankCode">
                  <li class="error-msg"><g:message error="${it}" /></li>
                  </g:eachError>
                </ul>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label for="accountNumber"><g:message code="install.clientData.accountNumber.label" default="Account number" /></label>
              </div>
              <div class="field${hasErrors(bean: client, field: 'accountNumber', ' error')}">
                <g:textField name="accountNumber"
                  value="${client.accountNumber}" size="40"/>
                <ul class="field-msgs">
                  <g:eachError bean="${client}" field="accountNumber">
                  <li class="error-msg"><g:message error="${it}" /></li>
                  </g:eachError>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </div>
</body>
</html>
