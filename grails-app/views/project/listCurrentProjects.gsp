<%@ page import="org.amcworld.springcrm.ProjectStatus" %>

<ul>
  <g:each in="${projectInstanceList}" var="projectInstance">
  <li>
    <div class="text">
      <g:link controller="project" action="show"
        id="${projectInstance.id}">${projectInstance.title}</g:link><br/>
      ${projectInstance.organization.name}<br/>
      <g:message code="project.status.label"/>:
      <strong>${projectInstance.status}</strong>
    </div>
    <div class="project-status-buttons"
      ><g:each in="['play', 'pause', 'calendar-o', 'clock-o']" var="icon"
        status="i"
        ><g:link controller="project" action="setStatus"
          id="${projectInstance.id}" params="[status: 2600 + i]"
          class="project-status-${2600 + i}${projectInstance.status.id == 2600 + i ? ' active' : ''}"
          title="${ProjectStatus.read(2600 + i)}" role="button">
          <i class="fa fa-${icon}"></i>
          <span class="sr-only">${ProjectStatus.read(2600 + i)}</span>
        </g:link
      ></g:each
    ></div>
  </li>
  </g:each>
</ul>
