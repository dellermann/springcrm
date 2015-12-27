<ul>
  <g:each in="${bean."${property}"}" var="u">
  <li
    ><g:link controller="user" action="show" id="${u.id}"
      >${u?.encodeAsHTML()}</g:link
    ></li
  >
  </g:each>
</ul>
