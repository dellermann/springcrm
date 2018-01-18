<html>
  <head>
    <meta name="backLinkUrl"
      content="${createLink(
        action: 'index', params: [listType: params.listType]
      )}" />
  </head>

  <body>
    <g:applyLayout name="create" model="[
        type: 'organization', instance: organization,
        listParams: [listType: params.listType]
      ]"/>

    <content tag="scripts">
      <asset:javascript src="organization-form"/>
    </content>
  </body>
</html>
