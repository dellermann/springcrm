<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="user.settings.title" /></title>
    <meta name="stylesheet" content="config" />
    <meta name="caption" content="${message(code: 'user.settings.title')}" />
  </head>

  <body>
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>

    <div class="row configuration-overview">
      <div class="col-xs-12 col-sm-6">
        <ul>
          <li>
            <g:link action="settingsLanguage" class="configuration-icon"
              role="presentation">
              <i class="fa fa-language"></i>
            </g:link>
            <g:link action="settingsLanguage" class="configuration-title">
              <g:message code="user.settings.language.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="user.settings.language.description" />
            </p>
          </li>
        </ul>
      </div>
      <div class="col-xs-12 col-sm-6">
        <ul>
          <li>
            <g:link action="settingsGoogleAuth" class="configuration-icon"
              role="presentation">
              <i class="fa fa-google"></i>
            </g:link>
            <g:link action="settingsGoogleAuth" class="configuration-title">
              <g:message code="user.settings.googleAuth.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="user.settings.googleAuth.description" />
            </p>
          </li>
        </ul>
      </div>
    </div>
  </body>
</html>
