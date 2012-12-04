<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.currency.title" default="Currency" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.currency.title" default="Currency" /></h2>
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
        <h4><g:message code="config.fieldset.currency.label" default="Currency settings" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="form">
              <div class="row">
                <div class="label">
                  <label for="number"><g:message code="config.currencySymbol.label" default="Currency symbol" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'currency', ' error')}">
                  <g:select name="config.currency" from="${currencies}" optionKey="key" optionValue="value" value="${currentCurrency}" />
                  <g:hasErrors bean="${configData}" field="currency">
                    <span class="error-msg"><g:eachError bean="${configData}" field="currency"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
