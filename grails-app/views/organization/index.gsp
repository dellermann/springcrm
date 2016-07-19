<%@ page import="org.amcworld.springcrm.Organization" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:if test="${(params.listType ?: 0) as int & 1}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.customers')}" />
    </g:if>
    <g:elseif test="${(params.listType ?: 0) as int & 2}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.vendors')}" />
    </g:elseif>
    <g:else>
    <g:set var="entitiesName"
      value="${message(code: 'organization.plural')}" />
    </g:else>
    <meta name="caption" content="${entitiesName}" />
    <title>${entitiesName}</title>
  </head>

  <body>
    <g:applyLayout name="list" model="[
        createParams: [
            recType: params.listType ?: 0, listType: params.listType
        ],
        list: organizationInstanceList,
        type: 'organization'
      ]">
      <div class="visible-xs">
        <g:letterBar params="[listType: params.listType]"
          clazz="${Organization}" property="name"
          where='${params.listType ? "o.recType in (${params.listType}, 3)" : ""}'
          numLetters="5" separator="-" />
      </div>
      <div class="visible-sm">
        <g:letterBar params="[listType: params.listType]"
          clazz="${Organization}" property="name"
          where='${params.listType ? "o.recType in (${params.listType}, 3)" : ""}'
          numLetters="3" />
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar params="[listType: params.listType]"
          clazz="${Organization}" property="name"
          where='${params.listType ? "o.recType in (${params.listType}, 3)" : ""}' />
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number" title="${message(code: 'organization.number.label')}" />
              <g:sortableColumn property="name" title="${message(code: 'organization.name.label')}" />
              <th><g:message code="organization.billingAddr.label" /></th>
              <g:sortableColumn property="phone" title="${message(code: 'organization.phone.label')}" />
              <g:sortableColumn property="email1" title="${message(code: 'organization.email1.label')}" />
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${organizationInstanceList}" status="i"
              var="organizationInstance">
            <tr>
              <td class="col-type-id organization-number"><g:link action="show" id="${organizationInstance.id}" params="[listType: params.listType]"><g:fieldValue bean="${organizationInstance}" field="fullNumber" /></g:link></td>
              <td class="col-type-string organization-name"><g:link action="show" id="${organizationInstance.id}" params="[listType: params.listType]"><g:fieldValue bean="${organizationInstance}" field="name" /></g:link></td>
              <td class="col-type-string organization-billing-addr"><g:fieldValue bean="${organizationInstance}" field="billingAddr" /></td>
              <td class="col-type-string organization-phone"><a href="tel:${organizationInstance.phone}"><g:fieldValue bean="${organizationInstance}" field="phone" /></a></td>
              <td class="col-type-string organization-email1"><a href="mailto:${fieldValue(bean: organizationInstance, field: 'email1')}"><g:fieldValue bean="${organizationInstance}" field="email1" /></a></td>
              <td class="col-actions">
                <g:button action="edit" id="${organizationInstance.id}"
                  params="[listType: params.listType]" color="success" size="xs"
                  icon="pencil-square-o"
                  message="default.button.edit.label" />
              </td>
            </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <nav class="text-center">
        <div class="visible-xs">
          <g:paginate total="${organizationInstanceTotal}"
            params="[listType: params.listType]" maxsteps="3"
            class="pagination-sm" />
        </div>
        <div class="hidden-xs">
          <g:paginate total="${organizationInstanceTotal}"
            params="[listType: params.listType]" />
        </div>
      </nav>
    </g:applyLayout>
  </body>
</html>
