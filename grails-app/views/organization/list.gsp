
<%@ page import="org.amcworld.springcrm.Organization" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'organization.label', default: 'Organization')}" />
  <g:if test="${(params.type ?: 0) as int & 1}">
  <g:set var="entitiesName" value="${message(code: 'organization.customers', default: 'Customers')}" />
  </g:if>
  <g:elseif test="${(params.type ?: 0) as int & 2}">
  <g:set var="entitiesName" value="${message(code: 'organization.vendors', default: 'Vendors')}" />
  </g:elseif>
  <g:else>
  <g:set var="entitiesName" value="${message(code: 'organization.plural', default: 'Organizations')}" />
  </g:else>
  <title>${entitiesName}</title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="create" class="green" params="[recType:params.type ?: 0]"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:if test="${organizationInstanceList}">
    <g:letterBar clazz="${Organization}" property="name" where='${params.type ? "o.recType in (${params.type}, 3)" : ""}'/>
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="organization-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'organization.number.label', default: 'Number')}" />
          <g:sortableColumn property="name" title="${message(code: 'organization.name.label', default: 'Name')}" />
          <th><g:message code="organization.billingAddr.label" default="Billing address" /></th>
          <g:sortableColumn property="phone" title="${message(code: 'organization.phone.label', default: 'Phone')}" />
          <g:sortableColumn property="email1" title="${message(code: 'organization.email1.label', default: 'E-mail')}" />
          <g:sortableColumn property="website" title="${message(code: 'organization.website.label', default: 'Website')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${organizationInstanceList}" status="i" var="organizationInstance">
        <tr>
          <td><input type="checkbox" id="organization-multop-${organizationInstance.id}" class="multop-sel-item" /></td>
          <td style="text-align: center;"><g:link action="show" id="${organizationInstance.id}" params="[type:params.type]">${fieldValue(bean: organizationInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${organizationInstance.id}" params="[type:params.type]">${fieldValue(bean: organizationInstance, field: "name")}</g:link></td>
          <td>${fieldValue(bean: organizationInstance, field: "billingAddr")}</td>
          <td>${fieldValue(bean: organizationInstance, field: "phone")}</td>
          <td><a href="mailto:${fieldValue(bean: organizationInstance, field: 'email1')}">${fieldValue(bean: organizationInstance, field: "email1")}</a></td>
          <td><a href="${organizationInstance?.website}" target="_blank">${fieldValue(bean: organizationInstance, field: "website")}</a></td>
          <td>
            <g:link action="edit" id="${organizationInstance.id}" params="[listType:params.type]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${organizationInstance?.id}" params="[type:params.type]" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${organizationInstanceTotal}" params="[type:params.type]" />
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
