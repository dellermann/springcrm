<nav>
  <ul id="main-menu">
    <li>
      <g:link uri="/"><g:message code="menu.home" default="Home" /></g:link>
    </li>
    <li>
      <span><g:message code="menu.purchasing" default="Purchasing" /></span>
      <div>
        <ul>
          <g:ifModuleAllowed modules="contact">
          <li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li>
          <li><g:link controller="organization" params="[type: 2]"><g:message code="organization.vendors" default="Vendors" /></g:link></li>
          <li><g:link controller="person"><g:message code="person.plural" /></g:link></li>
          </g:ifModuleAllowed>
          <g:ifModuleAllowed modules="purchaseInvoice">
          <li><hr /></li>
          <li><g:link controller="purchaseInvoice"><g:message code="purchaseInvoice.plural" /></g:link></li>
          </g:ifModuleAllowed>
        </ul>
      </div>
    </li>
    <g:ifModuleAllowed modules="contact">
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
    </g:ifModuleAllowed>
    <li>
      <span><g:message code="menu.invoice" default="Invoice" /></span>
      <div>
        <ul>
          <g:ifModuleAllowed modules="quote"><li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="salesOrder"><li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="invoice"><li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="dunning"><li><g:link controller="dunning"><g:message code="dunning.plural" /></g:link></li></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="creditMemo"><li><g:link controller="creditMemo"><g:message code="creditMemo.plural" /></g:link></li></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="purchaseInvoice"><li><hr /></li>
          <li><g:link controller="purchaseInvoice"><g:message code="purchaseInvoice.plural" /></g:link></li></g:ifModuleAllowed>
        </ul>
      </div>
    </li>
    <li>
      <a href="#"><g:message code="menu.stock" default="Stock" /></a>
      <div>
        <ul>
          <g:ifModuleAllowed modules="product"><li><g:link controller="product"><g:message code="product.plural" /></g:link></li></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="service"><li><g:link controller="service"><g:message code="service.plural" /></g:link></li></g:ifModuleAllowed>
        </ul>
      </div>
    </li>
    <li>
      <a href="#"><g:message code="menu.office" default="Office" /></a>
      <div>
        <ul>
          <g:ifModuleAllowed modules="call"><li><g:link controller="call"><g:message code="call.plural" /></g:link></li></g:ifModuleAllowed>
          <li><hr /></li>
          <g:ifModuleAllowed modules="document"><li><g:link controller="document"><g:message code="document.plural" /></g:link></li></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="note"><li><g:link controller="note"><g:message code="note.plural" /></g:link></li></g:ifModuleAllowed>
          <li><hr /></li>
          <g:ifModuleAllowed modules="project"><li><g:link controller="project"><g:message code="project.plural" /></g:link></li></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="calendar"><li><g:link controller="calendarEvent"><g:message code="calendarEvent.menu.label" /></g:link></li></g:ifModuleAllowed>
        </ul>
      </div>
    </li>
    <li>
      <a href="#"><g:message code="menu.reports" default="Reports" /></a>
      <div>
        <ul>
          <li><g:link controller="report" action="salesJournal"><g:message code="report.salesJournal.title" default="Sales journal" /></g:link></li>
        </ul>
      </div>
    </li>
    <li>
      <a href="#"><g:message code="menu.settings" default="Settings" /></a>
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
        <g:ifModuleAllowed modules="contact"><option value="${createLink(controller: 'organization', action: 'create')}"><g:message code="default.quickMenu.organization" /></option></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><option value="${createLink(controller: 'person', action: 'create')}"><g:message code="default.quickMenu.person" /></option></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="call"><option value="${createLink(controller: 'call', action: 'create')}"><g:message code="default.quickMenu.call" /></option></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="quote"><option value="${createLink(controller: 'quote', action: 'create')}"><g:message code="default.quickMenu.quote" /></option></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="salesOrder"><option value="${createLink(controller: 'salesOrder', action: 'create')}"><g:message code="default.quickMenu.salesOrder" /></option></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="invoice"><option value="${createLink(controller: 'invoice', action: 'create')}"><g:message code="default.quickMenu.invoice" /></option></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="purchaseInvoice"><option value="${createLink(controller: 'purchaseInvoice', action: 'create')}"><g:message code="default.quickMenu.purchaseInvoice" /></option></g:ifModuleAllowed>
        <!--<option>Neue Aufgabe</option>
        <option>Neues Dokument</option>-->
      </select>
    </li>
  </ul>
</nav>
