<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="user.settings.language.title" /> -
    <g:message code="user.settings.title" /></title>
    <meta name="caption" content="${message(code: 'user.settings.title')}" />
    <meta name="subcaption"
      content="${message(code: 'user.settings.language.title')}" />
    <meta name="backLinkUrl" content="${createLink(action: 'settingsIndex')}" />
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarSettings" />
    </content>

    <g:render template="/layouts/flashMessage" />
    <g:render template="/layouts/errorMessage" />

    <form id="config-form"
      action="${createLink(action: 'settingsLanguageSave')}" method="post"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="user.settings.language.fieldset.language.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="locale" class="control-label">
                <g:message code="user.settings.language.language.label" />
              </label>
              <div class="control-container">
                <g:select name="locale" id="locale-select" from="${locales}"
                  value="${currentLocale}" optionKey="key" optionValue="value"
                  />
              </div>
            </div>
          </div>
        </div>
      </section>
    </form>
  </body>
</html>
