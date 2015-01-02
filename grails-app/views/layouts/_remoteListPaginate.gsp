<nav class="text-center">
  <div class="visible-xs">
    <g:paginate total="${total}" params="${linkParams}" maxsteps="3"
      class="pagination-sm" />
  </div>
  <div class="hidden-xs">
    <g:paginate total="${total}" params="${linkParams}" />
  </div>
</nav>
