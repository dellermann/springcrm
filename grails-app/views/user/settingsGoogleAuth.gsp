<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="user.settings.googleAuth.title" /> -
    <g:message code="user.settings.title" /></title>
    <meta name="caption" content="${message(code: 'user.settings.title')}" />
    <meta name="subcaption"
      content="${message(code: 'user.settings.googleAuth.title')}" />
    <meta name="backLinkUrl" content="${createLink(action: 'settingsIndex')}" />
  </head>

  <body>
    <g:render template="/layouts/flashMessage" />
    <g:render template="/layouts/errorMessage" />

    <g:if test="${authorized}">
      <div class="alert alert-info" role="alert">
        <g:message code="user.settings.googleAuth.alreadyAuthorized" />
      </div>
    </g:if>
    <g:else>
      <p><g:message code="user.settings.googleAuth.hints" /></p>
    </g:else>

    <div>
      <g:button action="settingsGoogleAuthRequest" color="success"
        icon="sign-in" message="user.settings.googleAuth.authorize" />
      <g:if test="${authorized}">
      <g:button elementId="google-auth-revoke" color="danger" icon="sign-out"
        message="user.settings.googleAuth.revoke" />
      </g:if>
      <g:button action="settingsIndex" color="default" icon="close"
        message="default.btn.cancel" />
    </div>

    <div id="modal-confirm" class="modal fade" tabindex="-1" role="dialog"
      aria-labelledby="modal-confirm-title"
      data-submit-url="${createLink(action: 'settingsGoogleAuthRevoke')}">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
              aria-label="${message(code: 'default.btn.close')}">
              <span aria-hidden="true">×</span>
            </button>
            <h4 id="modal-confirm-title" class="modal-title">
              <g:message code="user.settings.googleAuth.revoke.confirm.title" />
            </h4>
          </div>
          <div class="modal-body">
            <p><g:message code="user.settings.googleAuth.revoke.confirm.message" /></p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger disconnect-btn"
              ><i class="fa fa-sign-out"></i>
              <g:message code="user.settings.googleAuth.revoke.confirm.disconnect"
            /></button>
            <button type="button" class="btn btn-default" data-dismiss="modal"
              ><i class="fa fa-close"></i>
              <g:message code="default.button.cancel.label"
            /></button>
          </div>
        </div>
      </div>
    </div>

    <content tag="scripts">
      <asset:javascript src="settings-google-auth" />
    </content>
  </body>
</html>
