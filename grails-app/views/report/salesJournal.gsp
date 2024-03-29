<%@ page import="grails.util.GrailsNameUtils" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="report.salesJournal.title" /></title>
    <meta name="stylesheet" content="report-sales-journal" />
    <meta name="caption"
      content="${message(code: 'report.salesJournal.title')}" />
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
        ><g:message code="report.salesJournal.title.short"
      /></h1>
    </content>
    <content tag="toolbar">
      <button class="btn btn-default btn-print"
        ><i class="fa fa-print"></i>
        <g:message code="default.button.print.label"
      /></button>
    </content>

    <g:render template="/layouts/flashMessage" />
    <div class="btn-group month-year-selector" role="group"
      aria-label="${message(code: 'report.salesJournal.monthYearSelector.label')}">
      <g:monthBar action="salesJournal"
        params="[sort: params.sort, order: params.order]"
        activeMonth="${activeMonth}" />
      <g:link action="salesJournal"
        params="[sort: params.sort, order: params.order]"
        class="btn btn-default${(activeMonth == 0) ? ' active' : ''} btn-month btn-whole-year"
        data-month="0"
        ><g:message code="report.salesJournal.wholeYear.label"
      /></g:link>
      <div class="btn-group" role="group">
        <button type="button" class="btn btn-default dropdown-toggle"
          data-toggle="dropdown" aria-haspopup="true"
          aria-owns="filter-bar-years">
          ${activeYear}
          <span class="caret"></span>
        </button>
        <ul class="dropdown-menu year-selector"
          data-current-year="${activeYear}" role="menu" aria-expanded="false">
          <g:each var="year" in="${(yearEnd..yearStart)}">
          <li><a href="#" data-year="${year}">${year}</a></li>
          </g:each>
        </ul>
      </div>
    </div>
    <h2>
      <g:if test="${activeMonth}">
      <g:month month="${activeMonth}" year="${activeYear}" />
      </g:if>
      <g:else>
      <g:message code="report.salesJournal.yearlyOverview.label"
        args="${[activeYear]}" />
      </g:else>
    </h2>

    <div class="detail-view">
      <g:if test="${invoicingTransactionInstanceList}">
      <div class="table-responsive">
        <table class="table data-table price-table report report-sales-journal">
          <thead>
            <tr>
              <th colspan="4"><g:message code="report.salesJournal.invoiceDunningCreditMemo.label" /></th>
              <th colspan="2"><g:message code="report.salesJournal.due.label" /></th>
              <th colspan="2"><g:message code="report.salesJournal.payment.label" /></th>
              <th colspan="2"><g:message code="report.salesJournal.balance.label" /></th>
            </tr>
            <tr>
              <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label.short')}" />
              <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label')}" />
              <th><g:message code="invoicingTransaction.organization.label" /></th>
              <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label')}" />
              <g:sortableColumn property="dueDatePayment" title="${message(code: 'report.salesJournal.date.label')}" />
              <g:sortableColumn property="total" title="${message(code: 'report.salesJournal.sum.label')}" />
              <g:sortableColumn property="paymentDate" title="${message(code: 'report.salesJournal.date.label')}" />
              <g:sortableColumn property="paymentAmount" title="${message(code: 'report.salesJournal.sum.label')}" />
              <th><g:message code="report.salesJournal.sum.label" /></th>
              <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label.short')}" />
            </tr>
          </thead>
          <tfoot>
            <tr class="row-total">
              <td colspan="4"><g:message code="report.salesJournal.total.label" /></td>
              <td colspan="2" class="col-type-currency"><g:formatCurrency number="${total}" displayZero="true" external="true" /></td>
              <td colspan="2" class="col-type-currency"><g:formatCurrency number="${totalPaymentAmount}" displayZero="true" external="true" /></td>
              <td class="col-type-currency"><g:formatCurrency number="${totalPaymentAmount - total}" displayZero="true" external="true" /></td>
              <td></td>
            </tr>
          </tfoot>
          <tbody>
          <g:set var="group" value="I" />
          <g:each in="${invoicingTransactionInstanceList}" status="i"
            var="invoicingTransactionInstance">
          <tr class="row-${GrailsNameUtils.getScriptName(invoicingTransactionInstance.class)}${invoicingTransactionInstance.type != group ? ' row-divider' : ''}">
            <g:if test="${invoicingTransactionInstance.type != group}"
              ><g:set var="group" value="${invoicingTransactionInstance.type}"
            /></g:if>
            <td class="col-type-date sales-journal-doc-date"><g:formatDate date="${invoicingTransactionInstance?.docDate}" formatName="default.format.date" /></td>
            <td class="col-type-id sales-journal-number"><g:link controller="${invoicingTransactionInstance.type == 'I' ? 'invoice' : (invoicingTransactionInstance.type == 'D' ? 'dunning' : 'creditMemo')}" action="show" id="${invoicingTransactionInstance.id}"><g:fullNumber bean="${invoicingTransactionInstance}"/></g:link></td>
            <td class="col-type-ref sales-journal-organization"><g:link controller="organization" action="show" id="${invoicingTransactionInstance.organization?.id}"><g:fieldValue bean="${invoicingTransactionInstance}" field="organization" /></g:link></td>
            <td class="col-type-string sales-journal-subject"><g:link controller="${invoicingTransactionInstance.type == 'I' ? 'invoice' : (invoicingTransactionInstance.type == 'D' ? 'dunning' : 'creditMemo')}" action="show" id="${invoicingTransactionInstance.id}">${invoicingTransactionInstance.subject.replaceAll(~/_{2,}/, ' ')}</g:link></td>
            <td class="col-type-date sales-journal-due-date-payment"><g:if test="${invoicingTransactionInstance.type == 'C'}">—</g:if><g:else><g:formatDate date="${invoicingTransactionInstance?.dueDatePayment}" formatName="default.format.date" /></g:else></td>
            <td class="col-type-currency sales-journal-total"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-invoicingTransactionInstance?.total}" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.total}" displayZero="true" external="true" /></g:else></td>
            <td class="col-type-date sales-journal-payment-date"><g:formatDate date="${invoicingTransactionInstance?.paymentDate}" formatName="default.format.date" /></td>
            <td class="col-type-currency sales-journal-payment-amount"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-(invoicingTransactionInstance?.paymentAmount ?: 0)}" displayZero="true" external="true" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.paymentAmount}" displayZero="true" external="true" /></g:else></td>
%{--            <td class="col-type-currency sales-journal-sum balance-state balance-state-${invoicingTransactionInstance?.balanceColor}"><g:formatCurrency number="${invoicingTransactionInstance?.balance}" displayZero="true" external="true" /></td>--}%
            <td class="col-type-currency sales-journal-sum balance-state"><g:formatCurrency number="${invoicingTransactionInstance?.balance}" displayZero="true" external="true" /></td>
%{--            <td class="col-type-status sales-journal-stage payment-state payment-state-${invoicingTransactionInstance?.paymentStateColor}"><g:fieldValue bean="${invoicingTransactionInstance}" field="stage" /></td>--}%
            <td class="col-type-status sales-journal-stage payment-state"><g:fieldValue bean="${invoicingTransactionInstance}" field="stage" /></td>
          </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      </g:if>
      <g:else>
        <div class="well well-lg empty-list">
          <p><g:message code="default.list.empty" /></p>
        </div>
      </g:else>
    </div>

    <content tag="scripts">
      <asset:javascript src="report-time-span" />
    </content>
  </body>
</html>
