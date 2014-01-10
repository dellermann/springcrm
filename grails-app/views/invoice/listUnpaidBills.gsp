<ol class="simple-ordered-list">
<g:each in="${invoiceInstanceList}" var="invoiceInstance">
  <li>
    <g:link controller="invoice" action="show" id="${invoiceInstance.id}"><g:fieldValue bean="${invoiceInstance}" field="fullNumber" /> ${invoiceInstance.subject.replaceAll(~/_{2,}/, ' ')}</g:link>
    <span class="item-actions">
      <g:link controller="invoice" action="edit" id="${invoiceInstance.id}" params="[returnUrl: createLink(uri: '/')]" class="bubbling-icon"><i class="fa fa-pencil-square-o"></i></g:link>
    </span>
    <div class="indent">
      <g:message code="invoice.for.label" />
      ${invoiceInstance.organization.name}<br />
      <g:message code="invoice.due.label" />:
      <strong><g:formatDate date="${invoiceInstance.dueDatePayment}"
        type="date" /></strong><br />
      <g:message code="invoice.amount.label" />:
      <strong><g:formatCurrency number="${invoiceInstance.total}"
        displayZero="true" external="true"
        /><g:if test="${invoiceInstance.paymentAmount}">,
        <g:message code="invoice.stillUnpaid.label" default="still unpaid" />:
        <g:formatCurrency number="${-invoiceInstance.balance}"
          external="true" displayZero="true"
        /></g:if></strong>
    </div>
  </li>
</g:each>
</ol>
