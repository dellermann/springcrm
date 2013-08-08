<%@ page import="org.amcworld.springcrm.Organization" %>
<html>
<head>
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
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="create" params="[recType:params.type ?: 0]"
          color="green" icon="plus" message="default.new.label"
          args="[entityName]" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${organizationInstanceList}">
    <g:letterBar clazz="${Organization}" property="name" where='${params.type ? "o.recType in (${params.type}, 3)" : ""}' />
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="organization-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'organization.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="name" title="${message(code: 'organization.name.label', default: 'Name')}" />
          <th scope="col"><g:message code="organization.billingAddr.label" default="Billing address" /></th>
          <g:sortableColumn scope="col" property="phone" title="${message(code: 'organization.phone.label', default: 'Phone')}" />
          <g:sortableColumn scope="col" property="email1" title="${message(code: 'organization.email1.label', default: 'E-mail')}" />
          <g:sortableColumn scope="col" property="website" title="${message(code: 'organization.website.label', default: 'Website')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${organizationInstanceList}" status="i" var="organizationInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="organization-row-selector-${organizationInstance.id}" data-id="${organizationInstance.id}" /></td>
          <td class="id organization-number"><g:link action="show" id="${organizationInstance.id}" params="[type: params.type]"><g:fieldValue bean="${organizationInstance}" field="fullNumber" /></g:link></td>
          <td class="string organization-name"><g:link action="show" id="${organizationInstance.id}" params="[type: params.type]"><g:fieldValue bean="${organizationInstance}" field="name" /></g:link></td>
          <td class="string organization-billing-addr"><g:fieldValue bean="${organizationInstance}" field="billingAddr" /></td>
          <td class="string organization-phone"><a href="tel:<g:fieldValue bean="${organizationInstance}" field="phone" />"><g:fieldValue bean="${organizationInstance}" field="phone" /></a></td>
          <td class="string organization-email1"><a href="mailto:${fieldValue(bean: organizationInstance, field: 'email1')}"><g:fieldValue bean="${organizationInstance}" field="email1" /></a></td>
          <td class="string organization-website"><a href="${organizationInstance?.website}" target="_blank">${fieldValue(bean: organizationInstance, field: "website")?.replace('http://', '')}</a></td>
          <td class="action-buttons">
            <g:button action="edit" id="${organizationInstance.id}"
              params="[listType: params.type]" color="green" size="small"
              message="default.button.edit.label" />
            <g:button action="delete" id="${organizationInstance?.id}"
              params="[listType: params.type]" color="red" size="small"
              class="delete-btn" message="default.button.delete.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
