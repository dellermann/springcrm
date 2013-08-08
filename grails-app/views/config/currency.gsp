<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="config.currency.title" default="Currency" /></title>
</head>

<body>
  <header>
    <h1><g:message code="config.currency.title" default="Currency" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button color="green" class="submit-btn" icon="save"
          data-form="config-form" message="default.button.save.label" /></li> 
        <li><g:button action="index" back="true" color="red"
          icon="remove-circle" message="default.button.cancel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:form name="config-form" action="save" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <header><h3><g:message code="config.fieldset.currency.label" default="Currency settings" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="form">
              <div class="row">
                <div class="label">
                  <label for="number"><g:message code="config.currency.label" default="Currency" /></label>
                </div>
                <div class="field">
                  <g:select name="config.currency" from="${currencies}"
                    optionKey="key" optionValue="value"
                    value="${currentCurrency}" />
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label for="numFractionDigits"><g:message code="config.numFractionDigits.label" default="Num. of fraction digits" /></label>
                </div>
                <div class="field">
                  <g:select name="config.numFractionDigits" from="${0..10}"
                    value="${numFractionDigits}" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </div>
</body>
</html>
