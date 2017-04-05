<g:select name="${property}" id="${id ?: property}-select"
  from="${type.list(sort: 'orderId')}" optionKey="id"
  value="${bean?."${property}"?.id}"
  noSelection="${required ? null : ['': '']}" />
