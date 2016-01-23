<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <div class="well well-lg empty-list">
      <p><g:message code="ticket.helpdesk.emptyList" /></p>
      <g:if test="${session.credential.checkAllowedControllers(['helpdesk'] as Set)}">
      <div class="buttons">
        <g:link controller="helpdesk" action="create" class="green">
          <i class="fa fa-plus-circle"></i>
          <g:message code="default.new.label"
            args="[message(code: 'helpdesk.label')]" />
        </g:link>
      </div>
      </g:if>
      <g:else>
      <p><g:message code="ticket.helpdesk.notAllowed" /></p>
      </g:else>
    </div>
  </body>
</html>
