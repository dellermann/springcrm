<html>
  <head>
    <meta name="stylesheet" content="person-form"/>
  </head>

  <body>
    <g:applyLayout name="create" model="[
        type: 'person', instance: person, enctype: 'multipart/form-data'
      ]"/>

    <content tag="scripts">
      <asset:javascript src="person-form"/>
    </content>
  </body>
</html>
