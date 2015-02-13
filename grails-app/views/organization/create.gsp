<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="backLinkUrl"
      content="${createLink(action: 'index', params: [listType: params.listType])}" />
  </head>

  <body>
    <g:applyLayout name="create" model="[
        type: 'organization', instance: organizationInstance,
        listParams: [listType: params.recType]
      ]" />

    <content tag="scripts">
      <asset:javascript src="organization-form" />
    </content>
  </body>
</html>
