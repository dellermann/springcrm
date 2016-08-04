<input type="text" name="${property}" value="${value}" class="${cssClass}"
  size="${size ?: 10}" ${required ? raw('required="required"') : ''}
  ${constraints.nullable ? raw('data-allow-null="true"') : ''}/>
