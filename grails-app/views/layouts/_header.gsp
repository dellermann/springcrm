<aside id="login-area">
  <p><g:loginControl/></p>
  <div id="search-area">
    <g:form controller="searchable" action="index" id="searchableForm" name="searchableForm" method="get">
      <g:textField name="q" value="${message(code:'default.search.label', default:'Search...')}" id="search" class="search-field"/>
      <a href="#"><img src="${resource(dir:'img', file:'search.png')}" alt="${message(code:'default.search.button.label', default:'Search')}" title="${message(code:'default.search.button.label', default:'Search')}" width="16" height="16" /></a>
    </g:form>
  </div>
</aside>
<header>
  <h1 id="logo"><a href="${createLink(uri: '/')}"><strong>SpringCRM</strong></a></h1>
</header>
