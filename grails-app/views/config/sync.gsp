<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="config.sync.title" /> -
    <g:message code="config.title" /></title>
    <meta name="caption" content="${message(code: 'config.title')}" />
    <meta name="subcaption" content="${message(code: 'config.sync.title')}" />
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarForm" model="[formName: 'config']" />
    </content>

    <g:render template="/layouts/flashMessage" />
    <g:render template="/layouts/errorMessage" />

    <form id="config-form"
      action="${createLink(action: 'save', params: [returnUrl: params.returnUrl])}"
      method="post" class="form-horizontal data-form form-view">
      <section class="column-group">
        <div class="column">
          <header>
            <h3><g:message code="config.sync.contacts.title" /></h3>
          </header>
          <div class="column-content">
            <div class="form-group">
              <label for="config-contacts-frequency" class="control-label">
                <g:message code="config.sync.frequency.label" />
              </label>
              <div class="control-container">
                <div class="input-group">
                  <g:textField id="config-contacts-frequency"
                    name="config.syncContactsFrequency"
                    value="${configData.syncContactsFrequency ?: 5}"
                    class="form-control"
                    aria-describedby="config-contacts-frequency-unit" />
                  <span id="config-contacts-frequency-unit"
                    class="input-group-addon"
                    ><g:message code="config.sync.frequency.minutes"
                  /></span>
                </div>
                <ul class="control-messages"
                  ><g:eachError bean="${configData}"
                    field="syncContactsFrequency"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="config-contacts-options-allow-create"
                class="control-label">
                <g:message code="config.sync.options.label" />
              </label>
              <div class="control-container">
                <div class="checkbox">
                  <label>
                    <g:checkBox id="config-contacts-options-allow-create"
                      name="config.syncContactsOptionsAllowCreate" value="true"
                      checked="${configData.syncContactsOptionsAllowCreate == 'true'}" />
                    <g:message code="config.sync.options.allowCreate" />
                  </label>
                </div>
                <div class="checkbox">
                  <label>
                    <g:checkBox id="config-contacts-options-allow-modify"
                      name="config.syncContactsOptionsAllowModify" value="true"
                      checked="${(configData.syncContactsOptionsAllowModify ?: 'true') == 'true'}" />
                    <g:message code="config.sync.options.allowModify" />
                  </label>
                </div>
                <div class="checkbox">
                  <label>
                    <g:checkBox id="config-contacts-options-allow-delete"
                      name="config.syncContactsOptionsAllowDelete" value="true"
                      checked="${configData.syncContactsOptionsAllowDelete == 'true'}" />
                    <g:message code="config.sync.options.allowDelete" />
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="column">
          <header>
            <h3><g:message code="config.sync.calendarEvents.title" /></h3>
          </header>
          <div class="column-content">
            <div class="form-group">
              <label for="config-calendar-events-frequency"
                class="control-label">
                <g:message code="config.sync.frequency.label" />
              </label>
              <div class="control-container">
                <div class="input-group">
                  <g:textField id="config-calendar-events-frequency"
                    name="config.syncCalendarEventsFrequency"
                    value="${configData.syncCalendarEventsFrequency ?: 5}"
                    class="form-control"
                    aria-describedby="config-calendar-events-frequency-unit" />
                  <span id="config-calendar-events-frequency-unit"
                    class="input-group-addon"
                    ><g:message code="config.sync.frequency.minutes"
                  /></span>
                </div>
                <ul class="control-messages"
                  ><g:eachError bean="${configData}"
                    field="syncCalendarEventsFrequency"
                  ><li class="control-message-error"><g:message error="${it}" /></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="config-calendar-events-options-allow-create"
                class="control-label">
                <g:message code="config.sync.options.label" />
              </label>
              <div class="control-container">
                <div class="checkbox">
                  <label>
                    <g:checkBox
                      id="config-calendar-events-options-allow-create"
                      name="config.syncCalendarEventsOptionsAllowCreate"
                      value="true"
                      checked="${configData.syncCalendarEventsOptionsAllowCreate == 'true'}" />
                    <g:message code="config.sync.options.allowCreate" />
                  </label>
                </div>
                <div class="checkbox">
                  <label>
                    <g:checkBox id="config-calendar-events-options-allow-modify"
                      name="config.syncCalendarEventsOptionsAllowModify"
                      value="true"
                      checked="${(configData.syncCalendarEventsOptionsAllowModify ?: 'true') == 'true'}" />
                    <g:message code="config.sync.options.allowModify" />
                  </label>
                </div>
                <div class="checkbox">
                  <label>
                    <g:checkBox id="config-calendar-events-options-allow-delete"
                      name="config.syncCalendarEventsOptionsAllowDelete"
                      value="true"
                      checked="${configData.syncCalendarEventsOptionsAllowDelete == 'true'}" />
                    <g:message code="config.sync.options.allowDelete" />
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </form>
  </body>
</html>
