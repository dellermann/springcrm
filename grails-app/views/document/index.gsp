<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="document.plural" default="Documents" /></title>
  <meta name="stylesheet" content="document" />
</head>

<body>
  <header>
    <h1><g:message code="document.plural" default="Documents" /></h1>
  </header>
  <form id="document-list-upload"
    data-url="${createLink(controller: 'document', action: 'upload')}"
    enctype="multipart/form-data">
    <aside id="action-bar">
      <h3><g:message code="default.actions" /></h3>
      <ul>
        <li>
          <g:button color="green" size="medium" class="fileinput-button"
            icon="plus">
            <span><g:message code="document.button.add" /></span>
            <input type="file" name="file" multiple="multiple" />
          </g:button>
        </li>
        <li>
          <g:button color="blue" size="medium" class="start" icon="upload"
            message="document.button.upload" />
        </li>
        <li>
          <g:button color="orange" size="medium" class="cancel" icon="ban"
            message="document.button.cancel" />
        </li>
      </ul>
    </aside>
    <div id="content">
      <g:if test="${flash.message}">
      <div class="flash-message message" role="status">${raw(flash.message)}</div>
      </g:if>
      <h2><g:message code="document.allDocuments.label" default="All documents" /></h2>
      <div id="document-list" class="document-list lg" data-list="document"
        data-list-url="${createLink(controller: 'document', action: 'dir')}"
        data-download-url="${createLink(controller: 'document', action: 'download')}"
        ></div>
      <div class="fileupload-buttonbar">
        <div>
          <!-- The global file processing state -->
          <span class="fileupload-process"></span>
        </div>
        <div class="fileupload-progress fade">
          <!-- The global progress bar -->
          <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
            <div class="progress-bar progress-bar-success" style="width:0%;"></div>
          </div>
          <!-- The extended global progress state -->
          <div class="progress-extended">&nbsp;</div>
        </div>
      </div>
      <div id="upload-files" class="fileupload-filelist">
        <h3><g:message code="document.upload.header" /></h3>
        <input type="hidden" id="current-path" name="path" value="" />
        <table class="table table-striped" role="presentation">
          <tbody></tbody>
        </table>
      </div>
    </div>
  </form>
  <script id="add-upload-request-template" type="text/x-tmpl-mustache">
    {{#files}}
    <tr class="upload-request-template">
      <td class="preview"></td>
      <td class="name">{{name}}</td>
      <td class="size"></td>
      <td>
        <div class="progress" role="progressbar" aria-valuemin="0"
          aria-valuemax="100" aria-valuenow="0">
          <div class="progress-bar progress-bar-success"
            style="width: 0%;"></div>
        </div>
      </td>
      <td>
        <button class="button green start" disabled="disabled">
          <i class="fa fa-upload"></i> <g:message code="document.button.start" />
        </button>
        <button class="button orange cancel">
          <i class="fa fa-ban"></i> <g:message code="default.btn.cancel" />
        </button>
      </td>
    </tr>
    {{/files}}
  </script>
  <content tag="scripts">
    <asset:javascript src="document" />
  </content>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.thumbnailUrl) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                {% if (file.url) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                    <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
                <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.deleteUrl) { %}
                <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>Delete</span>
                </button>
                <input type="checkbox" name="delete" value="1" class="toggle">
            {% } else { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Cancel</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
</body>
</html>
