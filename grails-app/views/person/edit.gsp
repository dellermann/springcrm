<html>
  <head>
    <meta name="stylesheet" content="person-form"/>
  </head>

  <body>
    <g:applyLayout name="edit" model="[
        type: 'person', instance: person, enctype: 'multipart/form-data',
        method: 'post'
      ]"/>

    <content tag="scripts">
      <asset:javascript src="modules"/>
      <asset:javascript src="lang/bootstrap-fileinput/${lang}.js"/>
      <asset:javascript src="person-form"/>
    </content>
  </body>
</html>
