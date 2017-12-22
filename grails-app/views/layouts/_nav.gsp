<nav class="navbar navbar-default" role="navigation">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed"
        data-toggle="collapse" data-target="#main-nav"
        aria-haspopup="true" aria-owns="main-nav">
        <span class="sr-only"
          ><g:message code="default.btn.toggleNavigation"
        /></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <g:render template="/layouts/backLink"/>
    </div>
    <div id="main-nav" class="collapse navbar-collapse">
      <ul class="nav navbar-nav" role="menubar">
        <li>
          <g:link controller="overview" action="index" role="menuitem"
            ><i class="fa fa-home"></i>
            <g:message code="menu.home"
          /></g:link>
        </li>
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_CONTACT">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="menuitem" aria-haspopup="true" aria-owns="menu-contacts">
            <g:message code="menu.contacts"/> <span class="caret"></span>
          </a>
          <ul id="menu-contacts" class="dropdown-menu" role="menu"
            aria-expanded="false">
            <li role="menuitem"><g:link controller="organization" params="[offset: 0]"><g:message code="organization.plural"/></g:link></li>
            <li role="menuitem"><g:link controller="organization" params="[listType: 1, offset: 0]"><g:message code="organization.customers"/></g:link></li>
            <li role="menuitem"><g:link controller="organization" params="[listType: 2, offset: 0]"><g:message code="organization.vendors"/></g:link></li>
            <li class="divider" role="presentation"></li>
            <li role="menuitem"><g:link controller="person"><g:message code="person.plural"/></g:link></li>
          </ul>
        </li>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_QUOTE,ROLE_SALES_ORDER,ROLE_INVOICE,ROLE_DUNNING,ROLE_CREDIT_MEMO,ROLE_PURCHASE_INVOICE">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="menuitem" aria-haspopup="true" aria-owns="menu-invoice">
            <g:message code="menu.invoice"/> <span class="caret"></span>
          </a>
          <ul id="menu-invoice" class="dropdown-menu" role="menu"
            aria-expanded="false">
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_QUOTE"><li role="menuitem"><g:link controller="quote"><g:message code="quote.plural"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_SALES_ORDER"><li role="menuitem"><g:link controller="salesOrder"><g:message code="salesOrder.plural"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_INVOICE"><li role="menuitem"><g:link controller="invoice"><g:message code="invoice.plural"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_DUNNING"><li role="menuitem"><g:link controller="dunning"><g:message code="dunning.plural"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_CREDIT_MEMO"><li role="menuitem"><g:link controller="creditMemo"><g:message code="creditMemo.plural"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PURCHASE_INVOICE">
            <li class="divider" role="presentation"></li>
            <li role="menuitem"><g:link controller="purchaseInvoice"><g:message code="purchaseInvoice.plural"/></g:link></li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_REPORT">
            <li class="divider" role="presentation"></li>
            <li role="menuitem"><g:link controller="report" action="salesJournal"><g:message code="report.salesJournal.title"/></g:link></li>
            <li role="menuitem"><g:link controller="report" action="outstandingItems"><g:message code="report.outstandingItems.title"/></g:link></li>
            <li role="menuitem"><g:link controller="report" action="turnoverReport"><g:message code="report.turnover.title"/></g:link></li>
            <li role="menuitem"><g:link controller="report" action="turnoverOverview"><g:message code="report.turnoverOverview.title"/></g:link></li>
            </sec:ifAnyGranted>
          </ul>
        </li>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PRODUCT,ROLE_WORK">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="menuitem" aria-haspopup="true" aria-owns="menu-stock">
            <g:message code="menu.stock"/> <span class="caret"></span>
          </a>
          <ul id="menu-stock" class="dropdown-menu" role="menu"
            aria-expanded="false">
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PRODUCT"><li role="menuitem"><g:link controller="product"><g:message code="product.plural"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_WORK"><li role="menuitem"><g:link controller="work"><g:message code="work.plural"/></g:link></li></sec:ifAnyGranted>
          </ul>
        </li>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_CALL,ROLE_DOCUMENT,ROLE_NOTE,ROLE_PROJECT,ROLE_CALENDAR">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="menuitem" aria-haspopup="true" aria-owns="menu-office">
            <g:message code="menu.office"/> <span class="caret"></span>
          </a>
          <ul id="menu-office" class="dropdown-menu" role="menu"
            aria-expanded="false">
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_CALL"><li role="menuitem"><g:link controller="call"><g:message code="call.plural"/></g:link></li></sec:ifAnyGranted>
            <li class="divider" role="presentation"></li>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_DOCUMENT"><li role="menuitem"><g:link controller="document"><g:message code="document.plural"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_NOTE"><li role="menuitem"><g:link controller="note"><g:message code="note.plural"/></g:link></li></sec:ifAnyGranted>
            <li class="divider" role="presentation"></li>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROJECT"><li role="menuitem"><g:link controller="project"><g:message code="project.plural"/></g:link></li></sec:ifAnyGranted>
            <%--
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_CALENDAR"><li role="menuitem"><g:link controller="calendarEvent"><g:message code="calendarEvent.menu.label"/></g:link></li></g:ifControllerAllowed>
            --%>
          </ul>
        </li>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_HELPDESK,ROLE_TICKET">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="menuitem" aria-haspopup="true" aria-owns="menu-support">
            <g:message code="menu.support"/> <span class="caret"></span>
          </a>
          <ul id="menu-support" class="dropdown-menu" role="menu"
            aria-expanded="false">
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_HELPDESK"><li role="menuitem"><g:link controller="helpdesk"><g:message code="helpdesk.plural"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_TICKET"><li role="menuitem"><g:link controller="ticket"><g:message code="ticket.plural"/></g:link></li></sec:ifAnyGranted>
          </ul>
        </li>
        </sec:ifAnyGranted>
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"
            role="menuitem" aria-haspopup="true" aria-owns="menu-settings">
            <g:message code="menu.settings"/> <span class="caret"></span>
          </a>
          <ul id="menu-settings" class="dropdown-menu" role="menu"
            aria-expanded="false">
            <sec:ifAnyGranted roles="ROLE_ADMIN"><li role="menuitem"><g:link controller="user"><g:message code="user.plural"/></g:link></li></sec:ifAnyGranted>
            <li role="menuitem"><g:link controller="user" action="settingsIndex"><g:message code="user.settings.title"/></g:link></li>
            <sec:ifAnyGranted roles="ROLE_ADMIN"><li role="menuitem"><g:link controller="config"><g:message code="config.title"/></g:link></li></sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_BOILERPLATE"><li role="menuitem"><g:link controller="boilerplate"><g:message code="boilerplate.plural"/></g:link></li></sec:ifAnyGranted>
          </ul>
        </li>
        <li class="hidden-xs">
          <a href="#" class="dropdown-toggle"
            title="${message(code: 'calculator.button.label')}"
            data-toggle="dropdown" role="menuitem" aria-haspopup="true"
            aria-owns="calculator"
            ><i class="fa fa-calculator"></i>
            <span class="sr-only"
              ><g:message code="calculator.button.label"
            /></span
          ></a>
          <div id="calculator"
            class="dropdown-menu tool-popup calculator-popup"
            role="grid" aria-expanded="false">
            <div></div>
          </div>
        </li>
        <li class="hidden-xs">
          <a href="#" class="dropdown-toggle"
            title="${message(code: 'vatCalculator.button.label')}"
            data-toggle="dropdown" role="menuitem" aria-haspopup="true"
            aria-owns="vat-calculator"
            ><i class="fa fa-money"></i>
            <span class="sr-only"
              ><g:message code="vatCalculator.button.label"
            /></span
          ></a>
          <div id="vat-calculator"
            class="dropdown-menu tool-popup vat-calculator-popup"
            role="grid" aria-expanded="false">
            <div></div>
          </div>
        </li>
        <li class="visible-xs">
          <g:form controller="search" method="get" class="search-form"
            role="search">
            <div class="form-group">
              <label for="search-xs" class="sr-only"
                ><g:message code="default.search.label"
              /></label>
              <div class="input-group">
                <g:textField type="search" id="search-xs" name="query"
                  value="${query}" class="form-control" required="required"
                  placeholder="${message(code: 'default.search.placeholder')}"/>
                <span class="input-group-btn">
                  <button type="submit" class="btn btn-default">
                    <i class="fa fa-search"></i>
                    <span class="sr-only"
                      ><g:message code="default.search.button.label"
                    /></span>
                  </button>
                </span>
              </div>
            </div>
          </g:form>
        </li>
        <li class="visible-xs">
          <g:link controller="user" action="logout"
            class="btn btn-warning navbar-btn" role="button">
            <i class="fa fa-sign-out"></i> <g:message code="default.logout"/>
          </g:link>
        </li>
      </ul>
    </div>
  </div>
</nav>
