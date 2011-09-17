<%@ page import="org.amcworld.springcrm.Modules" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title>SpringCRM</title>
  <style>
  #content:after {
    clear: both;
    content: ".";
    display: block;
    height: 0;
    visibility: hidden;
  }
  .left-col {
    float: left;
    width: 50%;
  }
  .right-col {
    float: right;
    width: 50%;
  }
  </style>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="overview.title" /></h2>
  </div>
  <section id="content">
    <div class="left-col">
      <p>Willkommen bei SpringCRM. Dieses Projekt befindet sich im Aufbau.</p>
      <ul style="margin-left: 0;">
      <g:each var="c" in="${grailsApplication.controllerClasses.logicalPropertyName.sort() - ['searchable', 'notification']}">
        <g:ifControllerAllowed controllers="${c}">
        <li style="list-style: none;">
          <g:link controller="${c}" class="button white" style="width: 25em;"><g:message code="${c}.plural" default="${c}"/></g:link>
        </li>
        </g:ifControllerAllowed>
      </g:each>
      </ul>
    </div>
    <%-- The LRU list will be replaced by the content areas on the home page. --%>
    <div class="right-col">
      <h3>Zuletzt verwendet</h3>
      <ol>
      <g:each var="lruEntry" in="${lruList}">
        <li><g:link controller="${lruEntry.controller}" action="show" id="${lruEntry.itemId}">${lruEntry.name}</g:link> (<g:message code="${lruEntry.controller}.label" default="${lruEntry.controller}"/>)</li>
      </g:each>
      </ol>
    </div>
  </section>
</body>
</html>
