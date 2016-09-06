<div id="alert-modal" class="modal fade" role="dialog"
  aria-labelledby="alert-modal-title" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
          aria-label="${message(code: 'default.btn.close')}"
          ><span aria-hidden="true">×</span
        ></button>
        <h4 id="alert-modal-title" class="modal-title">
          <g:message code="default.modal.alert.title" />
        </h4>
      </div>
      <div class="modal-body"><p></p></div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default btn-ok"
          data-dismiss="modal">
          <i class="fa fa-check-circle-o"></i>
          <span><g:message code="default.button.ok.label" /></span>
        </button>
      </div>
    </div>
  </div>
</div>

<div id="confirm-modal" class="modal fade" role="dialog"
  aria-labelledby="confirm-modal-title" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
          aria-label="${message(code: 'default.btn.close')}"
          ><span aria-hidden="true">×</span
        ></button>
        <h4 id="confirm-modal-title" class="modal-title">
          <g:message code="default.modal.confirm.title" />
        </h4>
      </div>
      <div class="modal-body"><p></p></div>
      <div class="modal-footer">
        <button type="button" class="btn btn-success btn-ok"
          data-dismiss="modal">
          <i class="fa fa-check-circle-o"></i>
          <span><g:message code="default.button.ok.label" /></span>
        </button>
        <button type="button" class="btn btn-default btn-cancel"
          data-dismiss="modal">
          <i class="fa fa-close"></i>
          <span><g:message code="default.button.cancel.label" /></span>
        </button>
      </div>
    </div>
  </div>
</div>

<div id="add-boilerplate-modal" class="modal fade" role="dialog"
  aria-labelledby="add-boilerplate-title" aria-hidden="true">
  <g:set var="boilerplateInstance"
    value="${new org.amcworld.springcrm.Boilerplate()}"/>
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
          aria-label="${message(code: 'default.btn.close')}"
          ><span aria-hidden="true">×</span
        ></button>
        <h4 id="add-boilerplate-title" class="modal-title">
          <g:message code="default.modal.addBoilerplate.title" />
        </h4>
      </div>
      <div class="modal-body">
        <g:form controller="boilerplate" action="save">
          <input type="hidden" name="noLruRecord" value="true"/>
          <f:field bean="${boilerplateInstance}" property="name"/>
          <f:field bean="${boilerplateInstance}" property="content"
            toolbar="none"/>
          <input type="submit" style="display: none;" aria-hidden="true"/>
        </g:form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-success btn-ok">
          <i class="fa fa-floppy-o"></i>
          <span><g:message code="default.button.save.label" /></span>
        </button>
        <button type="button" class="btn btn-default btn-cancel"
          data-dismiss="modal">
          <i class="fa fa-close"></i>
          <span><g:message code="default.button.cancel.label" /></span>
        </button>
      </div>
    </div>
  </div>
</div>
