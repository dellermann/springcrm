<header>
  <a id="logo" href="${createLink(uri: '/')}"><strong>SpringCRM</strong></a>
  <div id="top-area">
    <p><g:loginControl /></p>
    <form action="${createLink(controller: 'searchable', action: 'index')}"
      id="search-area">
      <span title="${message(code: 'default.search.button.label', default: 'Search')}"><i class="fa fa-search"></i></span>
      <g:textField id="search" name="q" value="${params.q}"
        placeholder="${message(code: 'default.search.label')}" />
      <g:hiddenField name="defaultOperator" value="${params.defaultOperator ?: 'and'}" />
      <g:hiddenField name="sort" value="${params.sort ?: 'alias'}" />
    </form>
    <p><g:link controller="searchable" action="index"><g:message code="searchable.extendedSearch.label" /></g:link></p>
  </div>
</header>

