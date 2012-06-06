<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></title>
  <r:script>
  (function (window, $) {
  
      "use strict";
      
      $("#google-auth-revoke").click(function () {
              $("#dialog-confirm").dialog({
                  buttons: {
                      "${message(code: 'user.settings.googleAuth.revoke.confirm.disconnect')}": function () {
                          $(this).dialog("close");
                          window.location.href = "${createLink(action: 'settingsGoogleAuthRevoke')}";
                      },
                      "${message(code: 'default.btn.cancel')}": function () {
                          $(this).dialog("close");
                      }
                  },
                  height: 140,
                  modal: true,
                  resizable: false
              });
          });
  }(window, jQuery));
  </r:script>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></h2>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${authorized}"><p><g:message code="user.settings.googleAuth.alreadyAuthorized" /></p></g:if>
    <g:else><p><g:message code="user.settings.googleAuth.hints" /></p></g:else>
    <p><g:link action="settingsGoogleAuthRequest" class="button green"><g:message code="user.settings.googleAuth.authorize" default="Authorize at Google" /></g:link> <g:if test="${authorized}"><a id="google-auth-revoke" href="#" class="button red"><g:message code="user.settings.googleAuth.revoke" default="Disconnect" /></a> </g:if><g:link action="settingsIndex" class="button white"><g:message code="default.btn.cancel" default="Cancel" /></g:link></p>
    <div id="dialog-confirm" title="${message(code: 'user.settings.googleAuth.revoke.confirm.title')}" style="display: none;">
      <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><g:message code="user.settings.googleAuth.revoke.confirm.message" /></p>
    </div>
  </section>
</body>
</html>
