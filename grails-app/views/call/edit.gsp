<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="call-form" />
  </head>

  <body>
    <g:applyLayout name="edit"
      model="[type: 'call', instance: callInstance]" />

    <content tag="scripts">
      <asset:javascript src="call-form" />
    </content>
  </body>
</html>
