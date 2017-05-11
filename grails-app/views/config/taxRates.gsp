<html>
  <head>
    <meta name="layout" content="main"/>
    <title><g:message code="config.taxRates.title"/> -
    <g:message code="config.title"/></title>
    <meta name="caption" content="${message(code: 'config.title')}"/>
    <meta name="subcaption"
      content="${message(code: 'config.taxRates.title')}"/>
    <meta name="stylesheet" content="config"/>
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarFormSimple"
        model="[formName: 'config-form']"/>
    </content>

    <g:render template="/layouts/flashMessage"/>
    <g:render template="/layouts/errorMessage"/>

    <form action="${createLink(action: 'saveTaxRates', params: [returnUrl: params.returnUrl])}"
      id="config-form" method="post"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="config.fieldset.taxRates.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label class="control-label">
                <g:message code="config.taxRates.label"/>
              </label>
              <div class="control-container">
                <div class="sel-values-list-container" data-list-type="taxRates"
                  data-load-url="${createLink(action: 'loadTaxRates')}">
                  <div class="sel-values-list"></div>
                  <input type="hidden" name="taxRates"/>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </form>

    <content tag="scripts">
      <asset:javascript src="config-sel-values"/>
    </content>
  </body>
</html>
