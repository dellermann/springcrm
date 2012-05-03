
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
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${organizationInstanceList}">
    <g:letterBar clazz="${Organization}" property="name" where='${params.type ? "o.recType in (${params.type}, 3)" : ""}' />
    <table class="content-table">
      <thead>
        <tr>
          <th id="content-table-headers-organization-row-selector"><input type="checkbox" id="organization-row-selector" /></th>
          <g:sortableColumn id="content-table-headers-organization-number" property="number" title="${message(code: 'organization.number.label', default: 'Number')}" />
          <g:sortableColumn id="content-table-headers-organization-name" property="name" title="${message(code: 'organization.name.label', default: 'Name')}" />
          <th id="content-table-headers-organization-billing-addr"><g:message code="organization.billingAddr.label" default="Billing address" /></th>
          <g:sortableColumn id="content-table-headers-organization-phone" property="phone" title="${message(code: 'organization.phone.label', default: 'Phone')}" />
          <g:sortableColumn id="content-table-headers-organization-email1" property="email1" title="${message(code: 'organization.email1.label', default: 'E-mail')}" />
          <g:sortableColumn id="content-table-headers-organization-website" property="website" title="${message(code: 'organization.website.label', default: 'Website')}" />
          <th id="content-table-headers-organization-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${organizationInstanceList}" status="i" var="organizationInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-organization-row-selector"><input type="checkbox" id="organization-row-selector-${organizationInstance.id}" data-id="${organizationInstance.id}" /></td>
          <td class="content-table-type-id content-table-column-organization-number" headers="content-table-headers-organization-number"><g:link action="show" id="${organizationInstance.id}" params="[type: params.type]">${fieldValue(bean: organizationInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-string content-table-column-organization-name" headers="content-table-headers-organization-name"><g:link action="show" id="${organizationInstance.id}" params="[type: params.type]">${fieldValue(bean: organizationInstance, field: "name")}</g:link></td>
          <td class="content-table-type-string content-table-column-organization-billing-addr" headers="content-table-headers-organization-billing-addr">${fieldValue(bean: organizationInstance, field: "billingAddr")}</td>
          <td class="content-table-type-string content-table-column-organization-phone" headers="content-table-headers-organization-phone"><a href="tel:${fieldValue(bean: organizationInstance, field: "phone")}">${fieldValue(bean: organizationInstance, field: "phone")}</a></td>
          <td class="content-table-type-string content-table-column-organization-email1" headers="content-table-headers-organization-email1"><a href="mailto:${fieldValue(bean: organizationInstance, field: 'email1')}">${fieldValue(bean: organizationInstance, field: "email1")}</a></td>
          <td class="content-table-type-string content-table-column-organization-website" headers="content-table-headers-organization-website"><a href="${organizationInstance?.website}" target="_blank">${fieldValue(bean: organizationInstance, field: "website")?.replace('http://', '')}</a></td>
          <td class="content-table-buttons" headers="content-table-headers-organization-buttons">
            <g:link action="edit" id="${organizationInstance.id}" params="[listType: params.type]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${organizationInstance?.id}" params="[type: params.type]" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${organizationInstanceTotal}" params="[type: params.type]" />
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
