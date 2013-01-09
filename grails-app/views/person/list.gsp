<%@ page import="org.amcworld.springcrm.Person" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
  <g:set var="entitiesName" value="${message(code: 'person.plural', default: 'Persons')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li class="menu">
          <span class="button menu-button white"><span><g:message code="default.export" default="Export" /></span></span>
          <ul>
            <li class="with-link"><g:link action="gdatasync"><g:message code="person.action.gdataExport.label" /></g:link></li>
            <li class="with-link"><g:link action="ldapexport"><g:message code="person.action.ldapExport.label" /></g:link></li>
          </ul>
        </li>
        <li><g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
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
            <g:link action="edit" id="${personInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${personInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
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
          <g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link>
        </div>
      </div>
    </g:else>
  </section>
</body>
</html>
