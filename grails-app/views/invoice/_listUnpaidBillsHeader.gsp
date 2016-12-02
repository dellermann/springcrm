<%@ page import="java.math.BigDecimal" %>
<%@ page import="org.amcworld.springcrm.ConfigHolder" %>

<div id="unpaid-bills-settings-dialog" class="modal fade" tabindex="-1"
  role="dialog" aria-labelledby="unpaid-bills-settings-dialog-title">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
          aria-label="${message(code: 'default.btn.close')}"
        ><span aria-hidden="true">×</span
        ></button>
        <h4 id="unpaid-bills-settings-dialog-title"
          class="modal-title"
        ><g:message code="invoice.unpaidBills.settings.title"
        /></h4>
      </div>
      <div class="modal-body">
        <form id="unpaid-bills-settings-form"
          action="${createLink(action: 'settingsUnpaidBillsSave')}"
          method="post">
          <div class="form-group">
            <label for="unpaid-bills-settings-minimum"
            ><g:message code="invoice.unpaidBills.settings.minimum.label"
            /></label>
            <div class="input-group">
              <g:set var="minDefault" value="${
                BigDecimal.ONE.movePointLeft(
                  ConfigHolder.instance['numFractionDigitsExt']?.toType(Integer) ?: 2i
                )
              }"/>
              <g:set var="min" value="${user.settings.unpaidBillsMinimum}"/>
              <input type="text" id="unpaid-bills-settings-minimum"
                name="minimum"
                class="form-control form-control-number form-control-currency"
                value="${formatCurrency(
                  number: min ? new BigDecimal(min) : minDefault,
                  displayZero: true, numberOnly: true
                )}"
                aria-describedby="unpaid-bills-settings-minimum-currency"/>
              <span id="unpaid-bills-settings-minimum-currency"
                class="input-group-addon"><g:currency/></span>
            </div>
          </div>
          <div class="form-group">
            <label for="unpaid-bills-settings-sort"
            ><g:message code="invoice.unpaidBills.settings.sort.label"
            /></label>
            <g:select name="sort" from="[
              'dueDatePayment', 'docDate', 'stillUnpaid', 'paymentAmount',
              'total', 'number'
            ]"
              id="unpaid-bills-settings-sort" class="form-control"
              value="${user.settings.unpaidBillsSort ?: 'dueDatePayment'}"
              valueMessagePrefix="invoice.unpaidBills.settings.sort"/>
          </div>
          <div class="form-group">
            <label for="unpaid-bills-settings-order"
            ><g:message code="invoice.unpaidBills.settings.order.label"
            /></label>
            <g:select name="order" from="['asc', 'desc']"
              id="unpaid-bills-settings-order" class="form-control"
              value="${user.settings.unpaidBillsOrder ?: 'desc'}"
              valueMessagePrefix="invoice.unpaidBills.settings.order"/>
          </div>
          <div class="form-group">
            <label for="unpaid-bills-settings-max"
            ><g:message code="invoice.unpaidBills.settings.max.label"
            /></label>
            <g:select name="max" from="[0, 5, 10, 15, 20, 30, 40, 50]"
              id="unpaid-bills-settings-max" class="form-control"
              value="${user.settings.unpaidBillsMax}"
              valueMessagePrefix="invoice.unpaidBills.settings.max"/>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">
          <i class="fa fa-close"></i>
          <g:message code="default.btn.close"/>
        </button>
        <button type="submit" form="unpaid-bills-settings-form"
          class="btn btn-success">
          <i class="fa fa-save"></i>
          <g:message code="default.button.save.label"/>
        </button>
      </div>
    </div>
  </div>
</div>

<div class="buttons" role="toolbar"
  aria-label="${message(code: 'invoice.unpaidBills.additionalToolbar.label')}">
  <g:link controller="invoice" action="index"
    title="${message(code: 'invoice.unpaidBills.btn.invoices')}">
    <i class="fa fa-list"></i>
    <span class="sr-only"
      ><g:message code="invoice.unpaidBills.btn.invoices"
    /></span>
  </g:link
  ><button type="button"
    title="${message(code: 'invoice.unpaidBills.btn.settings')}"
    data-toggle="modal" data-target="#unpaid-bills-settings-dialog">
    <i class="fa fa-cog"></i>
    <span class="sr-only"
      ><g:message code="invoice.unpaidBills.btn.settings"
    /></span>
  </button>
</div>

