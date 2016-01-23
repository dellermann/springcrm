<input type="hidden" id="stage-old"
  value="${session.credential.admin ? 0 : bean?.stage?.id}" />
<g:select name="stage" id="stage-select" from="${type.list()}" optionKey="id"
  value="${bean?.stage?.id}" />