<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="person-form" />
  </head>

  <body>
    <g:applyLayout name="edit" model="[
        type: 'person', instance: personInstance,
        enctype: 'multipart/form-data'
      ]" />

    <content tag="scripts">
      <asset:javascript src="person-form" />
    </content>
  </body>
</html>
