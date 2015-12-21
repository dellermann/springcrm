<g:pageProperty name="page.selectorListHeader" />

<g:if test="${list}">
<div class="table-responsive">
  <g:layoutBody />
</div>
<nav class="text-center">
  <div class="visible-xs">
    <g:paginate total="${total}" maxsteps="3" class="pagination-sm" />
  </div>
  <div class="hidden-xs">
    <g:paginate total="${total}" />
  </div>
</nav>
</g:if>
<g:else>
<div class="well empty-list">
  <p><g:message code="default.list.empty" /></p>
</div>
</g:else>
