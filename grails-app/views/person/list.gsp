<%@ page import="org.amcworld.springcrm.Person" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
  <g:set var="entitiesName" value="${message(code: 'person.plural', default: 'Persons')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li>
          <g:menuButton color="white" message="default.export">
            <li><g:link action="gdatasync"><g:message code="person.action.gdataExport.label" /></g:link></li>
            <li><g:link action="ldapexport"><g:message code="person.action.ldapExport.label" /></g:link></li>
          </g:menuButton>
        </li>
        <li><g:button action="create" color="green" icon="plus"
          message="default.new.label" args="[entityName]" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:if test="${personInstanceList}">
    <g:letterBar clazz="${Person}" property="lastName" />
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="person-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'person.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="lastName" title="${message(code: 'person.lastName.label', default: 'Last name')}" />
          <g:sortableColumn scope="col" property="firstName" title="${message(code: 'person.firstName.label', default: 'First name')}" />
          <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'person.organization.label', default: 'Organization')}" />
          <g:sortableColumn scope="col" property="phone" title="${message(code: 'person.phone.label', default: 'Phone')}" />
          <g:sortableColumn scope="col" property="email1" title="${message(code: 'person.email1.label', default: 'E-mail')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${personInstanceList}" status="i" var="personInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="person-row-selector-${personInstance.id}" data-id="${personInstance.id}" /></td>
          <td class="id person-number"><g:link action="show" id="${personInstance.id}"><g:fieldValue bean="${personInstance}" field="fullNumber" /></g:link></td>
          <td class="string person-last-name"><g:link action="show" id="${personInstance.id}"><g:fieldValue bean="${personInstance}" field="lastName" /></g:link></td>
          <td class="string person-first-name"><g:fieldValue bean="${personInstance}" field="firstName" /></td>
          <td class="ref person-organization"><g:link controller="organization" action="show" id="${personInstance.organization.id}"><g:fieldValue bean="${personInstance}" field="organization" /></g:link></td>
          <td class="string person-phone"><a href="tel:<g:fieldValue bean="${personInstance}" field="phone" />"><g:fieldValue bean="${personInstance}" field="phone" /></a></td>
          <td class="string person-email1"><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}"><g:fieldValue bean="${personInstance}" field="email1" /></a></td>
          <td class="action-buttons">
            <g:button action="edit" id="${personInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${personInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${personInstanceTotal}" />
    </div>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
