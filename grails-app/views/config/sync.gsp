<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.sync.title" default="Google synchronization" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.sync.title" default="Google synchronization" /></h2>
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
      <div class="multicol-content">
        <div class="col col-l">
          <fieldset>
            <h4><g:message code="config.sync.contacts.title" default="Contacts synchronization" /></h4>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label">
                  <label for="config-contacts-frequency"><g:message code="config.sync.frequency.label" default="Frequency" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'syncContactsFrequency', ' error')}">
                  <g:textField id="config-contacts-frequency" name="config.syncContactsFrequency" value="${configData.syncContactsFrequency ?: 5}" size="5" /> <g:message code="config.sync.frequency.minutes" default="minute(s)" /><br />
                  <g:hasErrors bean="${configData}" field="syncContactsFrequency">
                    <span class="error-msg"><g:eachError bean="${configData}" field="syncContactsFrequency"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label><g:message code="config.sync.options.label" default="Options" /></label>
                </div>
                <div class="field">
                  <ul class="checkbox-area">
                    <li><g:checkBox id="config-contacts-options-allow-create" name="config.syncContactsOptionsAllowCreate" value="true" checked="${configData.syncContactsOptionsAllowCreate == 'true'}" /><label for="config-contacts-options-allow-create"><g:message code="config.sync.options.allowCreate" /></label></li>
                    <li><g:checkBox id="config-contacts-options-allow-modify" name="config.syncContactsOptionsAllowModify" value="true" checked="${(configData.syncContactsOptionsAllowModify ?: 'true') == 'true'}" /><label for="config-contacts-options-allow-modify"><g:message code="config.sync.options.allowModify" /></label></li>
                    <li><g:checkBox id="config-contacts-options-allow-delete" name="config.syncContactsOptionsAllowDelete" value="true" checked="${configData.syncContactsOptionsAllowDelete == 'true'}" /><label for="config-contacts-options-allow-delete"><g:message code="config.sync.options.allowDelete" /></label></li>
                  </ul>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
        <div class="col col-r">
          <fieldset>
            <h4><g:message code="config.sync.calendarEvents.title" default="Calendar events synchronization" /></h4>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label">
                  <label for="config-calendar-events-frequency"><g:message code="config.sync.frequency.label" default="Frequency" /></label>
                </div>
                <div class="field${hasErrors(bean: configData, field: 'syncCalendarEventsFrequency', ' error')}">
                  <g:textField id="config-calendar-events-frequency" name="config.syncCalendarEventsFrequency" value="${configData.syncCalendarEventsFrequency ?: 5}" size="5" /> <g:message code="config.sync.frequency.minutes" default="minute(s)" /><br />
                  <g:hasErrors bean="${configData}" field="syncCalendarEventsFrequency">
                    <span class="error-msg"><g:eachError bean="${configData}" field="syncCalendarEventsFrequency"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
              <div class="row">
                <div class="label">
                  <label><g:message code="config.sync.options.label" default="Options" /></label>
                </div>
                <div class="field">
                  <ul class="checkbox-area">
                    <li><g:checkBox id="config-calendar-events-options-allow-create" name="config.syncCalendarEventsOptionsAllowCreate" value="true" checked="${configData.syncCalendarEventsOptionsAllowCreate == 'true'}" /><label for="config-calendar-events-options-allow-create"><g:message code="config.sync.options.allowCreate" /></label></li>
                    <li><g:checkBox id="config-calendar-events-options-allow-modify" name="config.syncCalendarEventsOptionsAllowModify" value="true" checked="${(configData.syncCalendarEventsOptionsAllowModify ?: 'true') == 'true'}" /><label for="config-calendar-events-options-allow-modify"><g:message code="config.sync.options.allowModify" /></label></li>
                    <li><g:checkBox id="config-calendar-events-options-allow-delete" name="config.syncCalendarEventsOptionsAllowDelete" value="true" checked="${configData.syncCalendarEventsOptionsAllowDelete == 'true'}" /><label for="config-calendar-events-options-allow-delete"><g:message code="config.sync.options.allowDelete" /></label></li>
                  </ul>
                </div>
              </div>
            </div>
          </fieldset>
        </div>
      </div>
    </g:form>
  </section>
</body>
</html>
