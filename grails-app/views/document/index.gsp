<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="document.plural" /></title>
    <meta name="stylesheet" content="document" />
  </head>

  <body>
    <content tag="toolbar">
      <button type="button" class="btn btn-success create-folder-button"
        data-toggle="modal" data-target="#create-folder-dialog">
        <i class="fa fa-plus-circle"></i>
        <g:message code="document.button.createFolder" />
      </button>
    </content>

    <g:render template="/layouts/flashMessage" />
    <div id="document-list" class="document-list"
      data-initial-path="${path}"
      data-list-url="${createLink(controller: 'document', action: 'dir')}"
      data-download-url="${createLink(controller: 'document', action: 'download')}"
      data-create-folder-url="${createLink(controller: 'document', action: 'createFolder')}"
      data-delete-url="${createLink(controller: 'document', action: 'delete')}"
      ></div>
    <form id="document-list-upload"
      action="${createLink(controller: 'document', action: 'upload')}"
      method="post" enctype="multipart/form-data">
      <h3><g:message code="document.upload.header" /></h3>
      <input type="hidden" id="current-path" name="path" value="${path}" />
      <input type="file" id="upload-file" name="file" multiple="multiple" />
    </form>

    <div id="create-folder-dialog" class="modal fade" tabindex="-1"
      role="dialog" aria-labelledby="create-folder-dialog-title"
      aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
              aria-label="${message(code: 'default.btn.close')}"
              ><span aria-hidden="true">×</span
            ></button>
            <h4 id="create-folder-dialog-title" class="modal-title"
              ><g:message code="document.createFolder.title"
            /></h4>
          </div>
          <div class="modal-body">
            <div class="form">
              <p>
                <label for="create-folder-name"
                  ><g:message code="document.createFolder.description"
                /></label>
              </p>
              <input type="text" class="form-control"
                placeholder="${message(code: 'document.createFolder.placeholder')}" />
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary create-btn"
              ><g:message code="document.createFolder.btn.create"
            /></button>
            <button type="button" class="btn btn-default"
              data-dismiss="modal"
              ><g:message code="default.button.cancel.label"
            /></button>
          </div>
        </div>
      </div>
    </div>

    <content tag="scripts">
      <asset:javascript src="document" />
    </content>
  </body>
</html>

