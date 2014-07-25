<html>
<head>
  <meta name="layout" content="main" />
  <asset:stylesheet src="about" />
  <title><g:message code="about.sysInfo" default="System information" /></title>
</head>

<body>
  <header>
    <h1><g:message code="about.sysInfo" default="System information" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="index" color="white" icon="arrow-left"
          message="about.back.label" default="Back" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <h2><g:message code="about.sysInfo.application" /></h2>
    <p><g:message code="about.sysInfo.version" args="${[meta(name: 'app.version')]}" /></p>
    <p><g:message code="about.sysInfo.build" args="${[meta(name: 'app.buildNumber'), meta(name: 'app.buildDate'), meta(name: 'app.buildProfile')]}" /></p>
    <p><g:message code="about.sysInfo.grailsVersion" args="${[meta(name: 'app.grails.version')]}" /></p>

    <h2><g:message code="about.sysInfo.env" /></h2>
    <p><g:message code="about.sysInfo.java" args="${[System.getProperty('java.version'), System.getProperty('java.vendor'), System.getProperty('java.vendor.url')]}" /></p>
    <p><g:message code="about.sysInfo.os" args="${[System.getProperty('os.name'), System.getProperty('os.arch'), System.getProperty('os.version')]}" /></p>
    <p><g:message code="about.sysInfo.userName" args="${[System.getProperty('user.name')]}" /></p>

    <h2><g:message code="about.sysInfo.sytem" /></h2>
    <p><g:message code="about.sysInfo.freeMemory" args="${[Runtime.runtime.freeMemory()]}" /></p>
    <p><g:message code="about.sysInfo.totalMemory" args="${[Runtime.runtime.totalMemory()]}" /></p>
    <p><g:message code="about.sysInfo.maxMemory" args="${[Runtime.runtime.maxMemory()]}" /></p>
    <p><g:message code="about.sysInfo.cpus" args="${[Runtime.runtime.availableProcessors()]}" /></p>

    <h2><g:message code="about.sysInfo.pathes" /></h2>
    <p><g:message code="about.sysInfo.appHome" args="${[grailsApplication.config.springcrm.dir.base]}" /></p>
    <p><g:message code="about.sysInfo.userDir" args="${[System.getProperty('user.dir')]}" /></p>
    <p><g:message code="about.sysInfo.userHome" args="${[System.getProperty('user.home')]}" /></p>
    <p><g:message code="about.sysInfo.tempDir" args="${[System.getProperty('java.io.tmpdir')]}" /></p>
    <p><g:message code="about.sysInfo.classPath" args="${[System.getProperty('java.class.path')]}" /></p>
    <p><g:message code="about.sysInfo.libraryPath" args="${[System.getProperty('java.library.path')]}" /></p>
  </div>
</body>
</html>
