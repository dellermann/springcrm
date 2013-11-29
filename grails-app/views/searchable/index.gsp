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
    <g:searchResults query="${params.q}" searchResult="${searchResult}"
      parseException="${parseException}" />
  </div>
</body>
</html>
