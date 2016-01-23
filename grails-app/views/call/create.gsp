<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="call-form" />
  </head>

  <body>
    <g:applyLayout name="create"
      model="[type: 'call', instance: callInstance]">
      <g:hiddenField name="project" value="${project}" />
      <g:hiddenField name="projectPhase" value="${projectPhase}" />
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="call-form" />
    </content>
  </body>
</html>

