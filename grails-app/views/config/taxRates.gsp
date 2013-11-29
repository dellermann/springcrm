<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="config.taxRates.title" default="Tax rates" /></title>
  <r:require modules="configSelValues" />
</head>

<body>
  <header>
    <h1><g:message code="config.taxRates.title" default="Tax rates" /></h1>
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
    <g:form name="config-form" action="saveTaxRates"
      params="[returnUrl: params.returnUrl]">
      <fieldset>
        <header><h3><g:message code="config.fieldset.taxRates.label" default="Available tax rates" /></h3></header>
        <div>
          <div class="row">
            <div class="label">
              <label><g:message code="config.taxRates.label" default="Tax rates" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'taxRates', ' error')}">
              <div id="tax-rates-list" class="sel-values-list"
                data-list-type="taxRates"
                data-load-url="${createLink(action: 'loadTaxRates')}">
              </div>
              <ul class="field-msgs">
                <g:eachError bean="${configData}" field="taxRates">
                <li class="error-msg"><g:message error="${it}" /></li>
                </g:eachError>
              </ul>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </div>
  <g:render template="configSelValuesTemplate" />
</body>
</html>
