<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.selValues.title" default="Editor for selector values" /></title>
  <r:require modules="configSelValues" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.selValues.title" default="Editor for selector values" /></h2>
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
    <g:form name="config-form" action="saveSelValues" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <h4><g:message code="config.fieldset.selValues.label" default="Available selector lists" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.salutation.label" default="Salutation for persons" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="salutation" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'salutation'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.orgType.label" default="Organization type" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="orgType" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'orgType'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.rating.label" default="Rating of organizations" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="rating" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'rating'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.unit.label" default="Unit" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="unit" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'unit'])}"></div>
              </div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.carrier.label" default="Carrier" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="carrier" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'carrier'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.industry.label" default="Industry" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="industry" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'industry'])}"></div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
