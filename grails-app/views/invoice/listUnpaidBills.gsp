<ul class="list-group" role="list">
  <g:each var="invoice" in="${invoiceList}">
    <g:if test="${invoice.paymentStateColor == 'red'}">
      <g:set var="dueColorClass" value=" due-danger"/>
    </g:if>
    <g:elseif test="${invoice.paymentStateColor == 'yellow'}">
      <g:set var="dueColorClass" value=" due-warning"/>
    </g:elseif>
    <li class="list-group-item${dueColorClass}" role="listitem"
      aria-labelledby="unpaid-bill-${invoice.id}-title">
      <g:link controller="invoice" action="show" id="${invoice.id}"
        elementId="unpaid-bill-${invoice.id}-title">
        <g:fullNumber bean="${invoice}"/>
        ${invoice.subject.replaceAll(~/_{2,}/, ' ')}
      </g:link>
      <div class="text">
        <g:message code="invoice.for.label"/> ${invoice.organization.name}<br/>
        <g:message code="invoice.due.label"/>:
        <strong><time><g:formatDate date="${invoice.dueDatePayment}"
          type="date"/></time></strong>
        <br/>
        <g:message code="invoice.amount.label"/>:
        <strong><g:formatCurrency number="${invoice.total}"
          displayZero="true" external="true"/></strong>
        <g:if test="${invoice.paymentAmount}">
          <br/>
          <g:message code="invoice.stillUnpaid.label"/>:
          <strong><g:formatCurrency number="${-invoice.balance}"
            external="true" displayZero="true"/></strong>
        </g:if>
      </div>
      <div class="buttons">
        <g:link controller="invoice" action="edit" id="${invoice.id}"
          params="[
            returnUrl: createLink(controller: 'overview', action: 'index')
          ]"
          title="${message(code: 'default.btn.edit')}" role="button"
          ><i class="fa fa-pencil-square-o"></i
          ><span class="sr-only"><g:message code="default.btn.edit"/></span
        ></g:link>
      </div>
    </li>
  </g:each>
</ul>
