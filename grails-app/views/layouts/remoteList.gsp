<section class="remote-list"
  data-load-url="${createLink(controller: controller, action: 'listEmbedded')}"
  data-load-params="${loadParams}" data-return-url="${url()}"
  role="article">
  <header>
    <h3><g:message code="${controller}.plural" /></h3>
    <g:unless test="${noCreateBtn}">
    <div class="buttons">
      <g:button controller="${controller}" action="create"
        params="${createParams}" color="success" icon="plus-circle"
        message="default.create.label"
        args="[message(code: controller + '.label')]" />
    </div>
    </g:unless>
  </header>
  <div aria-busy="true"></div>
</section>
