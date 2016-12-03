<html>
  <head>
    <meta name="layout" content="main"/>
    <title><g:message code="report.turnoverOverview.title"/></title>
    <meta name="stylesheet" content="report-turnover"/>
    <meta name="caption"
      content="${message(code: 'report.turnoverOverview.title')}"/>
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
        ><g:message code="report.turnoverOverview.title"
      /></h1>
    </content>
    <content tag="toolbar">
      <button class="btn btn-default btn-print"
        ><i class="fa fa-print"></i>
        <g:message code="default.button.print.label"
      /></button>
    </content>

    <g:render template="/layouts/flashMessage"/>
    <g:form action="turnoverOverview" method="get" class="form-inline">
      <div class="form-group">
        <label for="year"
          ><g:message code="report.turnoverOverview.year.label"
        /></label>
        <g:select from="${yearEnd..yearStart}" id="year" name="year"
          value="${year}" class="form-control"
          noSelection="[0: message(code: 'report.turnoverOverview.year.overview')]"/>
      </div>
      <div class="form-group">
        <label for="order"
          ><g:message code="report.turnoverOverview.order.label"
        /></label>
        <g:select from="['asc', 'desc']" id="order" name="order"
          value="${order}" class="form-control"
          valueMessagePrefix="report.turnoverOverview.order"/>
      </div>
      <button type="submit" class="btn btn-primary">
        <g:message code="report.turnoverOverview.btn.submit"/>
      </button>
    </g:form>
    <h2>
      <g:if test="${year}">${year}</g:if>
      <g:else><g:message code="report.turnover.completeOverview.label"/></g:else>
    </h2>

    <div class="detail-view">
      <g:if test="${turnoverList}">
      <div class="table-responsive">
        <table class="table data-table price-table report report-turnover">
          <thead>
            <tr>
              <th>
                <g:message code="invoicingTransaction.organization.label"/>
              </th>
              <th><g:message code="report.turnover.turnover.total"/></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${turnoverList}" var="entry">
          <tr>
            <td class="col-type-ref turnover-overview-organization">
              ${entry.key.name}
            </td>
            <td class="col-type-currency turnover-overview-turnover">
              <g:formatCurrency number="${entry.value}" displayZero="true"
                external="true"/>
            </td>
          </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      </g:if>
      <g:else>
      <div class="well well-lg empty-list">
        <p><g:message code="report.turnover.list.empty"/></p>
      </div>
      </g:else>
    </div>
  </body>
</html>
