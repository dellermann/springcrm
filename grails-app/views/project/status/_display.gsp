<g:applyLayout name="fieldDisplay">
  <g:select name="project-status"
    from="${org.amcworld.springcrm.ProjectStatus.list()}" optionKey="id"
    value="${bean.status.id}"
    data-submit-url="${createLink(action: 'setStatus', id: bean.id)}" />
</g:applyLayout>