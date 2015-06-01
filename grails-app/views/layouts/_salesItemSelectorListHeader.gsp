<nav class="row">
  <div class="col-xs-12 col-sm-8 col-md-9">
    <div class="visible-xs visible-sm">
      <g:letterBar clazz="${type}" property="name" numLetters="5"
        separator="-" where="name like '%${params.search ?: ''}%'" />
    </div>
    <div class="hidden-xs hidden-sm">
      <g:letterBar clazz="${type}" property="name" numLetters="3"
        separator="-" where="name like '%${params.search ?: ''}%'" />
    </div>
  </div>
  <div class="col-xs-12 col-sm-4 col-md-3 text-right">
    <%--
      XXX leave the </form> tag alone because it prevents the following
      <form> tag from being stripped by the jQuery $.load function.  It's not
      a problem of the load function rather than the called innerHTML function
      which strips form tags in most of the browsers.
    --%>
    </form>
    <g:form action="selectorList" class="search-form">
      <div class="input-group">
        <g:textField type="search" name="search" class="form-control"
          value="${params.search}"
          placeholder="${message(code: 'default.search.label')}" />
        <span class="input-group-btn">
          <button type="submit" class="btn btn-default search-btn"
            title="${message(code: 'default.search.button.label')}"
            ><i class="fa fa-search"></i
            ><span class="sr-only"
              ><g:message code="default.search.button.label"
            /></span
          ></button>
        </span>
      </div>
    </g:form>
  </div>
</nav>
