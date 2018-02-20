<html>
  <head>
    <title><g:message code="config.tenant.title"/> -
    <g:message code="config.title"/></title>
    <meta name="caption" content="${message(code: 'config.title')}"/>
    <meta name="subcaption" content="${message(code: 'config.tenant.title')}"/>
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarFormSimple"
        model="[formName: 'config-form']"/>
    </content>

    <g:render template="/layouts/flashMessage"/>
    <g:render template="/layouts/errorMessage"/>

    <form id="config-form"
      action="${createLink(
        action: 'saveTenant', params: [returnUrl: params.returnUrl]
      )}"
      method="post" class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="install.tenantData.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="name" class="control-label">
                <g:message code="install.tenantData.name.label"/>
              </label>
              <div class="control-container">
                <g:textField name="name" value="${tenant.name}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${tenant}" field="name"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="street" class="control-label">
                <g:message code="install.tenantData.street.label"/>
              </label>
              <div class="control-container">
                <g:textField name="street" value="${tenant.street}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${tenant}" field="street"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="postalCode" class="control-label">
                <g:message code="install.tenantData.postalCode.label"/>
              </label>
              <div class="control-container">
                <g:textField name="postalCode" value="${tenant.postalCode}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${tenant}" field="postalCode"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="location" class="control-label">
                <g:message code="install.tenantData.location.label"/>
              </label>
              <div class="control-container">
                <g:textField name="location" value="${tenant.location}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${tenant}" field="location"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
          <div class="column">
            <div class="form-group">
              <label for="phone" class="control-label">
                <g:message code="install.tenantData.phone.label"/>
              </label>
              <div class="control-container">
                <g:textField name="phone" value="${tenant.phone}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${tenant}" field="phone"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="fax" class="control-label">
                <g:message code="install.tenantData.fax.label"/>
              </label>
              <div class="control-container">
                <g:textField name="fax" value="${tenant.fax}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><g:eachError bean="${tenant}" field="fax"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="email" class="control-label">
                <g:message code="install.tenantData.email.label"/>
              </label>
              <div class="control-container">
                <g:field type="email" name="email" value="${tenant.email}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${tenant}" field="email"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="website" class="control-label">
                <g:message code="install.tenantData.website.label"/>
              </label>
              <div class="control-container">
                <g:field type="url" name="website" value="${tenant.website}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><g:eachError bean="${tenant}" field="website"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3>
            <g:message code="install.tenantData.fieldset.bankData.label"/>
          </h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="bankName" class="control-label">
                <g:message code="install.tenantData.bankName.label"/>
              </label>
              <div class="control-container">
                <g:textField name="bankName" value="${tenant.bankName}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><g:eachError bean="${tenant}" field="bankName"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="bankCode" class="control-label">
                <g:message code="install.tenantData.bankCode.label"/>
              </label>
              <div class="control-container">
                <g:textField name="bankCode" value="${tenant.bankCode}"
                  class="form-control"/>
                <ul class="control-messages"
                  ><g:eachError bean="${tenant}" field="bankCode"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="accountNumber" class="control-label">
                <g:message code="install.tenantData.accountNumber.label"/>
              </label>
              <div class="control-container">
                <g:textField name="accountNumber"
                  value="${tenant.accountNumber}" class="form-control"/>
                <ul class="control-messages"
                  ><g:eachError bean="${tenant}" field="accountNumber"
                    ><li class="control-message-error"
                      ><g:message error="${it}"
                    /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
          </div>
        </div>
      </section>
    </form>
  </body>
</html>
