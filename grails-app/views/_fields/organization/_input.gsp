<input type="text" id="${property}" value="${bean?."${property}"?.name}" size="35" data-find-url="${createLink(controller: 'organization', action: 'find')}" />
<input type="hidden" name="${property}.id" id="${property}.id" value="${bean?."${property}"?.id}" />