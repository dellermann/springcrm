<%@ page import="org.amcworld.springcrm.TicketStage" %>

<html>
  <head>
    <meta name="layout" content="frontend" />
    <title><g:message code="default.overview" /></title>
    <meta name="stylesheet" content="helpdesk-frontend" />
    <g:set var="entityName" value="${message(code: 'ticket.label')}" />
  </head>

  <body>
    <content tag="toolbar">
      <g:button controller="ticket" action="frontendCreate"
        params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
        color="success" size="sm" icon="plus-circle"
        message="default.new.label" args="[entityName]" />
    </content>

    <g:form controller="ticket" action="frontendSearchTicket" method="get"
      class="form-inline">
      <g:hiddenField name="helpdesk" value="${helpdeskInstance.id}"/>
      <g:hiddenField name="accessCode" value="${helpdeskInstance.accessCode}"/>
      <label for="code" class="sr-only">
        <g:message code="helpdesk.code.label"/>
      </label>
      <input type="text" id="code" name="code" class="form-control"
        placeholder="${message(code: 'helpdesk.code.label')}" size="64"
        required="required" minlength="10" maxlength="64"/>
      <button class="btn btn-primary">
        <i class="fa fa-search"></i>
        <g:message code="helpdesk.button.search"/>
      </button>
    </g:form>

    <content tag="scripts">
      <asset:javascript src="helpdesk-frontend" />
    </content>
  </body>
</html>
