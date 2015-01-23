<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName"
      value="${message(code: 'organization.label')}" />
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
    <title>${organizationInstance} -
    <g:message code="default.edit.label" args="[entityName]" /></title>
  </head>

  <body>
    <g:applyLayout name="edit" model="[
        type: 'organization', instance: organizationInstance,
        listParams: [type: params.listType]
      ]" />

    <content tag="scripts">
      <asset:javascript src="organization-form" />
    </content>
  </body>
</html>
