<%@ page import="grails.util.GrailsNameUtils" %>

<ul>
  <g:each var="item" in="${bean."${property}"}">
  <li
    ><g:link controller="${GrailsNameUtils.getPropertyName(item.class)}"
      action="show" id="${item.id}"
      >${item}</g:link
    ></li
  >
  </g:each>
</ul>
