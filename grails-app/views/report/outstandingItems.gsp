<%@ page import="grails.util.GrailsNameUtils" %>

<html>
  <head>
    <title><g:message code="report.outstandingItems.title"/></title>
    <meta name="stylesheet" content="report-outstanding-items"/>
    <meta name="caption"
      content="${message(code: 'report.outstandingItems.title')}"/>
    <meta name="noSubcaption" content="true"/>
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
        ><g:message code="report.outstandingItems.title"
      /></h1>
    </content>
    <content tag="toolbar">
      <button class="btn btn-default btn-print"
        ><i class="fa fa-print"></i>
        <g:message code="default.button.print.label"
      /></button>
    </content>

    <g:render template="/layouts/flashMessage"/>
    <g:form action="outstandingItems" method="get"
      class="form-inline organization-selector-form">
      <div class="form-group">
        <label for="organization" class="sr-only">
          <g:message code="report.outstandingItems.organization.label"/>
        </label>
        <select id="organization-select" name="organization.id"
          data-find-url="${createLink(
            controller: 'organization', action: 'find', params: [type: 1]
          )}">
          <g:if test="${organizationInstance}">
          <option value="${organizationInstance.id}"
            >${organizationInstance}</option
          >
          </g:if>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">
        <g:message code="report.outstandingItems.submit.label"/>
      </button>
    </g:form>
    <h2>
      <g:if test="${organizationInstance}">${organizationInstance}</g:if>
      <g:else><g:message code="report.outstandingItems.overview.label"/></g:else>
    </h2>

    <div class="detail-view">
      <g:if test="${organizationInstance}">
      <div class="table-responsive">
        <table
          class="table data-table price-table report report-outstanding-items">
          <thead>
            <tr>
              <th colspan="3"><g:message code="report.outstandingItems.invoiceDunning.label"/></th>
              <th colspan="2"><g:message code="report.outstandingItems.due.label"/></th>
              <th colspan="2"><g:message code="report.outstandingItems.payment.label"/></th>
              <th colspan="2"><g:message code="report.outstandingItems.balance.label"/></th>
            </tr>
            <tr>
              <g:sortableColumn property="docDate"
                params="['organization.id': organizationInstance.id]"
                title="${message(code: 'invoice.docDate.label.short')}"/>
              <g:sortableColumn property="number"
                params="['organization.id': organizationInstance.id]"
                title="${message(code: 'invoicingTransaction.number.label')}"/>
              <g:sortableColumn property="subject"
                params="['organization.id': organizationInstance.id]"
                title="${message(code: 'invoicingTransaction.subject.label')}"/>
              <g:sortableColumn property="dueDatePayment"
                params="['organization.id': organizationInstance.id]"
                title="${message(code: 'report.outstandingItems.date.label')}"/>
              <g:sortableColumn property="total"
                params="['organization.id': organizationInstance.id]"
                title="${message(code: 'report.outstandingItems.sum.label')}"/>
              <g:sortableColumn property="paymentDate"
                params="['organization.id': organizationInstance.id]"
                title="${message(code: 'report.outstandingItems.date.label')}"/>
              <g:sortableColumn property="paymentAmount"
                params="['organization.id': organizationInstance.id]"
                title="${message(code: 'report.outstandingItems.sum.label')}"/>
              <th><g:message code="report.outstandingItems.sum.label"/></th>
              <g:sortableColumn property="stage"
                params="['organization.id': organizationInstance.id]"
                title="${message(code: 'invoice.stage.label.short')}"/>
            </tr>
          </thead>
          <tfoot>
            <tr class="row-total">
              <td colspan="3">
                <g:message code="report.outstandingItems.total.label"/>
              </td>
              <td colspan="2" class="col-type-currency">
                <g:formatCurrency number="${total}" displayZero="true"
                  external="true"/>
              </td>
              <td colspan="2" class="col-type-currency">
                <g:formatCurrency number="${totalPaymentAmount}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-currency">
                <g:formatCurrency number="${totalPaymentAmount - total}"
                  displayZero="true" external="true"/>
              </td>
              <td></td>
            </tr>
          </tfoot>
          <tbody>
          <g:set var="group" value="I"/>
          <g:each var="invoicingTransaction" in="${invoicingTransactionList}"
            status="i">
            <tr class="row-${GrailsNameUtils.getScriptName(invoicingTransaction.class)}${invoicingTransaction.type != group ? ' row-divider' : ''}">
              <g:if test="${invoicingTransaction.type != group}"
                ><g:set var="group" value="${invoicingTransaction.type}"
              /></g:if>
              <td class="col-type-date outstanding-items-doc-date">
                <g:formatDate date="${invoicingTransaction?.docDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-id outstanding-items-number">
                <g:link
                  controller="${invoicingTransaction.type == 'I' ? 'invoice' : 'dunning'}"
                  action="show" id="${invoicingTransaction.id}">
                  <g:fullNumber bean="${invoicingTransaction}"/>
                </g:link>
              </td>
              <td class="col-type-string outstanding-items-subject">
                <g:link controller="${invoicingTransaction.type == 'I' ? 'invoice' : 'dunning'}"
                  action="show" id="${invoicingTransaction.id}">
                  ${invoicingTransaction.subject.replaceAll(~/_{2,}/, ' ')}
                </g:link>
              </td>
              <td class="col-type-date outstanding-items-due-date-payment">
                <g:formatDate date="${invoicingTransaction?.dueDatePayment}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency outstanding-items-payable">
                <g:formatCurrency number="${invoicingTransaction?.payable}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-date outstanding-items-payment-date">
                <g:formatDate date="${invoicingTransaction?.paymentDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency outstanding-items-payment-amount">
                <g:formatCurrency
                  number="${invoicingTransaction?.paymentAmount}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-currency outstanding-items-closing-balance
                  balance-state
                  balance-state-${invoicingTransaction?.balanceColor}">
                <g:formatCurrency
                  number="${invoicingTransaction?.closingBalance}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-status outstanding-items-stage payment-state
                  payment-state-${invoicingTransaction?.paymentStateColor}">
                <g:fieldValue bean="${invoicingTransaction}" field="stage"/>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      </g:if>
      <g:else>
        <div class="well well-lg empty-list">
          <p><g:message code="report.outstandingItems.list.empty"/></p>
        </div>
      </g:else>
    </div>
  </body>
</html>
