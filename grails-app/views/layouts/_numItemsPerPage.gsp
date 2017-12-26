<div class="hidden-xs hidden-sm col-md-3">
  <form class="form-inline num-items-per-page-form">
    <div class="form-group">
      <label for="num-items-per-page"
        ><g:message code="default.list.numItemsPerPage"
      /></label>
      <g:select id="num-items-per-page" name="max" class="form-control"
        style="width: 6em;" from="[10, 20, 30, 40, 50, 100]"
        value="${params.max ?: 10}"/>
    </div>
  </form>
</div>
