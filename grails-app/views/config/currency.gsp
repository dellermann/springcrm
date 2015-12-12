<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="config.currency.title" /> -
    <g:message code="config.title" /></title>
    <meta name="caption" content="${message(code: 'config.title')}" />
    <meta name="subcaption" content="${message(code: 'config.currency.title')}" />
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarForm" model="[formName: 'config']" />
    </content>

    <g:render template="/layouts/flashMessage" />
    <g:render template="/layouts/errorMessage" />

    <g:form action="save" params="[returnUrl: params.returnUrl]"
      elementId="config-form" method="post"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="config.fieldset.currency.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="currency" class="control-label">
                <g:message code="config.currency.label" />
              </label>
              <div class="control-container">
                <g:select name="config.currency" id="currency-select"
                  from="${currencies}" optionKey="key" optionValue="value"
                  value="${currentCurrency}" />
              </div>
            </div>
          </div>
          <div class="column">
            <div class="form-group">
              <label for="numFractionDigits" class="control-label">
                <g:message code="config.numFractionDigits.label" />
              </label>
              <div class="control-container">
                <g:select name="config.numFractionDigits"
                  id="numFractionDigits-select" from="${0..10}"
                  value="${numFractionDigits}"
                  title="${message(code: 'config.numFractionDigits.title')}" />
              </div>
            </div>
            <div class="form-group">
              <label for="numFractionDigitsExt" class="control-label">
                <g:message code="config.numFractionDigitsExt.label" />
              </label>
              <div class="control-container">
                <g:select name="config.numFractionDigitsExt"
                  id="numFractionDigitsExt-select" from="${0..10}"
                  value="${numFractionDigitsExt}"
                  title="${message(code: 'config.numFractionDigitsExt.title')}" />
              </div>
            </div>
          </div>
        </div>
      </section>
    </g:form>
  </body>
</html>
