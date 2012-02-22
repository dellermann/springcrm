<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="install.clientData.title" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="index" class="white"><g:message code="install.btn.previous.label" /></g:link></li>
        <li><a href="#" class="green submit-btn" data-form="client-data-form"><g:message code="install.btn.next.label" /></a></li>
      </ul>
    </nav>
  </div>
  <section id="content">
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
        <h4><g:message code="install.clientData.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="form">
              <div class="row">
                <div class="label">
                  <label for="name"><g:message code="install.clientData.name.label" default="Name" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'name', ' error')}">
                  <g:textField name="name" value="${client.name}" size="40"/><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
                  <g:hasErrors bean="${client}" field="name">
                  <span class="error-msg"><g:eachError bean="${client}" field="name"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="street"><g:message code="install.clientData.street.label" default="Street, no." /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'street', ' error')}">
                  <g:textField name="street" value="${client.street}" size="40"/><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
                  <g:hasErrors bean="${client}" field="street">
                  <span class="error-msg"><g:eachError bean="${client}" field="street"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="postalCode"><g:message code="install.clientData.postalCode.label" default="Postal code" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'postalCode', ' error')}">
                  <g:textField name="postalCode" value="${client.postalCode}" size="10"/><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
                  <g:hasErrors bean="${client}" field="postalCode">
                  <span class="error-msg"><g:eachError bean="${client}" field="postalCode"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="location"><g:message code="install.clientData.location.label" default="Location" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'location', ' error')}">
                  <g:textField name="location" value="${client.location}" size="40"/><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
                  <g:hasErrors bean="${client}" field="location">
                  <span class="error-msg"><g:eachError bean="${client}" field="location"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
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
                  <g:textField name="phone" value="${client.phone}" size="40"/><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
                  <g:hasErrors bean="${client}" field="phone">
                  <span class="error-msg"><g:eachError bean="${client}" field="phone"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="fax"><g:message code="install.clientData.fax.label" default="Fax" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'fax', ' error')}">
                  <g:textField name="fax" value="${client.fax}" size="40"/><br />
                  <g:hasErrors bean="${client}" field="fax">
                  <span class="error-msg"><g:eachError bean="${client}" field="fax"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="email"><g:message code="install.clientData.email.label" default="E-mail" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'email', ' error')}">
                  <g:field type="email" name="email" value="${client.email}" size="40"/><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
                  <g:hasErrors bean="${client}" field="email">
                  <span class="error-msg"><g:eachError bean="${client}" field="email"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="website"><g:message code="install.clientData.website.label" default="Website" /></label>
                </div>
                <div class="field${hasErrors(bean: client, field: 'website', ' error')}">
                  <g:field type="url" name="website" value="${client.website}" size="40"/><br />
                  <g:hasErrors bean="${client}" field="website">
                  <span class="error-msg"><g:eachError bean="${client}" field="website"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <h4><g:message code="install.clientData.fieldset.bankData.label" /></h4>
        <div class="fieldset-content">
          <div class="form">
            <div class="row">
              <div class="label">
                <label for="bankName"><g:message code="install.clientData.bankName.label" default="Bank name" /></label>
              </div>
              <div class="field${hasErrors(bean: client, field: 'bankName', ' error')}">
                <g:textField name="bankName" value="${client.bankName}" size="40"/><br />
                <g:hasErrors bean="${client}" field="bankName">
                <span class="error-msg"><g:eachError bean="${client}" field="bankName"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label for="bankCode"><g:message code="install.clientData.bankCode.label" default="Bank code" /></label>
              </div>
              <div class="field${hasErrors(bean: client, field: 'bankCode', ' error')}">
                <g:textField name="bankCode" value="${client.bankCode}" size="40"/><br />
                <g:hasErrors bean="${client}" field="bankCode">
                <span class="error-msg"><g:eachError bean="${client}" field="bankCode"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label for="accountNumber"><g:message code="install.clientData.accountNumber.label" default="Account number" /></label>
              </div>
              <div class="field${hasErrors(bean: client, field: 'accountNumber', ' error')}">
                <g:textField name="accountNumber" value="${client.accountNumber}" size="40"/><br />
                <g:hasErrors bean="${client}" field="accountNumber">
                <span class="error-msg"><g:eachError bean="${client}" field="bankCode"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
