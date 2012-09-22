<%@ page import="org.amcworld.springcrm.Quote" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="report.salesJournal.title" /></title>
  <r:require modules="reportSalesJournal" />
  <style>
  @media print {
    @page {
      size: landscape;
    }
  }
  </style>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="report.salesJournal.title" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a id="print-btn" href="#" class="white"><g:message code="default.button.print.label" /></a></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${invoicingTransactionInstanceList}">
    <div id="filter-bar" class="filter-bar" data-load-url="${createLink(action: 'salesJournal')}">
      <g:set var="monthNames" value="${java.text.DateFormatSymbols.instance.months}" />
      <g:set var="shortMonthNames" value="${java.text.DateFormatSymbols.instance.shortMonths}" />
      <ul id="month-selector" class="letter-bar">
      <g:each var="i" in="${(1..12)}">
        <li class="available ${(currentMonth == i) ? 'current' : ''}" data-month="${i}"><g:link action="salesJournal">${shortMonthNames[i - 1]}</g:link></li>
      </g:each>
        <li class="available ${currentMonth == 0 ? 'current' : ''}" data-month="0"><g:link action="salesJournal"><g:message code="report.salesJournal.wholeYear.label" default="Year {0}" args="${[currentYear]}" /></g:link></li>
      </ul>
      <g:select name="year-selector" from="${(yearStart..yearEnd)}" value="${currentYear}" />
    </div>
    <h3><g:if test="${currentMonth}">${monthNames[currentMonth - 1]} ${currentYear}</g:if><g:else><g:message code="report.salesJournal.yearlyOverview.label" default="Overview of year {0}" args="${[currentYear]}" /></g:else></h3>
    <table class="content-table report">
      <thead>
        <tr>
          <th id="content-table-headers-sales-journal-invoice" colspan="4"><g:message code="report.salesJournal.invoice.label" default="Invoice" /></th>
          <th id="content-table-headers-sales-journal-due" colspan="2"><g:message code="report.salesJournal.due.label" default="Due" /></th>
          <th id="content-table-headers-sales-journal-payment" colspan="2"><g:message code="report.salesJournal.payment.label" default="Payment" /></th>
          <th id="content-table-headers-sales-journal-balance" colspan="2"><g:message code="report.salesJournal.balance.label" default="Balance" /></th>
        </tr>
        <tr>
          <g:sortableColumn id="content-table-headers-sales-journal-doc-date" property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn id="content-table-headers-sales-journal-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn id="content-table-headers-sales-journal-organization" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn id="content-table-headers-sales-journal-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn id="content-table-headers-sales-journal-due-date-payment" property="dueDatePayment" title="${message(code: 'report.salesJournal.date.label', default: 'Date')}" />
          <g:sortableColumn id="content-table-headers-sales-journal-total" property="total" title="${message(code: 'report.salesJournal.sum.label', default: 'Sum')}" style="width: 6em;" />
          <g:sortableColumn id="content-table-headers-sales-journal-payment-date" property="paymentDate" title="${message(code: 'report.salesJournal.date.label', default: 'Date')}" />
          <g:sortableColumn id="content-table-headers-sales-journal-payment-amount" property="paymentAmount" title="${message(code: 'report.salesJournal.sum.label', default: 'Sum')}" style="width: 6em;" />
          <th id="content-table-headers-sales-journal-sum"><g:message code="report.salesJournal.sum.label" default="Sum" /></th>
          <g:sortableColumn id="content-table-headers-sales-journal-stage" property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" />
        </tr>
      </thead>
      <g:if test="${invoicingTransactionInstanceList}">
      <tfoot>
        <tr>
          <td colspan="4" class="align-right"><g:message code="report.salesJournal.total.label" default="Total" /></td>
          <td colspan="2" class="content-table-type-currency"><g:formatCurrency number="${total}" displayZero="true" /></td>
          <td colspan="2" class="content-table-type-currency"><g:formatCurrency number="${totalPaymentAmount}" displayZero="true" /></td>
          <td class="content-table-type-currency"><g:formatCurrency number="${totalPaymentAmount - total}" displayZero="true" /></td>
          <td></td>
        </tr>
      </tfoot>
      </g:if>
      <tbody>
      <g:set var="group" value="I" />
      <g:each in="${invoicingTransactionInstanceList}" status="i" var="invoicingTransactionInstance">
        <g:if test="${invoicingTransactionInstance.type != group}"><g:set var="group" value="${invoicingTransactionInstance.type}" /><tr class="content-table-divider"></g:if><g:else><tr></g:else>
          <td class="content-table-type-date content-table-column-sales-journal-doc-date" headers="content-table-headers-sales-journal-invoice content-table-headers-sales-journal-doc-date" style="text-align: center;"><g:formatDate date="${invoicingTransactionInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="content-table-type-id content-table-column-sales-journal-number" headers="content-table-headers-sales-journal-invoice content-table-headers-sales-journal-number"><g:link controller="${invoicingTransactionInstance.type == 'I' ? 'invoice' : (invoicingTransactionInstance.type == 'D' ? 'dunning' : 'creditMemo')}" action="show" id="${invoicingTransactionInstance.id}">${fieldValue(bean: invoicingTransactionInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-ref content-table-column-sales-journal-organization" headers="content-table-headers-sales-journal-invoice content-table-headers-sales-journal-organization"><g:link controller="organization" action="show" id="${invoicingTransactionInstance.organization?.id}">${fieldValue(bean: invoicingTransactionInstance, field: "organization")}</g:link></td>
          <td class="content-table-type-string content-table-column-sales-journal-subject" headers="content-table-headers-sales-journal-invoice content-table-headers-sales-journal-subject"><g:link controller="${invoicingTransactionInstance.type == 'I' ? 'invoice' : (invoicingTransactionInstance.type == 'D' ? 'dunning' : 'creditMemo')}" action="show" id="${invoicingTransactionInstance.id}">${fieldValue(bean: invoicingTransactionInstance, field: "subject")}</g:link></td>
          <td class="content-table-type-date content-table-column-sales-journal-due-date-payment" headers="content-table-headers-sales-journal-due content-table-headers-sales-journal-due-date-payment"><g:if test="${invoicingTransactionInstance.type == 'C'}">—</g:if><g:else><g:formatDate date="${invoicingTransactionInstance?.dueDatePayment}" formatName="default.format.date" /></g:else></td>
          <td class="content-table-type-currency content-table-column-sales-journal-total" headers="content-table-headers-sales-journal-due content-table-headers-sales-journal-total"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-invoicingTransactionInstance?.total}" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.total}" /></g:else></td>
          <td class="content-table-type-date content-table-column-sales-journal-payment-date" headers="content-table-headers-sales-journal-payment content-table-headers-sales-journal-payment-date"><g:formatDate date="${invoicingTransactionInstance?.paymentDate}" formatName="default.format.date" /></td>
          <td class="content-table-type-currency content-table-column-sales-journal-payment-amount" headers="content-table-headers-sales-journal-payment content-table-headers-sales-journal-payment-amount"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-(invoicingTransactionInstance?.paymentAmount ?: 0)}" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.paymentAmount}" /></g:else></td>
          <td class="content-table-type-currency content-table-column-sales-journal-sum balance-state balance-state-${invoicingTransactionInstance?.balanceColor}" headers="content-table-headers-sales-journal-balance content-table-headers-sales-journal-sum"><g:formatCurrency number="${invoicingTransactionInstance?.balance}" displayZero="true" /></td>
          <td class="content-table-type-status content-table-column-sales-journal-stage payment-state payment-state-${invoicingTransactionInstance?.paymentStateColor}" headers="content-table-headers-sales-journal-balance content-table-headers-sales-journal-stage">${fieldValue(bean: invoicingTransactionInstance, field: "stage")}</td>
        </tr>
      </g:each>
      </tbody>
    </table>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
      </div>
    </g:else>
  </section>
</body>
</html>
