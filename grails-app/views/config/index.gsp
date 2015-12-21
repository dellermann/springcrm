<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="config" />
    <meta name="caption" content="${message(code: 'config.title')}" />
  </head>

  <body>
    <g:render template="/layouts/flashMessage" />
    <g:render template="/layouts/errorMessage" />

    <div class="row configuration-overview">
      <div class="col-xs-12 col-sm-6">
        <ul>
          <li>
            <g:link action="loadClient" class="configuration-icon"
              role="presentation">
              <i class="fa fa-institution"></i>
            </g:link>
            <g:link action="loadClient" class="configuration-title">
              <g:message code="config.client.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.client.description" />
            </p>
          </li>
          <li>
            <g:link action="currency" class="configuration-icon"
              role="presentation">
              <i class="fa fa-euro"></i>
            </g:link>
            <g:link action="currency" class="configuration-title">
              <g:message code="config.currency.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.currency.description" />
            </p>
          </li>
          <li>
            <g:link action="show" params="[page: 'taxRates']"
              class="configuration-icon" role="presentation">
              <i class="fa fa-money"></i>
            </g:link>
            <g:link action="show" params="[page: 'taxRates']"
              class="configuration-title">
              <g:message code="config.taxRates.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.taxRates.description" />
            </p>
          </li>
          <li>
            <g:link action="show" params="[page: 'mail']"
              class="configuration-icon" role="presentation">
              <i class="fa fa-envelope"></i>
            </g:link>
            <g:link action="show" params="[page: 'mail']"
              class="configuration-title">
              <g:message code="config.mail.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.mail.description" />
            </p>
          </li>
          <li>
            <g:link action="show" params="[page: 'sync']"
              class="configuration-icon" role="presentation">
              <i class="fa fa-google"></i>
            </g:link>
            <g:link action="show" params="[page: 'sync']"
              class="configuration-title">
              <g:message code="config.sync.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.sync.description" />
            </p>
          </li>
        </ul>
      </div>
      <div class="col-xs-12 col-sm-6">
        <ul>
          <li>
            <g:link action="show" params="[page: 'ldap']"
              class="configuration-icon" role="presentation">
              <i class="fa fa-cubes"></i>
            </g:link>
            <g:link action="show" params="[page: 'ldap']"
              class="configuration-title">
              <g:message code="config.ldap.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.ldap.description" />
            </p>
          </li>
          <li>
            <g:link action="show" params="[page: 'selValues']"
              class="configuration-icon" role="presentation">
              <i class="fa fa-list"></i>
            </g:link>
            <g:link action="show" params="[page: 'selValues']"
              class="configuration-title">
              <g:message code="config.selValues.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.selValues.description" />
            </p>
          </li>
          <li>
            <g:link action="loadSeqNumbers" class="configuration-icon"
              role="presentation">
              <i class="fa fa-sort-numeric-asc"></i>
            </g:link>
            <g:link action="loadSeqNumbers" class="configuration-title">
              <g:message code="config.seqNumbers.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.seqNumbers.description" />
            </p>
          </li>
          <li>
            <g:link action="show" params="[page: 'paths']"
              class="configuration-icon" role="presentation">
              <i class="fa fa-folder-o"></i>
            </g:link>
            <g:link action="show" params="[page: 'paths']"
              class="configuration-title">
              <g:message code="config.paths.title" />
            </g:link>
            <p class="configuration-description">
              <g:message code="config.paths.description" />
            </p>
          </li>
        </ul>
      </div>
    </div>
  </body>
</html>
