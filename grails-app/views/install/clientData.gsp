<html>
  <head>
    <meta name="layout" content="install" />
    <title><g:message code="install.clientData.title" /></title>
  </head>

  <body>
    <content tag="toolbar">
      <g:button action="installBaseData" color="default" icon="arrow-left"
        class="hidden-xs" message="install.btn.previous.label" />
      <button type="submit" form="client-data-form" class="btn btn-success">
        <i class="fa fa-arrow-right"></i>
        <g:message code="install.btn.next.label" />
      </button>
    </content>

    <div class="install-description">
      <p><g:message code="install.clientData.description" /></p>
    </div>
    <g:form action="clientDataSave" elementId="client-data-form"
      name="client-data-form" method="post"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="install.clientData.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="name" class="control-label">
                <g:message code="install.clientData.name.label" />
              </label>
              <div class="control-container">
                <g:textField name="name" value="${client.name}"
                  class="form-control" />
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${client}" field="name"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="street" class="control-label">
                <g:message code="install.clientData.street.label" />
              </label>
              <div class="control-container">
                <g:textField name="street" value="${client.street}"
                  class="form-control" />
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${client}" field="street"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="postalCode" class="control-label">
                <g:message code="install.clientData.postalCode.label" />
              </label>
              <div class="control-container">
                <g:textField name="postalCode" value="${client.postalCode}"
                  class="form-control" />
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${client}" field="postalCode"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="location" class="control-label">
                <g:message code="install.clientData.location.label" />
              </label>
              <div class="control-container">
                <g:textField name="location" value="${client.location}"
                  class="form-control" />
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${client}" field="location"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
          <div class="column">
            <div class="form-group">
              <label for="phone" class="control-label">
                <g:message code="install.clientData.phone.label" />
              </label>
              <div class="control-container">
                <g:textField name="phone" value="${client.phone}"
                  class="form-control" />
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${client}" field="phone"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="fax" class="control-label">
                <g:message code="install.clientData.fax.label" />
              </label>
              <div class="control-container">
                <g:textField name="fax" value="${client.fax}"
                  class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${client}" field="fax"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="email" class="control-label">
                <g:message code="install.clientData.email.label" />
              </label>
              <div class="control-container">
                <g:field type="email" name="email" value="${client.email}"
                  class="form-control" />
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${client}" field="email"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="website" class="control-label">
                <g:message code="install.clientData.website.label" />
              </label>
              <div class="control-container">
                <g:field type="url" name="website" value="${client.website}"
                  class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${client}" field="website"
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
          <h3
            ><g:message code="install.clientData.fieldset.bankData.label"
          /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="bankName" class="control-label">
                <g:message code="install.clientData.bankName.label" />
              </label>
              <div class="control-container">
                <g:textField name="bankName" value="${client.bankName}"
                  class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${client}" field="bankName"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="bankCode" class="control-label">
                <g:message code="install.clientData.bankCode.label" />
              </label>
              <div class="control-container">
                <g:textField name="bankCode" value="${client.bankCode}"
                  class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${client}" field="bankCode"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="accountNumber" class="control-label">
                <g:message code="install.clientData.accountNumber.label" />
              </label>
              <div class="control-container">
                <g:textField name="accountNumber"
                  value="${client.accountNumber}" class="form-control" />
                <ul class="control-messages"
                  ><g:eachError bean="${client}" field="accountNumber"
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
