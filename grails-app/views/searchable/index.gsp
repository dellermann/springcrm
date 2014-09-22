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
      parseException="${parseException}" sort="${params.sort}" />

    <div id="search-queries">
      <h2><g:message code="searchable.extendedSearch.label" /></h2>
      <div>
        <g:form controller="searchable" action="index">
          <div class="row">
            <div class="label">
              <label for="search-query"><g:message code="searchable.term.label" /></label>
            </div>
            <div class="field">
              <g:textField name="q" id="search-query" value="${params.q}"
                size="100" />
            </div>
          </div>
          <div class="row">
            <div class="label">
              <label for="search-operator"><g:message code="searchable.mode.label" /></label>
            </div>
            <div class="field">
              <g:select name="defaultOperator" id="search-operator"
                from="['and', 'or']" value="${params.defaultOperator}"
                valueMessagePrefix="searchable.mode" />
            </div>
          </div>
          <div class="row">
            <div class="label">
              <label for="search-order"><g:message code="searchable.sort.label" /></label>
            </div>
            <div class="field">
              <g:select name="sort" id="search-order"
                from="['alias', 'SCORE']" value="${params.sort}"
                valueMessagePrefix="searchable.sort" />
            </div>
          </div>
          <div class="buttons">
            <g:submitButton name="search"
              value="${message(code: 'default.search.button.label')}"
              class="button green" />
          </div>
        </g:form>
      </div>
    </div>
  </div>
  <content tag="scripts">
    <asset:javascript src="search" />
  </content>
</body>
</html>
