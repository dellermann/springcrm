<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="user.settings.language.title" default="Language" /></title>
</head>

<body>
  <header>
    <h1><g:message code="user.settings.language.title" default="Language" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button color="green" class="submit-btn" icon="save"
          data-form="config-form" message="default.button.save.label" /></li> 
        <li><g:button action="settingsIndex" back="true" color="red"
          icon="remove-circle" message="default.button.cancel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:form name="config-form" action="settingsLanguageSave">
      <fieldset>
        <header><h3><g:message code="user.settings.language.fieldset.language.label" default="Language settings" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="form">
              <div class="row">
                <div class="label">
                  <label for="locale"><g:message code="user.settings.language.language.label" default="Language" /></label>
                </div>
                <div class="field">
                  <g:select name="locale" from="${locales}"
                    value="${currentLocale}" optionKey="key"
                    optionValue="value" />
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
