<!DOCTYPE html>

<!--

  SpringCRM

  Copyright (c) 2011-2015, Daniel Ellermann

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.

-->

<html lang="${locale}" data-currency-symbol="${currencySymbol}"
  data-num-fraction-digits="${numFractionDigits}"
  data-num-fraction-digits-ext="${numFractionDigitsExt}"
  data-decimal-separator="${decimalSeparator}"
  data-grouping-separator="${groupingSeparator}"
  data-load-markdown-help-url="${createLink(controller: 'help', params: [type: 'markdown'])}">
  <head>
    <meta charset="utf-8" />
    <title><g:layoutTitle /> –
    <g:fieldValue bean="${helpdeskInstance}" field="name" /> –
    <g:message code="helpdesk.frontend.title" /> –
    <g:message code="default.appName" /></title>
    <asset:stylesheet src="${(pageProperty(name: 'meta.stylesheet') ?: 'frontend').toString()}" />
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="shortcut icon" href="favicon.png" type="image/png" />
    <asset:link rel="icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="icon" href="favicon.png" type="image/png" />
    <g:layoutHead />
  </head>

  <body>
    <div id="top"></div>
    <div id="frontend-container" class="container">
      <header role="banner">
        <div class="header-left">
          <g:link mapping="helpdeskFrontend"
            params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}"
            elementId="application-title" class="brand"
            ><g:message code="default.appName"
          /></g:link>
        </div>
        <div class="header-right">
          <h1><g:message code="helpdesk.label" />
          <g:fieldValue bean="${helpdeskInstance}" field="name" /></h1>
        </div>
      </header>
      <nav class="navbar navbar-default visible-xs" role="navigation">
        <div class="container-fluid">
          <div class="navbar-header">
            <g:if test="${actionName == 'frontendIndex'}">
            <g:link mapping="helpdeskFrontend"
              params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}"
              class="navbar-brand" role="button"
              ><g:message code="default.appName"
            /></g:link>
            </g:if>
            <g:else>
            <g:link mapping="helpdeskFrontend"
              params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}"
              class="navbar-back-link" role="button"
              ><i class="fa fa-arrow-left"></i>
              <g:message code="default.button.back.toList"
            /></g:link>
            </g:else>
          </div>
        </div>
      </nav>

      <div class="inner-container">
        <div class="row">
          <div class="title-toolbar">
            <div class="title">
              <h1><g:layoutTitle /></h1>
            </div>
            <div class="toolbar" role="toolbar"
              aria-label="${message(code: 'default.toolbar.label')}">
              <a href="#top" class="btn btn-default go-top-btn" role="button">
                <i class="fa fa-arrow-up"></i>
                <span class="sr-only"
                  ><g:message code="default.button.top.label"
                /></span>
              </a>
              <g:pageProperty name="page.toolbar" />
              <g:if test="${pageProperty(name: 'page.actionMenu')}">
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle"
                  data-toggle="dropdown" aria-haspopup="true" aria-owns="action-bar-menu"
                  ><i class="fa fa-cogs"></i> <g:message code="default.actions" />
                  <span class="caret"></span
                ></button>
                <ul id="action-bar-menu" class="dropdown-menu" role="menu"
                  aria-expanded="false">
                  <g:pageProperty name="page.actionMenu" />
                </ul>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="main-content" role="main">
          <g:render template="/layouts/flashMessage" />
          <g:render template="/layouts/errorMessage" />

          <g:layoutBody />
        </div>
      </div>
      <g:render template="/layouts/footer" />
    </div>

    <g:render template="/layouts/modals" />
    <div id="spinner" class="spinner" aria-hidden="true">
      <i class="fa fa-circle-o-notch fa-spin"></i>
    </div>

    <asset:i18n locale="${locale}" />
    <g:pageProperty name="page.scripts"
      default="${asset.javascript(src: 'application')}" />
    <asset:deferredScripts />
  </body>
</html>
