<footer role="contentinfo">
  <div class="row">
    <div class="col-xs-12 col-sm-6">
      <ul>
        <li><g:message code="default.appName" />
          <g:message code="default.version.long"
            args="[meta(name: 'app.version'), meta(name: 'app.buildNumber')]" />
        </li>
        <g:if test="${controllerName != 'install'}">
        <li>
          <g:link controller="about"
            ><g:message code="default.about.label"
          /></g:link>
        </li>
        </g:if>
      </ul>
    </div>
    <div class="col-xs-12 col-sm-6">
      <span class="hidden-xs"><g:message code="default.copyright.text" /></span>
      <g:message code="default.copyright" args="[new Date()]" />,
      <a href="http://www.amc-world.de" target="_blank"
        ><g:message code="default.manufacturer"
      /></a>
    </div>
  </div>
</footer>