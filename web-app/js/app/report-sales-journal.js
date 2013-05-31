// Generated by CoffeeScript 1.4.0
(function() {
  var $, onChangeYearSelector, onClickMonthSelector, submitFilter;

  $ = jQuery;

  onChangeYearSelector = function() {
    return submitFilter($("#month-selector").find(".current"));
  };

  onClickMonthSelector = function(event) {
    var $li;
    $li = $(event.target).parent("li");
    $li.siblings().removeClass("current").end().addClass("current");
    submitFilter($li);
    return false;
  };

  submitFilter = function($monthLink) {
    var params, url;
    params = {
      month: $monthLink.data("month"),
      year: $("#year-selector").val()
    };
    url = $monthLink.find("a").attr("href");
    url += (/\?/.test(url) ? "&" : "?") + $.param(params);
    return window.location.href = url;
  };

  $("#month-selector").click(onClickMonthSelector);

  $("#year-selector").selectBoxIt({
    theme: "jqueryui"
  }).change(onChangeYearSelector);

}).call(this);
