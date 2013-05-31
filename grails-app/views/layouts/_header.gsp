<aside id="login-area">
  <p><g:loginControl /></p>
  <div id="search-area">
    <g:form controller="searchable" action="index" id="searchableForm" name="searchableForm" method="get">
      <g:textField id="search" name="q" placeholder="${message(code: 'default.search.label')}" />
      <img src="${resource(dir: 'img', file: 'search.png')}"
        alt="${message(code: 'default.search.button.label', default: 'Search')}"
        title="${message(code: 'default.search.button.label', default: 'Search')}"
        width="14" height="14" />
    </g:form>
  </div>
</aside>
<header>
  <h1 id="logo"><a href="${createLink(uri: '/')}"><strong>SpringCRM</strong></a></h1>
</header>
