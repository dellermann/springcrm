<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="about.sysInfo" default="About" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="about.title" default="About" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="index" class="white"><g:message code="about.back.label" default="Back" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <h3><g:message code="about.sysInfo.application" /></h3>
    <p><g:message code="about.sysInfo.version" args="${[meta(name: 'app.version'), meta(name: 'app.buildNumber'), meta(name: 'app.buildDate'), meta(name: 'app.buildProfile')]}" /></p>
    <p><g:message code="about.sysInfo.grailsVersion" args="${[meta(name: 'app.grails.version')]}" /></p>

    <h3><g:message code="about.sysInfo.env" /></h3>
    <p><g:message code="about.sysInfo.java" args="${[System.getProperty('java.version'), System.getProperty('java.vendor'), System.getProperty('java.vendor.url')]}" /></p>
    <p><g:message code="about.sysInfo.os" args="${[System.getProperty('os.name'), System.getProperty('os.arch'), System.getProperty('os.version')]}" /></p>

    <h3><g:message code="about.sysInfo.sytem" /></h3>
    <p><g:message code="about.sysInfo.freeMemory" args="${[Runtime.runtime.freeMemory()]}" /></p>
    <p><g:message code="about.sysInfo.totalMemory" args="${[Runtime.runtime.totalMemory()]}" /></p>
    <p><g:message code="about.sysInfo.maxMemory" args="${[Runtime.runtime.maxMemory()]}" /></p>
    <p><g:message code="about.sysInfo.cpus" args="${[Runtime.runtime.availableProcessors()]}" /></p>
  </section>
</body>
</html>