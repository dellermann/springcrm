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
  data-tax-rates="${taxRatesString}"
  data-load-markdown-help-url="${createLink(controller: 'help', params: [type: 'markdown'])}">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title><g:title /> – <g:message code="default.appName" /></title>
    <asset:stylesheet src="${(pageProperty(name: 'meta.stylesheet') ?: 'application').toString()}" />
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="shortcut icon" href="favicon.png" type="image/png" />
    <asset:link rel="icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="icon" href="favicon.png" type="image/png" />
    <g:layoutHead />
  </head>

  <body role="application" aria-labelledby="application-title">
    <div id="top"></div>
    <div class="container">
      <g:render template="/layouts/header" />
      <g:render template="/layouts/nav" />
      <div class="inner-container">
        <div class="row">
          <div class="title-toolbar">
            <div class="title">
              <h1 class="hidden-xs">
                <g:if test="${pageProperty(name: 'meta.caption')}">
                <g:pageProperty name="meta.caption" />
                </g:if>
                <g:else>
                <g:message code="${controllerName}.plural"
                  default="${message(code: "${controllerName}.label")}" />
                </g:else>
              </h1>
              <g:if test="${!pageProperty(name: 'meta.noSubcaption')}">
              <h2 class="visible-xs">
                <g:if test="${pageProperty(name: 'meta.subcaption')}">
                <g:pageProperty name="meta.subcaption" />
                </g:if>
                <g:elseif test="${actionName == 'edit' || actionName == 'show'}">
                ${pageScope."${controllerName}Instance"}
                </g:elseif>
                <g:elseif test="${actionName == 'create'}">
                <g:message code="${controllerName}.new.label" />
                </g:elseif>
                <g:else>
                <g:message code="default.overview" />
                </g:else>
              </h2>
              </g:if>
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
            </div>
          </div>
        </div>
        <g:if test="${pageProperty(name: 'page.captionActionBar')}">
        <div class="caption-action-bar hidden-xs">
          <g:pageProperty name="page.captionActionBar" />
        </div>
        </g:if>
        <div class="main-content" role="main">
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
    <asset:javascript
      src="lang/bootstrap-datepicker/bootstrap-datepicker.${lang}.js" />
    <asset:deferredScripts />
  </body>
</html>
