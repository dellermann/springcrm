<%@ page import="grails.util.GrailsNameUtils" %>

<g:select name="${property}" from="${type.values()}" value="${value}" size="1"
  valueMessagePrefix="${GrailsNameUtils.getPropertyName(bean.class)}.${property}"
  noSelection="${required ? null : ['': '']}"
  class="form-control" />