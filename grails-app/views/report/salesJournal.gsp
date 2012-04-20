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
          <th colspan="4"><g:message code="report.salesJournal.invoice.label" default="Invoice" /></th>
          <th colspan="2"><g:message code="report.salesJournal.due.label" default="Due" /></th>
          <th colspan="2"><g:message code="report.salesJournal.payment.label" default="Payment" /></th>
          <th colspan="2"><g:message code="report.salesJournal.balance.label" default="Balance" /></th>
        </tr>
        <tr>
          <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn property="dueDatePayment" title="${message(code: 'report.salesJournal.date.label', default: 'Date')}" />
          <g:sortableColumn property="total" title="${message(code: 'report.salesJournal.sum.label', default: 'Sum')}" style="width: 6em;" />
          <g:sortableColumn property="paymentDate" title="${message(code: 'report.salesJournal.date.label', default: 'Date')}" />
          <g:sortableColumn property="paymentAmount" title="${message(code: 'report.salesJournal.sum.label', default: 'Sum')}" style="width: 6em;" />
          <th style="width: 6em;"><g:message code="report.salesJournal.sum.label" default="Sum" /></th>
          <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" />
        </tr>
      </thead>
      <g:if test="${invoicingTransactionInstanceList}">
      <tfoot>
        <tr>
          <td colspan="4" style="text-align: right;"><g:message code="report.salesJournal.total.label" default="Total" /></td>
          <td colspan="2" style="text-align: right;"><g:formatCurrency number="${total}" displayZero="true" /></td>
          <td colspan="2" style="text-align: right;"><g:formatCurrency number="${totalPaymentAmount}" displayZero="true" /></td>
          <td style="text-align: right;"><g:formatCurrency number="${totalPaymentAmount - total}" displayZero="true" /></td>
          <td></td>
        </tr>
      </tfoot>
      </g:if>
      <tbody>
      <g:each in="${invoicingTransactionInstanceList}" status="i" var="invoicingTransactionInstance">
        <tr>
          <td style="text-align: center;"><g:formatDate date="${invoicingTransactionInstance?.docDate}" formatName="default.format.date" /></td>
          <td><g:link controller="${invoicingTransactionInstance.type == 'I' ? 'invoice' : 'dunning'}" action="show" id="${invoicingTransactionInstance.id}">${fieldValue(bean: invoicingTransactionInstance, field: "fullNumber")}</g:link></td>
          <td><g:link controller="organization" action="show" id="${invoicingTransactionInstance.organization?.id}">${fieldValue(bean: invoicingTransactionInstance, field: "organization")}</g:link></td>
          <td><g:link action="show" id="${invoicingTransactionInstance.id}">${fieldValue(bean: invoicingTransactionInstance, field: "subject")}</g:link></td>
          <td style="text-align: center;"><g:if test="${invoicingTransactionInstance.type == 'C'}">—</g:if><g:else><g:formatDate date="${invoicingTransactionInstance?.dueDatePayment}" formatName="default.format.date" /></g:else></td>
          <td style="text-align: right;"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-invoicingTransactionInstance?.total}" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.total}" /></g:else></td>
          <td style="text-align: center;"><g:formatDate date="${invoicingTransactionInstance?.paymentDate}" formatName="default.format.date" /></td>
          <td style="text-align: right;"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-(invoicingTransactionInstance?.paymentAmount ?: 0)}" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.paymentAmount}" /></g:else></td>
          <td style="text-align: right;" class="balance-state-${invoicingTransactionInstance?.balanceColor}"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-invoicingTransactionInstance?.balance}" displayZero="true" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.balance}" displayZero="true" /></g:else></td>
          <td style="text-align: center;" class="payment-state-${invoicingTransactionInstance?.paymentStateColor}">${fieldValue(bean: invoicingTransactionInstance, field: "stage")}</td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </section>
</body>
</html>
