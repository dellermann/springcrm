<%@ page import="org.amcworld.springcrm.Ticket" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'ticket.label', default: 'Ticket')}" />
  <g:set var="entitiesName" value="${message(code: 'ticket.plural', default: 'Tickets')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <div class="empty-list">
      <p><g:message code="ticket.helpdesk.emptyList" /></p>
      <g:if test="${session.user.checkAllowedControllers(['helpdesk'])}">
      <div class="buttons">
        <g:link controller="helpdesk" action="create" class="green"><g:message code="default.new.label" args="[message(code: 'helpdesk.label')]" /></g:link>
      </div>
      </g:if>
      <g:else>
      <p><g:message code="ticket.helpdesk.notAllowed" /></p>
      </g:else>
    </div>
  </section>
</body>
</html>
