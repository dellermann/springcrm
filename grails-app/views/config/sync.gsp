<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="config.sync.title" default="Google synchronization" /></title>
</head>

<body>
  <header>
    <h1><g:message code="config.sync.title" default="Google synchronization" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button color="green" class="submit-btn" icon="save"
          data-form="config-form" message="default.button.save.label" /></li> 
        <li><g:button action="index" back="true" color="red"
          icon="remove-circle" message="default.button.cancel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:form name="config-form" action="save" params="[returnUrl: params.returnUrl]">
      <div class="multicol-content">
        <div class="col col-l">
          <fieldset>
            <header><h3><g:message code="config.sync.contacts.title" default="Contacts synchronization" /></h3></header>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label">
                  <label for="config-contacts-frequency"><g:message code="config.sync.frequency.label" default="Frequency" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'syncContactsFrequency', ' error')}">
                  <div class="field-text">
                    <span class="input">
                      <g:textField id="config-contacts-frequency"
                        name="config.syncContactsFrequency"
                        value="${configData.syncContactsFrequency ?: 5}"
                        size="5" />
                    </span>
                    <span><g:message code="config.sync.frequency.minutes" default="minute(s)" /></span>
                  </div>
                  <ul class="field-msgs">
                    <g:eachError bean="${configData}"
                      field="syncContactsFrequency">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label><g:message code="config.sync.options.label" default="Options" /></label>
                </div>
                <div class="field">
                  <dl class="checkbox-area">
                    <dt><g:checkBox id="config-contacts-options-allow-create"
                      name="config.syncContactsOptionsAllowCreate" value="true"
                      checked="${configData.syncContactsOptionsAllowCreate == 'true'}" /></dt>
                    <dd><label for="config-contacts-options-allow-create"><g:message code="config.sync.options.allowCreate" /></label></dd>
                    <dt><g:checkBox id="config-contacts-options-allow-modify"
                      name="config.syncContactsOptionsAllowModify" value="true"
                      checked="${(configData.syncContactsOptionsAllowModify ?: 'true') == 'true'}" /></dt>
                    <dd><label for="config-contacts-options-allow-modify"><g:message code="config.sync.options.allowModify" /></label></dd>
                    <dt><g:checkBox id="config-contacts-options-allow-delete"
                      name="config.syncContactsOptionsAllowDelete" value="true"
                      checked="${configData.syncContactsOptionsAllowDelete == 'true'}" /></dt>
                    <dd><label for="config-contacts-options-allow-delete"><g:message code="config.sync.options.allowDelete" /></label></dd>
                  </dl>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
        <div class="col col-r">
          <fieldset>
            <header><h3><g:message code="config.sync.calendarEvents.title" default="Calendar events synchronization" /></h3></header>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label">
                  <label for="config-calendar-events-frequency"><g:message code="config.sync.frequency.label" default="Frequency" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'syncCalendarEventsFrequency', ' error')}">
                  <div class="field-text">
                    <span class="input">
                      <g:textField id="config-calendar-events-frequency"
                        name="config.syncCalendarEventsFrequency"
                        value="${configData.syncCalendarEventsFrequency ?: 5}"
                        size="5" />
                    </span>
                    <span><g:message code="config.sync.frequency.minutes" default="minute(s)" /></span>
                  </div>
                  <ul class="field-msgs">
                    <g:eachError bean="${configData}" field="syncCalendarEventsFrequency">
                    <li class="error-msg"><g:message error="${it}" /></li>
                    </g:eachError>
                  </ul>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label><g:message code="config.sync.options.label" default="Options" /></label>
                </div>
                <div class="field">
                  <dl class="checkbox-area">
                    <dt><g:checkBox id="config-calendar-events-options-allow-create"
                      name="config.syncCalendarEventsOptionsAllowCreate"
                      value="true"
                      checked="${configData.syncCalendarEventsOptionsAllowCreate == 'true'}" /></dt>
                    <dd><label for="config-calendar-events-options-allow-create"><g:message code="config.sync.options.allowCreate" /></label></dd>
                    <dt><g:checkBox id="config-calendar-events-options-allow-modify"
                      name="config.syncCalendarEventsOptionsAllowModify"
                      value="true"
                      checked="${(configData.syncCalendarEventsOptionsAllowModify ?: 'true') == 'true'}" /></dt>
                    <dd><label for="config-calendar-events-options-allow-modify"><g:message code="config.sync.options.allowModify" /></label></dd>
                    <dt><g:checkBox id="config-calendar-events-options-allow-delete"
                      name="config.syncCalendarEventsOptionsAllowDelete"
                      value="true"
                      checked="${configData.syncCalendarEventsOptionsAllowDelete == 'true'}" /></dt>
                    <dd><label for="config-calendar-events-options-allow-delete"><g:message code="config.sync.options.allowDelete" /></label></dd>
                  </dl>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
      </div>
    </g:form>
  </div>
</body>
</html>
