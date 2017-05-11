<html>
  <head>
    <meta name="layout" content="main"/>
    <title><g:message code="user.settings.language.title"/> -
    <g:message code="user.settings.title"/></title>
    <meta name="caption" content="${message(code: 'user.settings.title')}"/>
    <meta name="subcaption"
      content="${message(code: 'user.settings.control.title')}"/>
    <meta name="backLinkUrl" content="${createLink(action: 'settingsIndex')}"/>
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarSettings"/>
    </content>

    <g:render template="/layouts/flashMessage"/>
    <g:render template="/layouts/errorMessage"/>

    <form id="config-form"
      action="${createLink(action: 'settingsControlSave')}" method="post"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="user.settings.control.fieldset.forms.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="save-type" class="control-label control-label-lg">
                <g:message code="user.settings.control.saveType.label"/>
              </label>
              <div class="control-container control-container-lg">
                <g:select name="saveType" id="save-type-select"
                  from="['save', 'saveAndClose']"
                  value="${saveType}"
                  valueMessagePrefix="user.settings.control.saveType"/>
              </div>
            </div>
          </div>
        </div>
      </section>
    </form>
  </body>
</html>
