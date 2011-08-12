<nav>
  <ul id="main-menu">
    <li>
      <a href="#">Home</a>
      <ul>
        <li><a href="${createLink(uri: '/')}">Startseite</a></li>
        <!--<li><a href="#">Kalender</a></li>-->
        <g:ifModuleAllowed modules="call"><li><g:link controller="call"><g:message code="call.plural" /></g:link></li></g:ifModuleAllowed>
        <!--<li><a href="#">Postfach</a></li>-->
      </ul>
    </li>
    <li>
      <a href="#">Marketing</a>
      <ul>
        <!--<li><a href="#">Kampagnen</a></li>-->
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="person"><g:message code="person.plural" /></g:link></li></g:ifModuleAllowed>
        <!--<li><a href="#">Leads</a></li>
        <li><a href="#">Kalender</a></li>
        <li><a href="#">Postfach</a></li>-->
        <g:ifModuleAllowed modules="note"><li><g:link controller="note"><g:message code="note.plural" /></g:link></li></g:ifModuleAllowed>
      </ul>
    </li>
    <li>
      <a href="#">Vertrieb</a>
      <ul>
        <!--<li><a href="#">Leads</a></li>-->
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="person"><g:message code="person.plural" /></g:link></li></g:ifModuleAllowed>
        <!--<li><a href="#">Verkaufspotentiale</a></li>-->
        <g:ifModuleAllowed modules="contact"><li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li></g:ifModuleAllowed>
      </ul>
    </li>
    <li>
      <a href="#">Support</a>
      <ul>
        <!--<li><a href="#">Trouble Tickets</a></li>
        <li><a href="#">Wissensbasis</a></li>-->
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="person"><g:message code="person.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="note"><li><g:link controller="note"><g:message code="note.plural" /></g:link></li></g:ifModuleAllowed>
        <!--<li><a href="#">Postfach</a></li>-->
      </ul>
    </li>
    <li>
      <a href="#">Bestand</a>
      <ul>
        <g:ifModuleAllowed modules="product"><li><g:link controller="product"><g:message code="product.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="service"><li><g:link controller="service"><g:message code="service.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li></g:ifModuleAllowed>
        <!--<li><a href="#">Lieferanten</a></li>
        <li><a href="#">Preislisten</a></li>
        <li><a href="#">Einkaufsbestellungen</a></li>-->
        <g:ifModuleAllowed modules="quote"><li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="salesOrder"><li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="invoice"><li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li></g:ifModuleAllowed>
      </ul>
    </li>
    <g:ifAdmin>
    <li>
      <a href="#">Einstellungen</a>
      <ul>
        <li><g:link controller="user"><g:message code="user.plural" /></g:link></li>
      </ul>
    </li>
    </g:ifAdmin>
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