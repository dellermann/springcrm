<input type="hidden" id="stage-old"
  value="${session.user.admin ? 0 : bean?.stage?.id}" />
<g:select name="stage" id="stage" from="${type.list()}" optionKey="id"
  value="${bean?.stage?.id}" />
