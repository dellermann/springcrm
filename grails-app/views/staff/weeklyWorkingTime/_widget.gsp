<div class="input-group">
  <input type="text" id="${property}" name="${property}"
    value="${formatNumber(number: value, maxFractionDigits: 2)}"
    class="form-control form-control-number" size="${size ?: 10}"
    aria-describedby="${property}-unit" data-allow-null="true"/>
  <span id="${property}-unit" class="input-group-addon"
    ><g:message code="staff.weeklyWorkingTime.unit"/></span>
</div>
