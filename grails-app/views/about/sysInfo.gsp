<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="about.sysInfo" /></title>
    <meta name="stylesheet" content="about" />
    <meta name="caption" content="${message(code: 'about.sysInfo')}" />
    <meta name="noSubcaption" content="true" />
  </head>

  <body>
    <%--
    <div class="row">
      <div class="title-toolbar">
        <div class="title">
          <h1 class="hidden-xs"><g:message code="about.sysInfo" /></h1>
          <h2 class="visible-xs"><g:message code="about.sysInfo" /></h2>
        </div>
      </div>
    </div>
    --%>

    <content tag="toolbar">
      <g:button action="index" color="default" icon="arrow-left"
        message="about.back.label" />
    </content>

    <section>
      <h3><g:message code="about.sysInfo.application" /></h3>
      <dl class="dl-horizontal sys-info-list">
        <dt><g:message code="about.sysInfo.version" /></dt>
        <dd><g:meta name="info.app.version" /></dd>
        <dt><g:message code="about.sysInfo.build" /></dt>
        <dd
          ><g:message code="about.sysInfo.build.value"
            args="[
                meta(name: 'info.app.buildNumber'),
                meta(name: 'info.app.buildDate'),
                meta(name: 'grails.env')
              ]"
        /></dd>
        <dt><g:message code="about.sysInfo.grailsVersion" /></dt>
        <dd><g:meta name="info.app.grailsVersion" /></dd>
      </dl>
    </section>

    <section>
      <h3><g:message code="about.sysInfo.env" /></h3>
      <dl class="dl-horizontal sys-info-list">
        <dt><g:message code="about.sysInfo.java" /></dt>
        <dd
          ><g:message code="about.sysInfo.java.value"
            args="[
                System.getProperty('java.version'),
                System.getProperty('java.vendor'),
                System.getProperty('java.vendor.url')
              ]"
        /></dd>
        <dt><g:message code="about.sysInfo.os" /></dt>
        <dd
          ><g:message code="about.sysInfo.os.value"
            args="[
                System.getProperty('os.name'), System.getProperty('os.arch'),
                System.getProperty('os.version')
              ]"
        /></dd>
        <dt><g:message code="about.sysInfo.userName" /></dt>
        <dd>${System.getProperty('user.name')}</dd>
      </dl>
    </section>

    <section>
      <h3><g:message code="about.sysInfo.sytem" /></h3>
      <dl class="dl-horizontal sys-info-list">
        <dt><g:message code="about.sysInfo.freeMemory" /></dt>
        <dd>
          <g:formatSize number="${Runtime.runtime.freeMemory()}" />B
          (<g:message code="about.sysInfo.bytes"
            args="[Runtime.runtime.freeMemory()]" />)
        </dd>
        <dt><g:message code="about.sysInfo.totalMemory" /></dt>
        <dd>
          <g:formatSize number="${Runtime.runtime.totalMemory()}" />B
          (<g:message code="about.sysInfo.bytes"
            args="[Runtime.runtime.totalMemory()]" />)
        </dd>
        <dt><g:message code="about.sysInfo.maxMemory" /></dt>
        <dd>
          <g:formatSize number="${Runtime.runtime.maxMemory()}" />B
          (<g:message code="about.sysInfo.bytes"
            args="[Runtime.runtime.maxMemory()]" />)
        </dd>
        <dt><g:message code="about.sysInfo.cpus" /></dt>
        <dd>${Runtime.runtime.availableProcessors()}</dd>
      </dl>
    </section>

    <section>
      <h3><g:message code="about.sysInfo.pathes" /></h3>
      <dl class="dl-horizontal sys-info-list">
        <dt><g:message code="about.sysInfo.appHome" /></dt>
        <dd><code>${grailsApplication.config.springcrm.dir.base}</code></dd>
        <dt><g:message code="about.sysInfo.userDir" /></dt>
        <dd><code>${System.getProperty('user.dir')}</code></dd>
        <dt><g:message code="about.sysInfo.userHome" /></dt>
        <dd><code>${System.getProperty('user.home')}</code></dd>
        <dt><g:message code="about.sysInfo.tempDir" /></dt>
        <dd><code>${System.getProperty('java.io.tmpdir')}</code></dd>
      </dl>
      <dl>
        <dt><g:message code="about.sysInfo.classPath" /></dt>
        <dd>
          <ol>
            <g:each in="${System.getProperty('java.class.path').split(System.getProperty('path.separator'))}">
            <g:if test="${it}">
            <li><code>${it}</code></li>
            </g:if>
            </g:each>
          </ol>
        </dd>
        <dt><g:message code="about.sysInfo.libraryPath" /></dt>
        <dd>
          <ol>
            <g:each in="${System.getProperty('java.library.path').split(System.getProperty('path.separator'))}">
            <g:if test="${it}">
            <li><code>${it}</code></li>
            </g:if>
            </g:each>
          </ol>
        </dd>
      </dl>
    </section>
  </body>
</html>
