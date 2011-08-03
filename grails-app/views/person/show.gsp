
<%@ page import="org.amcworld.springcrm.Person" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
  <g:set var="entitiesName" value="${message(code: 'person.plural', default: 'Persons')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <li><g:link action="edit" id="${personInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${personInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${personInstance?.id}" class="red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><g:link controller="call" action="create" params="['person.id':personInstance?.id]" class="button medium white"><g:message code="person.action.createCall.label" /></g:link></li>
      <li><g:link action="gdatasync" params="[id:personInstance?.id]" class="button medium white"><g:message code="person.action.gdataExport.label"/></g:link></li>
      <li><g:link action="ldapexport" params="[id:personInstance?.id]" class="button medium white"><g:message code="person.action.ldapExport.label"/></g:link></li>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <h3>${personInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="person.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="person.number.label" default="Number" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "fullNumber")}</div>
			</div>
            
            <div class="row">
              <div class="label"><g:message code="person.organization.label" default="Organization" /></div>
              <div class="field">
                <g:link controller="organization" action="show" id="${personInstance?.organization?.id}">${personInstance?.organization?.encodeAsHTML()}</g:link>
			  </div>
			</div>
            
            <div class="row">
              <div class="label"><g:message code="person.salutation.label" default="Salutation" /></div>
              <div class="field">${personInstance?.salutation?.encodeAsHTML()}</div>
			</div>
            
            <div class="row">
              <div class="label"><g:message code="person.firstName.label" default="First Name" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "firstName")}</div>
			</div>
            
            <div class="row">
              <div class="label"><g:message code="person.lastName.label" default="Last Name" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "lastName")}</div>
			</div>
            
            <div class="row">
              <div class="label"><g:message code="person.jobTitle.label" default="Job Title" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "jobTitle")}</div>
			</div>
            
            <div class="row">
              <div class="label"><g:message code="person.department.label" default="Department" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "department")}</div>
			</div>
            
            <div class="row">
              <div class="label"><g:message code="person.assistant.label" default="Assistant" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "assistant")}</div>
			</div>
            
            <div class="row">
              <div class="label"><g:message code="person.birthday.label" default="Birthday" /></div>
              <div class="field"><g:formatDate date="${personInstance?.birthday}" type="date" /></div>
			</div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="person.phone.label" default="Phone" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "phone")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="person.phoneHome.label" default="Phone Home" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "phoneHome")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="person.mobile.label" default="Mobile" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "mobile")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="person.fax.label" default="Fax" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "fax")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="person.phoneAssistant.label" default="Phone Assistant" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "phoneAssistant")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="person.phoneOther.label" default="Phone Other" /></div>
              <div class="field">${fieldValue(bean: personInstance, field: "phoneOther")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="person.email1.label" default="Email1" /></div>
              <div class="field"><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}">${fieldValue(bean: personInstance, field: "email1")}</a></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="person.email2.label" default="Email2" /></div>
              <div class="field"><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}">${fieldValue(bean: personInstance, field: "email2")}</a></div>
            </div>
          </div>
        </div>
      </div>
      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="person.fieldset.mailingAddr.label" /></h4>
            <div class="fieldset-content">
              <div class="row">
                <div class="label"><g:message code="person.mailingAddrStreet.label" default="Street" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "mailingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.mailingAddrPoBox.label" default="Po Box" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "mailingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.mailingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "mailingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.mailingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "mailingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.mailingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "mailingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.mailingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "mailingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: personInstance, field: 'mailingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&q=${fieldValue(bean: personInstance, field: 'mailingAddr').encodeAsURL()}" target="_blank" class="button medium blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="person.fieldset.otherAddr.label" /></h4>
            <div class="fieldset-content">
              <div class="row">
                <div class="label"><g:message code="person.otherAddrStreet.label" default="Other Addr Street" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "otherAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.otherAddrPoBox.label" default="Other Addr Po Box" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "otherAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.otherAddrPostalCode.label" default="Other Addr Postal Code" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "otherAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.otherAddrLocation.label" default="Other Addr Location" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "otherAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.otherAddrState.label" default="Other Addr State" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "otherAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="person.otherAddrCountry.label" default="Other Addr Country" /></div>
                <div class="field">${fieldValue(bean: personInstance, field: "otherAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: personInstance, field: 'otherAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&q=${fieldValue(bean: personInstance, field: 'otherAddr').encodeAsURL()}" target="_blank" class="button medium blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>
      <g:if test="${personInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="person.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="person.notes.label" default="Notes" /></div>
            <div class="field">${fieldValue(bean: personInstance, field: "notes")}</div>
          </div>
        </div>
      </div>
      </g:if>

      <div class="fieldset">
        <div class="header-with-menu">
          <h4><g:message code="call.plural" /></h4>
          <div class="menu">
            <g:link controller="call" action="create" params="['person.id':personInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'call.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content">
          <g:if test="${personInstance?.calls}">
          <table class="content-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="call-multop-sel" class="multop-sel" /></th>
                <g:sortableColumn property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" />
                <g:sortableColumn property="start" title="${message(code: 'call.start.label', default: 'Start')}" />
                <g:sortableColumn property="type" title="${message(code: 'call.type.label', default: 'Type')}" />
                <g:sortableColumn property="status" title="${message(code: 'call.status.label', default: 'Status')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${personInstance.calls}" status="i" var="callInstance">
              <tr>
                <td><input type="checkbox" id="call-multop-${callInstance.id}" class="multop-sel-item" /></td>
                <td><g:link action="show" id="${callInstance.id}">${fieldValue(bean: callInstance, field: "subject")}</g:link></td>
                <td><g:formatDate date="${callInstance.start}" style="SHORT" /></td>
                <td><g:message code="call.type.${callInstance?.type}" /></td>
                <td><g:message code="call.status.${callInstance?.status}" /></td>
                <td>
                  <g:link action="edit" id="${callInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
                  <g:link action="delete" id="${callInstance?.id}" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
          <div class="paginator">
            <g:paginate total="${personInstance.calls.size()}" />
          </div>
          </g:if>
          <g:else>
            <div class="empty-list-inline">
              <p><g:message code="default.list.empty" /></p>
            </div>
          </g:else>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: personInstance?.dateCreated, style: 'SHORT'), formatDate(date: personInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
