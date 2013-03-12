<footer>
  <div id="app-version">
    SpringCRM v<g:meta name="app.version" /> (<g:message code="default.build.number" args="${[meta(name: 'app.buildNumber')]}"/>)
  </div>
  <div id="font-size-sel"></div>
  <ul id="footer-menu">
    <li><g:link controller="about"><g:message code="default.about.label" default="About" /></g:link></li>
  </ul>
  <div id="copyright"><g:message code="default.copyright" args="[new Date()]" />, <a href="http://www.amc-world.de" target="_blank">AMC World Technologies GmbH</a></div>
</footer>