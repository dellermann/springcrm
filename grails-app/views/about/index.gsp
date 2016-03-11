<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="about.title" /></title>
    <meta name="stylesheet" content="about" />
    <meta name="caption" content="${message(code: 'about.title')}" />
    <meta name="noSubcaption" content="true" />
  </head>

  <body>
    <content tag="toolbar">
      <span class="hidden-xs">
        <g:button action="sysInfo" color="default" icon="cog"
          message="about.sysInfo.label" />
      </span>
      <span class="visible-xs-inline">
        <g:button action="sysInfo" color="default" icon="cog"
          message="about.sysInfo.short" />
      </span>
    </content>

    <div class="row">
      <div class="col-xs-12 col-md-9">
        <p class="app-title">
          <strong><g:message code="default.appName" /></strong>
        </p>
        <p><g:message code="about.app.description" /></p>
        <p><g:message code="about.app.version"
          args="[
              meta(name: 'info.app.version'),
              meta(name: 'info.app.buildNumber'),
              meta(name: 'info.app.buildDate'),
              meta(name: 'grails.env')
            ]" /></p>
        <p><g:message code="about.app.copyright" args="[new Date()]" /></p>

        <h3><g:message code="about.license" /></h3>
        <p><g:message code="about.license.name" /></p>
        <pre class="license-text"
          ><g:render template="licenseApplication"
        /></pre>

        <h3><g:message code="about.furtherLicenses" /></h3>
        <p><g:message code="about.furtherLicenses.logo" /></p>
        <p><g:message code="about.furtherLicenses.gpl2" /></p>
        <p><g:message code="about.furtherLicenses.apache" /></p>
        <p><g:message code="about.furtherLicenses.mit" /></p>
        <p><g:message code="about.furtherLicenses.sil" /></p>
        <p><g:message code="about.furtherLicenses.bsd3" /></p>
        <p><g:message code="about.furtherLicenses.lightbox" /></p>
        <p><g:message code="about.furtherLicenses.handlebars" /></p>
        <pre class="license-text"
          ><g:render template="licenseHandlebars"
        /></pre>

        <h3><g:message code="about.authors" /></h3>
        <ul>
          <li><a href="mailto:d.ellermann@amc-world.de">Daniel
          Ellermann</a></li>
          <li><a href="mailto:p.drozd@amc-world.de">Philip Drozd</a></li>
        </ul>
      </div>
    </div>
  </body>
</html>
