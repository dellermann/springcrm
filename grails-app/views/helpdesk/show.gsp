<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: helpdeskInstance]">
      <content tag="actionMenu">
        <li role="menuitem">
          <g:link mapping="helpdeskFrontend"
            params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
            target="_blank">
            <i class="fa fa-eye"></i>
            <g:message code="helpdesk.button.callFrontend" />
          </g:link>
        </li>
        <li role="menuitem">
          <g:link controller="ticket" action="index"
            params="[helpdesk: helpdeskInstance.id]">
            <i class="fa fa-ticket"></i>
            <g:message code="helpdesk.button.showTickets" />
          </g:link>
        </li>
      </content>

      <section>
        <header>
          <h3><g:message code="helpdesk.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${helpdeskInstance}" property="organization" />
          </div>
          <div class="column">
            <f:display bean="${helpdeskInstance}" property="name" />
            <f:display bean="${helpdeskInstance}" property="accessCode" />
            <div class="form-group">
              <label class="control-label"
                ><g:message code="helpdesk.feUrl.label"
              /></label>
              <div class="control-container">
                <g:link mapping="helpdeskFrontend"
                  params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}"
                  target="_blank"
                  ><g:createLink mapping="helpdeskFrontend"
                    params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}"
                    absolute="true"
                /></g:link>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="helpdesk.fieldset.users.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${helpdeskInstance}" property="users" />
          </div>
        </div>
      </section>
    </g:applyLayout>
  </body>
</html>
