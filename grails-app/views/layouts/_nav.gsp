<nav class="navbar navbar-default" role="navigation">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed"
        data-toggle="collapse" data-target="#main-nav">
        <span class="sr-only"><g:message code="default.btn.toggleNavigation" /></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a href="#" class="navbar-brand visible-xs"
        ><g:message code="default.appName"
      /></a>
    </div>
    <div id="main-nav" class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li>
          <g:link uri="/"><i class="fa fa-home"></i>
          <g:message code="menu.home" /></g:link>
        </li>
        <g:ifControllerAllowed controllers="organization person">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="button" aria-expanded="false">
            <g:message code="menu.contacts" /> <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" role="menu">
            <li><g:link controller="organization"><g:message code="organization.plural" /></g:link></li>
            <li><g:link controller="organization" params="[type: 1]"><g:message code="organization.customers" /></g:link></li>
            <li><g:link controller="organization" params="[type: 2]"><g:message code="organization.vendors" /></g:link></li>
            <li class="divider"></li>
            <li><g:link controller="person"><g:message code="person.plural" /></g:link></li>
          </ul>
        </li>
        </g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="quote salesOrder invoice dunning creditMemo purchaseInvoice">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="button" aria-expanded="false">
            <g:message code="menu.invoice" /> <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" role="menu">
            <g:ifControllerAllowed controllers="quote"><li><g:link controller="quote"><g:message code="quote.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="salesOrder"><li><g:link controller="salesOrder"><g:message code="salesOrder.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="invoice"><li><g:link controller="invoice"><g:message code="invoice.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="dunning"><li><g:link controller="dunning"><g:message code="dunning.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="creditMemo"><li><g:link controller="creditMemo"><g:message code="creditMemo.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="purchaseInvoice">
            <li class="divider"></li>
            <li><g:link controller="purchaseInvoice"><g:message code="purchaseInvoice.plural" /></g:link></li>
            </g:ifControllerAllowed>
            <li class="divider"></li>
            <g:ifControllerAllowed controllers="organization invoice dunning creditMemo">
            <li><g:link controller="report" action="salesJournal"><g:message code="report.salesJournal.title" /></g:link></li>
            </g:ifControllerAllowed>
          </ul>
        </li>
        </g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="product service">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="button" aria-expanded="false">
            <g:message code="menu.stock" /> <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" role="menu">
            <g:ifControllerAllowed controllers="product"><li><g:link controller="product"><g:message code="product.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="service"><li><g:link controller="service"><g:message code="service.plural" /></g:link></li></g:ifControllerAllowed>
          </ul>
        </li>
        </g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="call document note project calendar">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="button" aria-expanded="false">
            <g:message code="menu.office" /> <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" role="menu">
            <g:ifControllerAllowed controllers="call"><li><g:link controller="call"><g:message code="call.plural" /></g:link></li></g:ifControllerAllowed>
            <li class="divider"></li>
            <g:ifControllerAllowed controllers="document"><li><g:link controller="document"><g:message code="document.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="note"><li><g:link controller="note"><g:message code="note.plural" /></g:link></li></g:ifControllerAllowed>
            <li class="divider"></li>
            <g:ifControllerAllowed controllers="project"><li><g:link controller="project"><g:message code="project.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="calendar"><li><g:link controller="calendarEvent"><g:message code="calendarEvent.menu.label" /></g:link></li></g:ifControllerAllowed>
          </ul>
        </li>
        </g:ifControllerAllowed>
        <g:ifControllerAllowed controllers="helpdesk ticket">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="button" aria-expanded="false">
            <g:message code="menu.support" /> <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" role="menu">
            <g:ifControllerAllowed controllers="helpdesk"><li><g:link controller="helpdesk"><g:message code="helpdesk.plural" /></g:link></li></g:ifControllerAllowed>
            <g:ifControllerAllowed controllers="ticket"><li><g:link controller="ticket"><g:message code="ticket.plural" /></g:link></li></g:ifControllerAllowed>
          </ul>
        </li>
        </g:ifControllerAllowed>
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="button" aria-expanded="false">
            <g:message code="menu.settings" /> <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" role="menu">
            <g:ifAdmin><li><g:link controller="user"><g:message code="user.plural" /></g:link></li></g:ifAdmin>
            <li><g:link controller="user" action="settingsIndex"><g:message code="user.settings.title" /></g:link></li>
            <g:ifAdmin><li><g:link controller="config"><g:message code="config.title" /></g:link></li></g:ifAdmin>
          </ul>
        </li>
<%--
        <li>
          <a href="#" id="calculator-button"
            title="${message(code: 'calculator.button.label')}"><i class="fa fa-calculator"></i></a>
          <div class="calculator-dialog" title="${message(code: 'calculator.title')}">
            <div class="calculator"></div>
          </div>
        </li>
--%>
        <li class="visible-xs">
          <g:link controller="user" action="logout"
            class="btn btn-warning navbar-btn">
            <i class="fa fa-sign-out"></i> <g:message code="default.logout" />
          </g:link>
        </li>
        <li class="visible-xs">
          <form>
          <div class="input-group">
            <input type="search" class="form-control"
              placeholder="${message(code: 'default.search.label')}" />
            <span class="input-group-btn">
              <button type="submit" class="btn btn-default">
                <i class="fa fa-search"></i>
                <span class="sr-only"
                  ><g:message code="default.search.button.label"
                /></span>
              </button>
            </span>
          </div>
        </form>
        </li>
      </ul>
    </div>
  </div>
</nav>
