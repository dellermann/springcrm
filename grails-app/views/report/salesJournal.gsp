<%@ page import="org.amcworld.springcrm.Quote" %>
<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="report.salesJournal.title" /></title>
  <meta name="stylesheet" content="report-sales-journal" />
</head>

<body>
  <header>
    <h1><g:message code="report.salesJournal.title" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button elementId="print-btn" color="white" icon="print"
          message="default.button.print.label" /></li>
      </ul>
    </nav>
  </header>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <div id="filter-bar" class="filter-bar"
      data-load-url="${createLink(action: 'salesJournal')}">
      <g:set var="monthNames"
        value="${java.text.DateFormatSymbols.instance.months}" />
      <g:set var="shortMonthNames"
        value="${java.text.DateFormatSymbols.instance.shortMonths}" />
      <ul id="month-selector" class="letter-bar">
        <g:each var="i" in="${(1..12)}">
        <li class="available ${(currentMonth == i) ? 'current' : ''}"
          data-month="${i}"
          ><g:link action="salesJournal">${shortMonthNames[i - 1]}</g:link></li>
        </g:each>
        <li class="available ${currentMonth == 0 ? 'current' : ''}"
          data-month="0"
          ><g:link action="salesJournal"><g:message code="report.salesJournal.wholeYear.label" default="Year {0}" args="[currentYear]" /></g:link></li>
      </ul>
      <g:select name="year-selector" from="${(yearStart..yearEnd)}"
        value="${currentYear}" />
    </div>
    <h2><g:if test="${currentMonth}">${monthNames[currentMonth - 1]} ${currentYear}</g:if><g:else><g:message code="report.salesJournal.yearlyOverview.label" default="Overview of year {0}" args="${[currentYear]}" /></g:else></h2>
    <g:if test="${invoicingTransactionInstanceList}">
    <table class="content-table report">
      <thead>
        <tr>
          <th scope="col" colspan="4"><g:message code="report.salesJournal.invoice.label" default="Invoice" /></th>
          <th scope="col" colspan="2"><g:message code="report.salesJournal.due.label" default="Due" /></th>
          <th scope="col" colspan="2"><g:message code="report.salesJournal.payment.label" default="Payment" /></th>
          <th scope="col" colspan="2"><g:message code="report.salesJournal.balance.label" default="Balance" /></th>
        </tr>
        <tr>
          <g:sortableColumn scope="col" property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn scope="col" property="dueDatePayment" title="${message(code: 'report.salesJournal.date.label', default: 'Date')}" />
          <g:sortableColumn scope="col" property="total" title="${message(code: 'report.salesJournal.sum.label', default: 'Sum')}" style="width: 6em;" />
          <g:sortableColumn scope="col" property="paymentDate" title="${message(code: 'report.salesJournal.date.label', default: 'Date')}" />
          <g:sortableColumn scope="col" property="paymentAmount" title="${message(code: 'report.salesJournal.sum.label', default: 'Sum')}" style="width: 6em;" />
          <th scope="col"><g:message code="report.salesJournal.sum.label" default="Sum" /></th>
          <g:sortableColumn scope="col" property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" />
        </tr>
      </thead>
      <g:if test="${invoicingTransactionInstanceList}">
      <tfoot>
        <tr>
          <td colspan="4" class="align-right"><g:message code="report.salesJournal.total.label" default="Total" /></td>
          <td colspan="2" class="currency"><g:formatCurrency number="${total}" displayZero="true" external="true" /></td>
          <td colspan="2" class="currency"><g:formatCurrency number="${totalPaymentAmount}" displayZero="true" external="true" /></td>
          <td class="currency"><g:formatCurrency number="${totalPaymentAmount - total}" displayZero="true" external="true" /></td>
          <td></td>
        </tr>
      </tfoot>
      </g:if>
      <tbody>
      <g:set var="group" value="I" />
      <g:each in="${invoicingTransactionInstanceList}" status="i" var="invoicingTransactionInstance">
        <g:if test="${invoicingTransactionInstance.type != group}"><g:set var="group" value="${invoicingTransactionInstance.type}" /><tr class="divider"></g:if><g:else><tr></g:else>
          <td class="date sales-journal-doc-date" style="text-align: center;"><g:formatDate date="${invoicingTransactionInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="id sales-journal-number"><g:link controller="${invoicingTransactionInstance.type == 'I' ? 'invoice' : (invoicingTransactionInstance.type == 'D' ? 'dunning' : 'creditMemo')}" action="show" id="${invoicingTransactionInstance.id}"><g:fieldValue bean="${invoicingTransactionInstance}" field="fullNumber" /></g:link></td>
          <td class="ref sales-journal-organization"><g:link controller="organization" action="show" id="${invoicingTransactionInstance.organization?.id}"><g:fieldValue bean="${invoicingTransactionInstance}" field="organization" /></g:link></td>
          <td class="string sales-journal-subject"><g:link controller="${invoicingTransactionInstance.type == 'I' ? 'invoice' : (invoicingTransactionInstance.type == 'D' ? 'dunning' : 'creditMemo')}" action="show" id="${invoicingTransactionInstance.id}">${invoicingTransactionInstance.subject.replaceAll(~/_{2,}/, ' ')}</g:link></td>
          <td class="date sales-journal-due-date-payment"><g:if test="${invoicingTransactionInstance.type == 'C'}">—</g:if><g:else><g:formatDate date="${invoicingTransactionInstance?.dueDatePayment}" formatName="default.format.date" /></g:else></td>
          <td class="currency sales-journal-total"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-invoicingTransactionInstance?.total}" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.total}" displayZero="true" external="true" /></g:else></td>
          <td class="date sales-journal-payment-date"><g:formatDate date="${invoicingTransactionInstance?.paymentDate}" formatName="default.format.date" /></td>
          <td class="currency sales-journal-payment-amount"><g:if test="${invoicingTransactionInstance.type == 'C'}"><g:formatCurrency number="${-(invoicingTransactionInstance?.paymentAmount ?: 0)}" displayZero="true" external="true" /></g:if><g:else><g:formatCurrency number="${invoicingTransactionInstance?.paymentAmount}" displayZero="true" external="true" /></g:else></td>
          <td class="currency sales-journal-sum balance-state balance-state-${invoicingTransactionInstance?.balanceColor}"><g:formatCurrency number="${invoicingTransactionInstance?.balance}" displayZero="true" external="true" /></td>
          <td class="status sales-journal-stage payment-state payment-state-${invoicingTransactionInstance?.paymentStateColor}"><g:fieldValue bean="${invoicingTransactionInstance}" field="stage" /></td>
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
  <content tag="scripts">
    <asset:javascript src="report-sales-journal" />
  </content>
</body>
</html>
