<%@ page import="org.springframework.util.ClassUtils" %>
<%@ page import="grails.plugin.searchable.internal.lucene.LuceneUtils" %>
<%@ page import="grails.plugin.searchable.internal.util.StringQueryUtils" %>
<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="searchable.results.label" default="Search results" /></title>
</head>

<body>
  <header>
    <h1><g:message code="searchable.results.label" default="Search results" /></h1>
  </header>
  <div id="content">
    <g:set var="haveQuery" value="${params.q?.trim()}" />
    <g:set var="haveResults" value="${searchResult?.results}" />
    <g:if test="${haveQuery && haveResults}">
    <p class="searchresults-number"><g:message code="searchable.results.number" args="[params.q, searchResult.total, searchResult.offset + 1, searchResult.results.size() + searchResult.offset]" /></p>
    </g:if>

    <g:if test="${haveQuery && !haveResults && !parseException}">
    <p class="searchresults-number searchresults-not-found"><g:message code="searchable.results.notFound" args="[params.q]" /></p>
    </g:if>

    <g:if test="${parseException}">
    <p class="searchresults-error"><g:message code="searchable.results.error" args="[params.q]" /></p>
    </g:if>

    <g:if test="${haveResults}">
    <dl class="searchresults-results">
      <g:each var="result" in="${searchResult.results}" status="index">
      <g:set var="className" value="${ClassUtils.getShortName(result.getClass())}" />
      <g:set var="link" value="${createLink(controller: className[0].toLowerCase() + className[1..-1], action: 'show', id: result.id)}" />
      <dt><a href="${link}">${result.toString().encodeAsHTML()}</a></dt>
      <dd>
        <div><g:message code="searchable.results.type.label" default="Type" />:
        <g:message code="${className[0].toLowerCase() + className[1..-1]}.label" /></div>
      </dd>
      </g:each>
    </dl>
    
    <div class="paginator">
      <g:paginate total="${searchResult.total}" action="index"
        params="[q: params.q]" />
    </div>
    </g:if>
  </div>
</body>
</html>
