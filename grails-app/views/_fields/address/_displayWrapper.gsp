<g:set var="address" value="${bean[property].toString()}"/>
<div class="column">
  <g:unless test="${suppressHeader}">
  <header>
    <h3>${title}</h3>
    <div class="buttons">
      <g:if test="${address}">
      <g:button url="http://maps.google.de/maps?hl=&q=${address.encodeAsURL()}"
        target="_blank" color="primary" size="xs" icon="map-marker"
        message="default.button.viewOnMap"/>
      </g:if>
    </div>
  </header>
  </g:unless>
  <div class="column-content">
    <f:displayWidget bean="${bean}" property="${property}"/>
  </div>
</div>
