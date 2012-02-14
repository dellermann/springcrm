<g:if test="${constraints.range}">
  <g:field type="range" name="${property}" value="${value}" min="${constraints.range.from}" max="${constraints.range.to}" size="${size ?: 10}" />
</g:if>
<g:else>
  <%--
  Because <input type="number" /> fields do not accept localized number values
  in HTML 5 we will suppress the default rendering of the fields plugin and
  render type "text" instead.
  --%>
  <g:field type="text" name="${property}" value="${value}" size="${size ?: 10}" />
</g:else>