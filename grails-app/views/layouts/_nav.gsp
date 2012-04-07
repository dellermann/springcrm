<nav>
  <ul id="main-menu">
    <li>
      <g:link uri="/"><g:message code="menu.home" default="Home" /></g:link>
      <ul>
        <li><g:link uri="/"><g:message code="menu.sub.homepage" default="Home page" /></g:link></li>
        <g:ifModuleAllowed modules="calendar"><li><g:link controller="calendarEvent"><g:message code="calendarEvent.menu.label" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="call"><li><g:link controller="call"><g:message code="call.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="note"><li><g:link controller="note"><g:message code="note.plural" /></g:link></li></g:ifModuleAllowed>
        <!--<li><a href="#">Postfach</a></li>-->
      </ul>
    </li>
    <li>
      <a href="#"><g:message code="menu.marketing" default="Marketing" /></a>
      <ul>
        <!--<li><a href="#">Kampagnen</a></li>-->
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization" params="[type:1]"><g:message code="organization.customers" default="Customers" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="person"><g:message code="person.plural" /></g:link></li></g:ifModuleAllowed>
        <!--<li><a href="#">Leads</a></li>
        <li><a href="#">Kalender</a></li>
        <li><a href="#">Postfach</a></li>-->
      </ul>
    </li>
    <li>
      <a href="#"><g:message code="menu.sales" default="Sales" /></a>
      <ul>
        <!--<li><a href="#">Leads</a></li>-->
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization" params="[type:1]"><g:message code="organization.customers" default="Customers" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="person"><g:message code="person.plural" /></g:link></li></g:ifModuleAllowed>
        <li><hr /></li>
        <!--<li><a href="#">Verkaufspotentiale</a></li>-->
        <g:ifModuleAllowed modules="contact"><li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li></g:ifModuleAllowed>
      </ul>
    </li>
    <li>
      <a href="#"><g:message code="menu.support" default="Support" /></a>
      <ul>
        <!--<li><a href="#">Trouble Tickets</a></li>
        <li><a href="#">Wissensbasis</a></li>-->
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization" params="[type:1]"><g:message code="organization.customers" default="Customers" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="person"><g:message code="person.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="note"><li><g:link controller="note"><g:message code="note.plural" /></g:link></li></g:ifModuleAllowed>
        <!--<li><a href="#">Postfach</a></li>-->
      </ul>
    </li>
    <li>
      <a href="#"><g:message code="menu.assets" default="Assets" /></a>
      <ul>
        <g:ifModuleAllowed modules="product"><li><g:link controller="product"><g:message code="product.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="service"><li><g:link controller="service"><g:message code="service.plural" /></g:link></li></g:ifModuleAllowed>
        <li><hr /></li>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization" params="[type:1]"><g:message code="organization.customers" default="Customers" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization" params="[type:2]"><g:message code="organization.vendors" default="Vendors" /></g:link></li></g:ifModuleAllowed>
        <li><hr /></li>
        <!--<li><a href="#">Preislisten</a></li>-->
        <g:ifModuleAllowed modules="purchaseInvoice"><li><g:link controller="purchaseInvoice"><g:message code="purchaseInvoice.plural" /></g:link></li>
        <li><hr /></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="quote"><li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="salesOrder"><li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="invoice"><li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="dunning"><li><g:link controller="dunning"><g:message code="dunning.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="creditMemo"><li><g:link controller="creditMemo"><g:message code="creditMemo.plural" /></g:link></li></g:ifModuleAllowed>
      </ul>
    </li>
    <li>
      <a href="#"><g:message code="menu.reports" default="Reports" /></a>
      <ul>
        <li><g:link controller="report" action="salesJournal"><g:message code="report.salesJournal.title" default="Sales journal" /></g:link></li>
      </ul>
    </li>
    <li>
      <a href="#"><g:message code="menu.settings" default="Settings" /></a>
      <ul>
        <g:ifAdmin>
        <li><g:link controller="user"><g:message code="user.plural" /></g:link></li>
        <li><g:link controller="config"><g:message code="config.title" /></g:link></li>
        </g:ifAdmin>
        <li><g:link controller="user" action="settingsIndex"><g:message code="user.settings.title" /></g:link></li>
      </ul>
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
        <!--<option>Neue Aufgabe</option>
        <option>Neues Dokument</option>-->
      </select>
    </li>
  </ul>
</nav>