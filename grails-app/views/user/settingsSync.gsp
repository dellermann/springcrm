<html>
  <head>
    <meta name="layout" content="main" />
    <title
      ><g:message code="user.settings.sync.title" /> -
      <g:message code="user.settings.title"
    /></title>
    <meta name="caption" content="${message(code: 'user.settings.title')}" />
    <meta name="subcaption"
      content="${message(code: 'user.settings.sync.title')}" />
    <meta name="backLinkUrl" content="${createLink(action: 'settingsIndex')}" />
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarSettings" />
    </content>

    <g:render template="/layouts/flashMessage" />
    <g:render template="/layouts/errorMessage" />

    <g:form action="settingsSyncSave" elementId="config-form" method="post"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="user.settings.sync.fieldset.exclude.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <p><g:message code="user.settings.sync.hint" /></p>
            <div class="form-group">
              <label for="exclude-from-sync" class="control-label">
                <g:message code="user.settings.sync.excludeFromSync.label" />
              </label>
              <div class="control-container">
                <g:select name="excludeFromSync" id="exclude-from-sync-select"
                  from="${ratings}" value="${excludeFromSync}" optionKey="id"
                  optionValue="name" multiple="true"
                  />
              </div>
            </div>
          </div>
        </div>
      </section>
    </g:form>
  </body>
</html>
