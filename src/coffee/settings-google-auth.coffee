$ = jQuery

$("#google-auth-revoke").on "click", ->
  $("#dialog-confirm").dialog
    buttons: [
        class: "red"
        click: ->
          $this = $(this)
          $this.dialog "close"
          window.location.href = $this.data "submit-url"
        text: $L("user.settings.googleAuth.revoke.confirm.disconnect")
      ,
        click: -> $(this).dialog "close"
        text: $L("default.button.cancel.label")
    ]
    modal: true
    resizable: false

