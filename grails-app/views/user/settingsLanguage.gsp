<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="user.settings.language.title" default="Language" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="user.settings.language.title" default="Language" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green submit-btn" data-form="config-form"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="settingsIndex" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:form name="config-form" action="settingsLanguageSave">
      <fieldset>
        <h4><g:message code="user.settings.language.fieldset.language.label" default="Language settings" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="form">
              <div class="row">
                <div class="label">
                  <label for="language"><g:message code="user.settings.language.language.label" default="Language" /></label>
                </div>
                <div class="field">
                  <g:select name="language" from="${locales}" value="${currentLocale}" optionKey="key" optionValue="value" />
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
