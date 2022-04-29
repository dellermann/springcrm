<%--
  Because <input type="number"/> fields do not accept localized number values
  in HTML 5 we will suppress the default rendering of the fields plugin and
  render type "text" instead.
--%>
<input type="text" id="${property}" name="${property}" value="${value}"
  class="form-control form-control-number"
  size="${size ?: 10}" ${required ? raw('required="required"') : ''}
  ${constraints.nullable ? raw('data-allow-null="true"') : ''}/>
