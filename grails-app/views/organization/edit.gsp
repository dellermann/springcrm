<html>
  <head>
    <g:if test="${(params.listType ?: 0) as int & 1}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.customers')}"/>
    </g:if>
    <g:elseif test="${(params.listType ?: 0) as int & 2}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.vendors')}"/>
    </g:elseif>
    <g:else>
    <g:set var="entitiesName"
      value="${message(code: 'organization.plural')}"/>
    </g:else>
    <meta name="caption" content="${entitiesName}"/>
    <meta name="backLinkUrl"
      content="${createLink(
        action: 'index', params: [listType: params.listType]
      )}"/>
  </head>

  <body>
    <g:applyLayout name="edit" model="[
        type: 'organization', instance: organization,
        listParams: [listType: params.listType]
      ]"/>

    <content tag="scripts">
      <asset:javascript src="organization-form"/>
    </content>
  </body>
</html>
