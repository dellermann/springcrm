<div class="btn-group btn-group-justified letter-bar" role="group"
  aria-label="${message(code: 'default.letterBar.label')}">
  <g:set var="n" value="${availableLetters.size()}"/>
  <g:each var="i" in="${(0..<n).step(numLetters)}">
    <g:set var="sublist"
      value="${availableLetters[i..Math.min(i + numLetters, n) - 1]}"/>
    <g:set var="inList" value="${!sublist.toList().intersect(letters).empty}"/>
    <a href="${
        inList \
          ? createLink(
            controller: controller, action: action,
            params: linkParams + [letter: availableLetters[i], max: max])
          : '#'
      }" class="btn btn-default${inList ? '' : ' disabled'}" role="button"
      aria-disabled="${!inList}">
      <g:if test="${separator && numLetters > 2}">
        ${sublist[0]}${separator}${sublist[-1]}
      </g:if>
      <g:else>
        ${sublist}
      </g:else>
    </a>
  </g:each>
</div>
