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
          <th id="content-table-headers-person-row-selector"><input type="checkbox" id="person-row-selector" /></th>
          <g:sortableColumn id="content-table-headers-person-number" property="number" title="${message(code: 'person.number.label', default: 'Number')}" />
          <g:sortableColumn id="content-table-headers-person-last-name" property="lastName" title="${message(code: 'person.lastName.label', default: 'Last name')}" />
          <g:sortableColumn id="content-table-headers-person-first-name" property="firstName" title="${message(code: 'person.firstName.label', default: 'First name')}" />
          <g:sortableColumn id="content-table-headers-person-organization" property="organization.name" title="${message(code: 'person.organization.label', default: 'Organization')}" />
          <g:sortableColumn id="content-table-headers-person-phone" property="phone" title="${message(code: 'person.phone.label', default: 'Phone')}" />
          <g:sortableColumn id="content-table-headers-person-email1" property="email1" title="${message(code: 'person.email1.label', default: 'E-mail')}" />
          <th id="content-table-headers-person-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${personInstanceList}" status="i" var="personInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-person-row-selector"><input type="checkbox" id="person-row-selector-${personInstance.id}" data-id="${personInstance.id}" /></td>
          <td class="content-table-type-id content-table-column-person-number" headers="content-table-headers-person-number"><g:link action="show" id="${personInstance.id}">${fieldValue(bean: personInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-string content-table-column-person-last-name" headers="content-table-headers-person-last-name"><g:link action="show" id="${personInstance.id}">${fieldValue(bean: personInstance, field: "lastName")}</g:link></td>
          <td class="content-table-type-string content-table-column-person-first-name" headers="content-table-headers-person-first-name">${fieldValue(bean: personInstance, field: "firstName")}</td>
          <td class="content-table-type-ref content-table-column-person-organization" headers="content-table-headers-person-organization"><g:link controller="organization" action="show" id="${personInstance.organization.id}">${fieldValue(bean: personInstance, field: "organization.name")}</g:link></td>
          <td class="content-table-type-string content-table-column-person-phone" headers="content-table-headers-person-phone"><a href="tel:${fieldValue(bean: personInstance, field: "phone")}">${fieldValue(bean: personInstance, field: "phone")}</a></td>
          <td class="content-table-type-string content-table-column-person-email1" headers="content-table-headers-person-email1"><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}">${fieldValue(bean: personInstance, field: "email1")}</a></td>
          <td class="content-table-buttons" headers="content-table-headers-person-buttons">
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
