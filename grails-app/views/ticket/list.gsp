<%@ page import="org.amcworld.springcrm.Ticket" %>
<html>
<head>
  <meta name="layout" content="main" />
  <r:require modules="ticket" />
  <g:set var="entityName" value="${message(code: 'ticket.label', default: 'Ticket')}" />
  <g:set var="entitiesName" value="${message(code: 'ticket.plural', default: 'Tickets')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="create" color="green" icon="plus"
          message="default.new.label" args="[entityName]" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${ticketInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="ticket-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'ticket.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="helpdesk.name" title="${message(code: 'ticket.helpdesk.label', default: 'Helpdesk')}" />
          <g:sortableColumn scope="col" property="subject" title="${message(code: 'ticket.subject.label', default: 'Subject')}" />
          <g:sortableColumn scope="col" property="stage" title="${message(code: 'ticket.stage.label', default: 'Stage')}" />
          <g:sortableColumn scope="col" property="customerName" title="${message(code: 'ticket.customerName.label', default: 'Customer name')}" />
          <g:sortableColumn scope="col" property="dateCreated" title="${message(code: 'ticket.dateCreated.label', default: 'Created')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${ticketInstanceList}" status="i" var="ticketInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="ticket-row-selector-${ticketInstance.id}" data-id="${ticketInstance.id}" /></td>
          <td class="string ticket-number"><g:link action="show" id="${ticketInstance.id}"><g:fieldValue bean="${ticketInstance}" field="fullNumber" /></g:link></td>
          <td class="ref ticket-helpdesk"><g:fieldValue bean="${ticketInstance}" field="helpdesk.name" /></td>
          <td class="string ticket-subject"><g:fieldValue bean="${ticketInstance}" field="subject" /></td>
          <td class="status ticket-stage ticket-stage-${ticketInstance.stage}"><g:message code="ticket.stage.${ticketInstance.stage}" default="${ticketInstance.stage.toString()}" /></td>
          <td class="string ticket-customer-name"><g:fieldValue bean="${ticketInstance}" field="customerName" /></td>
          <td class="date ticket-date-created"><g:formatDate date="${ticketInstance.dateCreated}" formatName="default.format.datetime" /></td>
          <td class="action-buttons">
            <g:button action="edit" id="${ticketInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${ticketInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${ticketInstanceTotal}" />
    </div>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
