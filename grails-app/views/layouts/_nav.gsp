<nav>
  <ul id="main-menu">
    <li>
      <g:link uri="/"><i class="fa fa-home"></i> <g:message code="menu.home" default="Home" /></g:link>
    </li>
    <g:ifControllerAllowed controllers="organization person purchaseInvoice">
    <li>
      <span><g:message code="menu.purchasing" default="Purchasing" /></span>
      <div>
        <ul>
          <g:ifControllerAllowed controllers="organization">
          <li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li>
          <li><g:link controller="organization" params="[type: 2]"><g:message code="organization.vendors" default="Vendors" /></g:link></li>
          </g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="person"><li><g:link controller="person"><g:message code="person.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="purchaseInvoice">
          <li><hr /></li>
          <li><g:link controller="purchaseInvoice"><g:message code="purchaseInvoice.plural" /></g:link></li>
          </g:ifControllerAllowed>
        </ul>
      </div>
    </li>
    </g:ifControllerAllowed>
    <g:ifControllerAllowed controllers="organization person">
    <li>
      <span><g:message code="menu.sales" default="Sales" /></span>
      <div>
        <ul>
          <li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li>
          <li><g:link controller="organization" params="[type: 1]"><g:message code="organization.customers" default="Customers" /></g:link></li>
          <li><g:link controller="person"><g:message code="person.plural" /></g:link></li>
        </ul>
      </div>
    </li>
    </g:ifControllerAllowed>
    <g:ifControllerAllowed controllers="quote salesOrder invoice dunning creditMemo purchaseInvoice">
    <li>
      <span><g:message code="menu.invoice" default="Invoice" /></span>
      <div>
        <ul>
          <g:ifControllerAllowed controllers="quote"><li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="salesOrder"><li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="invoice"><li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="dunning"><li><g:link controller="dunning"><g:message code="dunning.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="creditMemo"><li><g:link controller="creditMemo"><g:message code="creditMemo.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="purchaseInvoice">
            <li><hr /></li>
            <li><g:link controller="purchaseInvoice"><g:message code="purchaseInvoice.plural" /></g:link></li>
          </g:ifControllerAllowed>
        </ul>
      </div>
    </li>
    </g:ifControllerAllowed>
    <g:ifControllerAllowed controllers="product service">
    <li>
      <span><g:message code="menu.stock" default="Stock" /></span>
      <div>
        <ul>
          <g:ifControllerAllowed controllers="product"><li><g:link controller="product"><g:message code="product.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="service"><li><g:link controller="service"><g:message code="service.plural" /></g:link></li></g:ifControllerAllowed>
        </ul>
      </div>
    </li>
    </g:ifControllerAllowed>
    <g:ifControllerAllowed controllers="call document note project calendar">
    <li>
      <span><g:message code="menu.office" default="Office" /></span>
      <div>
        <ul>
          <g:ifControllerAllowed controllers="call"><li><g:link controller="call"><g:message code="call.plural" /></g:link></li></g:ifControllerAllowed>
          <li><hr /></li>
          <g:ifControllerAllowed controllers="document"><li><g:link controller="document"><g:message code="document.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="note"><li><g:link controller="note"><g:message code="note.plural" /></g:link></li></g:ifControllerAllowed>
          <li><hr /></li>
          <g:ifControllerAllowed controllers="project"><li><g:link controller="project"><g:message code="project.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="calendar"><li><g:link controller="calendarEvent"><g:message code="calendarEvent.menu.label" /></g:link></li></g:ifControllerAllowed>
        </ul>
      </div>
    </li>
    </g:ifControllerAllowed>
    <g:ifControllerAllowed controllers="helpdesk ticket">
    <li>
      <span><g:message code="menu.support" default="Support" /></span>
      <div>
        <ul>
          <g:ifControllerAllowed controllers="helpdesk"><li><g:link controller="helpdesk"><g:message code="helpdesk.plural" /></g:link></li></g:ifControllerAllowed>
          <g:ifControllerAllowed controllers="ticket"><li><g:link controller="ticket"><g:message code="ticket.plural" /></g:link></li></g:ifControllerAllowed>
        </ul>
      </div>
    </li>
    </g:ifControllerAllowed>
    <g:ifControllerAllowed controllers="organization invoice dunning creditMemo">
    <li>
      <span><g:message code="menu.reports" default="Reports" /></span>
      <div>
        <ul>
          <li><g:link controller="report" action="salesJournal"><g:message code="report.salesJournal.title" default="Sales journal" /></g:link></li>
        </ul>
      </div>
    </li>
    </g:ifControllerAllowed>
    <li>
      <span><g:message code="menu.settings" default="Settings" /></span>
      <div>
        <ul>
          <g:ifAdmin><li><g:link controller="user"><g:message code="user.plural" /></g:link></li></g:ifAdmin>
          <li><g:link controller="user" action="settingsIndex"><g:message code="user.settings.title" /></g:link></li>
          <g:ifAdmin><li><g:link controller="config"><g:message code="config.title" /></g:link></li></g:ifAdmin>
        </ul>
      </div>
    </li>
    <li>
      <select id="quick-access">
        <option value=""><g:message code="default.quickMenu" /></option>
        <g:ifControllerAllowed controllers="organization"><option value="${createLink(controller: 'organization', action: 'create')}"><g:message code="default.quickMenu.organization" /></option></g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="person"><option value="${createLink(controller: 'person', action: 'create')}"><g:message code="default.quickMenu.person" /></option></g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="call"><option value="${createLink(controller: 'call', action: 'create')}"><g:message code="default.quickMenu.call" /></option></g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="quote"><option value="${createLink(controller: 'quote', action: 'create')}"><g:message code="default.quickMenu.quote" /></option></g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="salesOrder"><option value="${createLink(controller: 'salesOrder', action: 'create')}"><g:message code="default.quickMenu.salesOrder" /></option></g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="invoice"><option value="${createLink(controller: 'invoice', action: 'create')}"><g:message code="default.quickMenu.invoice" /></option></g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="purchaseInvoice"><option value="${createLink(controller: 'purchaseInvoice', action: 'create')}"><g:message code="default.quickMenu.purchaseInvoice" /></option></g:ifControllerAllowed>
      </select>
    </li>
  </ul>
</nav>
