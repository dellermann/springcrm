<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.taxRates.title" default="Tax rates" /></title>
  <r:require modules="configSelValues" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.taxRates.title" default="Tax rates" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green submit-btn" data-form="config-form"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="index" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:form name="config-form" action="save" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <h4><g:message code="config.fieldset.taxRates.label" default="Available tax rates" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label">
              <label><g:message code="config.taxRates.label" default="Tax rates" /></label>
            </div>
            <div class="field${hasErrors(bean: configData, field: 'taxRates', ' error')}">
              <div id="tax-rates-list" class="sel-values-list" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'taxClass'])}" data-save-sel-values-url="${createLink(action: 'saveSelValues', params: [type: 'taxClass'])}">
              </div>
              <g:hasErrors bean="${configData}" field="taxRates">
                <span class="error-msg"><g:eachError bean="${configData}" field="taxRates"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
