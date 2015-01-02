<g:set var="address" value="${bean[property]}" />
<div class="column">
  <header>
    <h3>${title}</h3>
    <div class="buttons">
      <g:if test="${address.toString()}">
      <g:button
        url="http://maps.google.de/maps?hl=&q=${address.toString().encodeAsURL()}"
        target="_blank" color="primary" size="xs" icon="map-marker"
        message="default.button.viewOnMap" />
      </g:if>
    </div>
  </header>
  <div class="column-content">
    <f:display bean="${address}" property="street" />
    <f:display bean="${address}" property="poBox" />
    <f:display bean="${address}" property="postalCode" />
    <f:display bean="${address}" property="location" />
    <f:display bean="${address}" property="state" />
    <f:display bean="${address}" property="country" />
  </div>
</div>
