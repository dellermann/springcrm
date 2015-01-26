<g:select name="${property}.id" id="${id ?: property}-select"
  from="${type.list()}" optionKey="id" value="${bean?."${property}"?.id}"
  noSelection="${required ? null : ['null': '']}" />