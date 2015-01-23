<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'person.label')}" />
    <g:set var="entitiesName" value="${message(code: 'person.plural')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
    <meta name="stylesheet" content="person-form" />
  </head>

  <body>
    <g:applyLayout name="create" model="[
        type: 'person', instance: personInstance,
        enctype: 'multipart/form-data'
      ]" />

    <content tag="scripts">
      <asset:javascript src="person-form" />
    </content>
  </body>
</html>
