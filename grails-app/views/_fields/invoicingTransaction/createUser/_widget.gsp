<%@ page import="org.amcworld.springcrm.User" %>

<g:select id="${property}-select" name="${property}.id" value="${value?.id}"
  from="${User.list()}" optionKey="id" noSelection="${['': '']}" />
