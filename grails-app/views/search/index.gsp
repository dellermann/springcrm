<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<html>
  <head>
    <meta name="layout" content="main"/>
    <meta name="caption" content="${message(code: 'search.title')}"/>
    <title><g:message code="search.title"/></title>
    <meta name="stylesheet" content="search-results"/>
  </head>

  <body>
    <p><g:message code="search.numHits"
      args="[numHits, StringEscapeUtils.escapeXml10(query)]"/></p>
    <g:each in="${searchResults.entrySet()}" var="group">
      <g:ifControllerAllowed controllers="${group.key}">
        <section class="search-result-group">
          <h2>
            <g:dataTypeIcon controller="${group.key}"/>
            <g:message code="${group.key}.plural"/>
            <span class="badge"><g:formatNumber number="${group.value.size()}" groupingUsed="true" maxFractionDigits="0"/></span>
          </h2>
          <g:each in="${group.value}" var="item">
            <h3>
              <g:link controller="${item.type}" action="show"
                id="${item.recordId}"
                >${item.recordTitle}</g:link
              >
              <g:link controller="${item.type}" action="edit"
                id="${item.recordId}" params="[returnUrl: url()]"
                class="btn btn-default btn-xs edit-btn">
                <i class="fa fa-pencil-square-o"></i
                ><span class="hidden-xs"
                  ><g:message code="default.btn.edit"
                /></span>
              </g:link>
            </h3>
            <div class="search-result-fields">
              <g:each in="${item.structuredContent.values().findAll {StringUtils.containsIgnoreCase(it, query)}.toUnique().take(3)}"
                var="field">
                <p><g:searchResult text="${field}" query="${query}"/></p>
              </g:each>
            </div>
          </g:each>
        </section>
      </g:ifControllerAllowed>
    </g:each>
    <div class="row">
      <nav class="col-xs-12 col-md-9 pagination-container">
        <div class="visible-xs">
          <g:paginate total="${numHits}" params="[query: query]" maxsteps="3"
            class="pagination-sm"/>
        </div>
        <div class="hidden-xs">
          <g:paginate total="${numHits}" params="[query: query]"/>
        </div>
      </nav>
      <g:render template="/layouts/numItemsPerPage"/>
    </div>
  </body>
</html>
