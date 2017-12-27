<input type="hidden" name="${name}"
  value="${c ? formatDate(date: c, formatName: formatName) : ''}"/>
<g:if test="${useTime}">
  <div class="input-group date-time-control">
</g:if>
%{--

  Because the HTML 5 <input/> tag with type "date", "datetime", "time", and
  "datetime-local" fields do not support localized date/time strings we use
  type "text" here. Maybe in future this will be corrected in the HTML 5
  standard.

--}%
<input type="text" id="${id}-date" name="${name}_date"
  value="${c ? formatDate(date: c, formatName: 'default.format.date') : ''}"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>
<g:if test="${useTime}">
  <input type="text" id="${id}-time" name="${name}_time"
    value="${c ? formatDate(date: c, formatName: 'default.format.time') : ''}"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>
</g:if>
