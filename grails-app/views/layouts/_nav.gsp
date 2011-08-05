<nav>
  <ul id="main-menu">
    <li>
      <a href="#">Home</a>
      <ul>
        <li><a href="${createLink(uri: '/')}">Startseite</a></li>
        <!--<li><a href="#">Kalender</a></li>-->
        <li><g:link controller="call"><g:message code="call.plural" /></g:link></li>
        <!--<li><a href="#">Postfach</a></li>-->
      </ul>
    </li>
    <li>
      <a href="#">Marketing</a>
      <ul>
        <!--<li><a href="#">Kampagnen</a></li>-->
        <li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li>
        <li><g:link controller="person"><g:message code="person.plural" /></g:link></li>
        <!--<li><a href="#">Leads</a></li>
        <li><a href="#">Kalender</a></li>
        <li><a href="#">Postfach</a></li>
        <li><a href="#">Dokumente</a></li>-->
      </ul>
    </li>
    <li>
      <a href="#">Vertrieb</a>
      <ul>
        <!--<li><a href="#">Leads</a></li>-->
        <li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li>
        <li><g:link controller="person"><g:message code="person.plural" /></g:link></li>
        <!--<li><a href="#">Verkaufspotentiale</a></li>-->
        <li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li>
        <li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li>
        <li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li>
      </ul>
    </li>
    <li>
      <a href="#">Support</a>
      <ul>
        <!--<li><a href="#">Trouble Tickets</a></li>
        <li><a href="#">Wissensbasis</a></li>-->
        <li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li>
        <li><g:link controller="person"><g:message code="person.plural" /></g:link></li>
        <!--<li><a href="#">Dokumente</a></li>
        <li><a href="#">Postfach</a></li>-->
      </ul>
    </li>
    <li>
      <a href="#">Bestand</a>
      <ul>
        <li><g:link controller="product"><g:message code="product.plural" /></g:link></li>
        <li><g:link controller="service"><g:message code="service.plural" /></g:link></li>
        <li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li>
        <!--<li><a href="#">Lieferanten</a></li>
        <li><a href="#">Preislisten</a></li>
        <li><a href="#">Einkaufsbestellungen</a></li>-->
        <li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li>
        <li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li>
        <li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li>
      </ul>
    </li>
    <li>
      <a href="#">Einstellungen</a>
      <ul>
        <li><g:link controller="user"><g:message code="user.plural" /></g:link></li>
      </ul>
    </li>
    <li>
      <select id="quick-access">
        <option value=""><g:message code="default.quickMenu" /></option>
        <option value="${createLink(controller: 'organization', action: 'create')}"><g:message code="default.quickMenu.organization" /></option>
        <option value="${createLink(controller: 'person', action: 'create')}"><g:message code="default.quickMenu.person" /></option>
        <option value="${createLink(controller: 'call', action: 'create')}"><g:message code="default.quickMenu.call" /></option>
        <option value="${createLink(controller: 'quote', action: 'create')}"><g:message code="default.quickMenu.quote" /></option>
        <option value="${createLink(controller: 'salesOrder', action: 'create')}"><g:message code="default.quickMenu.salesOrder" /></option>
        <option value="${createLink(controller: 'invoice', action: 'create')}"><g:message code="default.quickMenu.invoice" /></option>
        <!--<option>Neue Aufgabe</option>
        <option>Neues Dokument</option>-->
      </select>
    </li>
  </ul>
</nav>