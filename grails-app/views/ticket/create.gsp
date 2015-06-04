<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="create" model="[
        type: 'ticket', instance: ticketInstance,
        enctype: 'multipart/form-data'
      ]" />

    <content tag="scripts">
      <asset:javascript src="ticket-form" />
    </content>
  </body>
</html>
