(function() {
  var $, onClickSendMessageBtn;

  $ = jQuery;

  onClickSendMessageBtn = function() {
    return $("#send-message-dialog").dialog({
      buttons: [
        {
          click: function() {
            return $(this).find("form").submit();
          },
          text: $L("default.button.send.label")
        }, {
          "class": "red",
          click: function() {
            return $(this).dialog("close");
          },
          text: $L("default.button.cancel.label")
        }
      ],
      minHeight: "15em",
      modal: true,
      width: "40em"
    });
  };

  $("#send-message-btn").on("click", onClickSendMessageBtn);

  $("#close-ticket-btn").on("click", function() {
    return $.confirm($L("ticket.changeStage.closed.confirm"));
  });

}).call(this);
