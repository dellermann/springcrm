<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="report.turnover.title" /></title>
    <meta name="stylesheet" content="report-turnover" />
    <meta name="caption" content="${message(code: 'report.turnover.title')}" />
    <meta name="noSubcaption" content="true" />
  </head>

  <body>
    <content tag="backLink">
      <g:link controller="overview" action="index"
        class="navbar-back-link visible-xs"
        ><i class="fa fa-home"></i>
        <span class="sr-only"
          ><g:message code="default.button.home.label"
        /></span
      ></g:link>
      <h1 class="navbar-title visible-xs"
        ><g:message code="report.turnover.title"
      /></h1>
    </content>
    <content tag="toolbar">
      <button class="btn btn-default btn-print"
        ><i class="fa fa-print"></i>
        <g:message code="default.button.print.label"
      /></button>
    </content>

    <g:render template="/layouts/flashMessage" />
    <g:form action="turnoverReport" method="get"
      class="form-inline organization-selector-form">
      <div class="form-group">
        <label for="organization" class="sr-only">
          <g:message code="report.turnover.organization.label" />
        </label>
        <select id="organization-select" name="organization.id"
          data-find-url="${createLink(controller: 'organization', action: 'find', params: [type: 1])}">
          <g:if test="${organizationInstance}">
          <option value="${organizationInstance.id}"
            >${organizationInstance}</option
          >
          </g:if>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">
        <g:message code="report.turnover.submit.label" />
      </button>
    </g:form>
    <g:if test="${organizationInstance}">
    <div class="btn-group month-year-selector" role="group"
      aria-label="${message(code: 'report.turnover.monthYearSelector.label')}">
      <g:monthBar action="turnoverReport"
        params="['organization.id': organizationInstance.id]"
        activeMonth="${activeMonth}" />
      <g:link action="turnoverReport"
        params="['organization.id': organizationInstance.id]"
        class="btn btn-default${(activeMonth == 0) ? ' active' : ''} btn-month btn-whole-year"
        data-month="0"
        ><g:message code="report.turnover.wholeYear.label"
      /></g:link>
      <div class="btn-group" role="group">
        <button type="button" class="btn btn-default dropdown-toggle"
          data-toggle="dropdown" aria-haspopup="true"
          aria-owns="filter-bar-years">
          <g:if test="${activeYear}">${activeYear}</g:if>
          <g:else
            ><g:message code="report.turnover.completeOverview.btn"
          /></g:else>
          <span class="caret"></span>
        </button>
        <ul class="dropdown-menu year-selector"
          data-current-year="${activeYear ?: currentYear}" role="menu"
          aria-expanded="false">
          <li
            ><a href="#"
              ><g:message code="report.turnover.completeOverview.btn"
            /></a
          ></li>
          <g:each var="year" in="${(yearEnd..yearStart)}">
          <li><a href="#" data-year="${year}">${year}</a></li>
          </g:each>
        </ul>
      </div>
    </div>
    </g:if>
    <h2>
      <g:if test="${organizationInstance}">
      ${organizationInstance}
      <g:if test="${activeMonth}">
      <g:month month="${activeMonth}" year="${activeYear}" />
      </g:if>
      <g:elseif test="${activeYear}">
      <g:message code="report.turnover.yearlyOverview.label"
        args="${[activeYear]}" />
      </g:elseif>
      <g:else>
      <g:message code="report.turnover.completeOverview.label" />
      </g:else>
      </g:if>
      <g:else><g:message code="report.turnover.overview.label" /></g:else>
    </h2>

    <div class="detail-view">
      <g:if test="${organizationInstance}">
      <g:if test="${invoiceInstanceList}">
      <div class="table-responsive">
        <table class="table data-table price-table report report-turnover">
          <thead>
            <tr>
              <th colspan="3"><g:message code="invoice.label" /></th>
              <th colspan="4"><g:message code="report.turnover.turnover.label" /></th>
              <g:sortableColumn rowspan="2" property="stage" params="['organization.id': organizationInstance.id]" title="${message(code: 'invoice.stage.label.short')}" />
            </tr>
            <tr>
              <g:sortableColumn property="docDate" params="['organization.id': organizationInstance.id]" title="${message(code: 'invoice.docDate.label.short')}" />
              <g:sortableColumn property="number" params="['organization.id': organizationInstance.id]" title="${message(code: 'invoicingTransaction.number.label')}" />
              <g:sortableColumn property="subject" params="['organization.id': organizationInstance.id]" title="${message(code: 'invoicingTransaction.subject.label')}" />
              <g:sortableColumn property="turnover" params="['organization.id': organizationInstance.id]" title="${message(code: 'report.turnover.turnover.total')}" />
              <g:sortableColumn property="turnoverProducts" params="['organization.id': organizationInstance.id]" title="${message(code: 'report.turnover.turnoverProducts.label')}" />
              <g:sortableColumn property="turnoverWorks" params="['organization.id': organizationInstance.id]" title="${message(code: 'report.turnover.turnoverWorks.label')}" />
              <g:sortableColumn property="turnoverOtherSalesItems" params="['organization.id': organizationInstance.id]" title="${message(code: 'report.turnover.turnoverOtherSalesItems.label')}" />
            </tr>
          </thead>
          <tfoot>
            <tr class="row-total">
              <td colspan="3"><g:message code="report.turnover.total.label" /></td>
              <td class="col-type-currency"><g:formatCurrency number="${total}" displayZero="true" external="true" /></td>
              <td class="col-type-currency"><g:formatCurrency number="${totalProducts}" displayZero="true" external="true" /></td>
              <td class="col-type-currency"><g:formatCurrency number="${totalWorks}" displayZero="true" external="true" /></td>
              <td class="col-type-currency"><g:formatCurrency number="${totalOtherItems}" displayZero="true" external="true" /></td>
              <td></td>
            </tr>
          </tfoot>
          <tbody>
          <g:each in="${invoiceInstanceList}" var="invoiceInstance">
          <tr>
            <td class="col-type-date turnover-report-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
            <td class="col-type-id turnover-report-number"><g:link controller="invoice" action="show" id="${invoiceInstance.id}"><g:fieldValue bean="${invoiceInstance}" field="fullNumber" /></g:link></td>
            <td class="col-type-string turnover-report-subject"><g:link controller="invoice" action="show" id="${invoiceInstance.id}">${invoiceInstance.subject.replaceAll(~/_{2,}/, ' ')}</g:link></td>
            <td class="col-type-currency turnover-report-turnover"><g:formatCurrency number="${invoiceInstance.turnover}" displayZero="true" external="true" /></td>
            <td class="col-type-currency turnover-report-turnover-products"><g:formatCurrency number="${invoiceInstance.turnoverProducts}" displayZero="true" external="true" /></td>
            <td class="col-type-currency turnover-report-turnover-works"><g:formatCurrency number="${invoiceInstance.turnoverWorks}" displayZero="true" external="true" /></td>
            <td class="col-type-currency turnover-report-turnover-other-items"><g:formatCurrency number="${invoiceInstance.turnoverOtherSalesItems}" displayZero="true" external="true" /></td>
            <td class="col-type-status turnover-report-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}"><g:fieldValue bean="${invoiceInstance}" field="stage" /></td>
          </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      </g:if>
      <g:else>
      <div class="well well-lg empty-list">
        <p><g:message code="report.turnover.list.empty" /></p>
      </div>
      </g:else>
      </g:if>
      <g:else>
      <div class="well well-lg empty-list">
        <p><g:message code="report.turnover.organization.notSelected" /></p>
      </div>
      </g:else>
    </div>

    <content tag="scripts">
      <asset:javascript src="report-time-span" />
    </content>
  </body>
</html>
