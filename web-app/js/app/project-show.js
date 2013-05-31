// Generated by CoffeeScript 1.4.0
(function() {
  var $, changePhase, loadList, onChangeTopCheckbox, onChangeType, onClickAddBtn, onClickLink, onClickPhases, onClickSelectItem, onLoadedList, onOpenSelectDlg, onSelectProjectStatus, onSubmittedSelectedItems, showFileSelector, submitSelectedDocuments, submitSelectedItems;

  $ = jQuery;

  changePhase = function(phaseName) {
    $ = jQuery;
    return $.get($("#project-phases").data("set-phase-url"), {
      phase: phaseName
    });
  };

  loadList = function(url) {
    return $("#select-project-item-list").load(url, {
      view: "selector"
    }, onLoadedList);
  };

  onChangeTopCheckbox = function() {
    var $this;
    $this = $(this);
    return $this.parents(".content-table").find("tbody td:first-child input:checkbox").attr("checked", $this.is(":checked"));
  };

  onChangeType = function() {
    var $option, $this;
    $this = $(this);
    $option = $this.find(":selected");
    $("#select-project-item-dialog h2").text($option.text());
    if ($option.data("controller") === "document") {
      showFileSelector($this.val());
      $("#select-project-item-add-btn").fadeOut();
      $(".selector-toolbar-search").fadeOut();
      $("#select-project-item-list").fadeOut();
      return $("#select-project-document-list").fadeIn();
    } else {
      loadList($this.val());
      $("#select-project-item-add-btn").show();
      $(".selector-toolbar-search").show();
      $("#select-project-item-list").fadeIn();
      return $("#select-project-document-list").fadeOut();
    }
  };

  onClickAddBtn = function() {
    var ids;
    $ = jQuery;
    ids = [];
    $("#select-project-item-list tbody :checked").each(function() {
      return ids.push($(this).parents("tr").data("item-id"));
    });
    submitSelectedItems(ids);
    $("#select-project-item-dialog").dialog("close");
    return false;
  };

  onClickLink = function() {
    loadList($(this).attr("href"));
    return false;
  };

  onClickPhases = function(event) {
    var $section, $target, phaseName, res;
    $ = jQuery;
    $target = $(event.target);
    $section = $target.parents("section");
    phaseName = $section.data("phase");
    res = true;
    if ($target.is("#project-phases h5")) {
      $("#project-phase").text($target.text());
      $section.addClass("current").siblings().removeClass("current");
      changePhase(phaseName);
      res = false;
    } else if ($target.is(".project-phase-actions-create")) {
      $("#create-project-item-dialog").dialog({
        modal: true
      }).find("a").click(function() {
        window.location.href = "" + ($(this).attr('href')) + "&projectPhase=" + phaseName;
        return false;
      });
      res = false;
    } else if ($target.is(".project-phase-actions-select")) {
      $("#select-project-item-dialog").dialog({
        minWidth: 900,
        minHeight: 520,
        modal: true,
        open: onOpenSelectDlg
      }).data("phase", phaseName);
      res = false;
    } else if ($target.is(".item-delete-btn img")) {
      if ($.confirm($L("default.delete.confirm.msg"))) {
        $.get($target.closest("a").attr("href"), null, function() {
          return $target.closest("li").remove();
        });
      }
      res = false;
    }
    return res;
  };

  onClickSelectItem = function() {
    var itemId;
    itemId = $(this).parents("tr").data("item-id");
    submitSelectedItems([itemId]);
    $("#select-project-item-dialog").dialog("close");
    return false;
  };

  onLoadedList = function() {
    $ = jQuery;
    return $("#select-project-item-list").find(".content-table th input:checkbox").change(onChangeTopCheckbox).end().find("a").each(function() {
      var $this;
      $this = $(this);
      if ($this.is(".content-table tbody a")) {
        return $this.click(onClickSelectItem);
      } else {
        return $this.click(onClickLink);
      }
    });
  };

  onOpenSelectDlg = function() {
    $("#select-project-item-type-selector").change(onChangeType).trigger("change");
    return $("#select-project-item-add-btn").click(onClickAddBtn);
  };

  onSelectProjectStatus = function() {
    var $this, status;
    $this = $(this);
    status = $this.val();
    $.get($this.data("submit-url"), {
      status: status
    });
    return $("#project-status-indicator").attr("class", "project-status-" + status).text($this.find(":selected").text());
  };

  onSubmittedSelectedItems = function() {
    return window.location.reload(true);
  };

  showFileSelector = function(url) {
    return $("#select-project-document-list").elfinder({
      commands: ["open", "reload", "home", "up", "back", "forward", "getfile", "quicklook", "download", "rm", "duplicate", "rename", "mkdir", "mkfile", "upload", "copy", "cut", "paste", "edit", "search", "info", "view", "help", "sort"],
      commandsOptions: {
        getfile: {
          onlyURL: false
        }
      },
      contextmenu: {
        files: ["getfile", "|", "open", "quicklook", "|", "download", "|", "copy", "cut", "paste", "duplicate", "|", "rm", "|", "edit", "rename", "|", "info"]
      },
      getFileCallback: submitSelectedDocuments,
      lang: "de",
      uiOptions: {
        toolbar: [["back", "forward"], ["mkdir", "mkfile", "upload"], ["open", "download", "getfile"], ["info", "quicklook"], ["copy", "cut", "paste"], ["rm"], ["duplicate", "rename", "edit"], ["search"], ["view", "sort"], ["help"]]
      },
      url: url
    }).elfinder("instance");
  };

  submitSelectedDocuments = function(file) {
    var $dialog;
    $ = jQuery;
    $dialog = $("#select-project-item-dialog");
    return $.post($dialog.data("submit-url"), {
      projectPhase: $dialog.data("phase"),
      controllerName: "document",
      documents: file.hash
    }, onSubmittedSelectedItems);
  };

  submitSelectedItems = function(ids) {
    var $dialog;
    $ = jQuery;
    $dialog = $("#select-project-item-dialog");
    return $.post($dialog.data("submit-url"), {
      projectPhase: $dialog.data("phase"),
      controllerName: $("#select-project-item-type-selector :selected").data("controller"),
      itemIds: ids.join()
    }, onSubmittedSelectedItems);
  };

  $("#project-phases").click(onClickPhases);

  $("#project-status").selectBoxIt({
    theme: "jqueryui"
  }).change(onSelectProjectStatus);

}).call(this);
