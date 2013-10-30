<g:set var="address" value="${bean[property]}" />
<f:display bean="${address}" property="street" />
<f:display bean="${address}" property="poBox" />
<f:display bean="${address}" property="postalCode" />
<f:display bean="${address}" property="location" />
<f:display bean="${address}" property="state" />
<f:display bean="${address}" property="country" />
<g:if test="${address.toString()}">
<div class="row">
  <div class="label empty-label"></div>
  <div class="field">
    <g:button url="http://maps.google.de/maps?hl=&q=${address.toString().encodeAsURL()}"
      target="_blank" color="blue" size="medium" icon="map-marker"
      message="default.link.viewInGoogleMaps" />
  </div>
</div>
</g:if>

