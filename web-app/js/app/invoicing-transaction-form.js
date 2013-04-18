// Generated by CoffeeScript 1.4.0
(function() {
  var $, InvoicingTransactionWidget;

  $ = jQuery;

  InvoicingTransactionWidget = {
    options: {
      checkStageTransition: true,
      organizationId: "#organization\\.id",
      stageValues: null
    },
    getOrganizationId: function() {
      return {
        organization: $(this.options.organizationId).val()
      };
    },
    refreshModifiedClosingBalance: function(data) {
      var _this = this;
      return $.getJSON(data.url, {
        id: data.id
      }, function(d) {
        return _this.element.find("#still-unpaid").data("modified-closing-balance", -d.closingBalance).end().find("#paymentAmount").trigger("change");
      });
    },
    _create: function() {
      var _this = this;
      this.element.find(".price-table").invoicingitems().end().find("#organization").autocompleteex({
        select: function() {
          return _this._onSelectOrganization();
        }
      }).end().find("#person").autocompleteex({
        loadParameters: function() {
          return _this.getOrganizationId();
        }
      }).end().find("#still-unpaid").click(function(event) {
        return _this._onClickStillUnpaid(event);
      }).end().find("#paymentAmount").change(function() {
        return _this._onChangePaymentAmount();
      }).trigger("change").end().find("#addresses").addrfields({
        leftPrefix: "billingAddr",
        menuItems: [
          {
            action: "copy",
            side: "left",
            text: $L("invoicingTransaction.billingAddr.copy")
          }, {
            action: "loadFromOrganization",
            propPrefix: "billingAddr",
            side: "left",
            text: $L("invoicingTransaction.addr.fromOrgBillingAddr")
          }, {
            action: "copy",
            side: "right",
            text: $L("invoicingTransaction.shippingAddr.copy")
          }, {
            action: "loadFromOrganization",
            propPrefix: "shippingAddr",
            side: "right",
            text: $L("invoicingTransaction.addr.fromOrgShippingAddr")
          }
        ],
        organizationId: this.options.organizationId,
        rightPrefix: "shippingAddr"
      });
      return this._initStageValues();
    },
    _getModifiedClosingBalance: function($a) {
      if ($a == null) {
        $a = this.element.find("#still-unpaid");
      }
      return parseFloat($a.data("modified-closing-balance"));
    },
    _getTotal: function() {
      return $("#total-price").text().parseNumber();
    },
    _getUnpaid: function() {
      var paymentAmount;
      paymentAmount = this.element.find("#paymentAmount").val().parseNumber();
      return (this._getTotal() - paymentAmount - this._getModifiedClosingBalance()).round();
    },
    _initStageValues: function() {
      var el, opts, stageValues,
        _this = this;
      $ = jQuery;
      el = this.element;
      opts = this.options;
      stageValues = opts.stageValues;
      if (stageValues) {
        el.find("#stage").change(function(event) {
          return _this._onChangeStage(event);
        });
        if (stageValues.shipping) {
          $("#shippingDate-date").change(function(event) {
            return _this._onChangeShippingDate(event);
          });
          el.submit(function() {
            if (opts.checkStageTransition) {
              return _this._onSubmitForm();
            }
          });
        }
        if (stageValues.payment) {
          $("#paymentDate-date").change(function(event) {
            return _this._onChangePaymentDate(event);
          });
        }
      }
      return this;
    },
    _onChangePaymentAmount: function() {
      var cls, val;
      val = this._getUnpaid();
      if (val > 0) {
        cls = "still-unpaid-unpaid";
      } else if (val < 0) {
        cls = "still-unpaid-too-much";
      } else {
        cls = "still-unpaid-paid";
      }
      return this.element.find("#still-unpaid").removeClass("still-unpaid-unpaid still-unpaid-paid still-unpaid-too-much").addClass(cls).find("output").text(val.formatCurrencyValue());
    },
    _onChangePaymentDate: function(event) {
      var $stage, v;
      $stage = this.element.find("#stage");
      v = this.options.stageValues.payment;
      if ($(event.target).val() !== "" && $stage.val() < v) {
        return $stage.val(v);
      }
    },
    _onChangeShippingDate: function(event) {
      var $stage, v;
      $stage = this.element.find("#stage");
      v = this.options.stageValues.shipping;
      if ($(event.target).val() !== "" && $stage.val() < v) {
        return $stage.val(v);
      }
    },
    _onChangeStage: function(event) {
      var $input, stageValues;
      $ = jQuery;
      stageValues = this.options.stageValues;
      $input = null;
      switch (parseInt($(event.target).val(), 10)) {
        case stageValues.shipping:
          $input = $("#shippingDate-date");
          break;
        case stageValues.payment:
          $input = $("#paymentDate-date");
      }
      if ($input) {
        $input.datepicker("setDate", new Date()).trigger("change");
      }
      return true;
    },
    _onClickStillUnpaid: function(event) {
      var $paymentAmount, val;
      if (this._getUnpaid() > 0) {
        val = this._getTotal() - this._getModifiedClosingBalance($(event.target));
        $paymentAmount = this.element.find("#paymentAmount");
        if (val) {
          $paymentAmount.val(val.formatCurrencyValue()).trigger("change");
        }
      }
      return false;
    },
    _onSelectOrganization: function() {
      return this.element.find("#addresses").addrfields("loadFromOrganizationToLeft", "billingAddr").addrfields("loadFromOrganizationToRight", "shippingAddr");
    },
    _onSubmitForm: function() {
      var $oldStage, newVal, oldVal, res, shippingStageValue;
      res = true;
      $oldStage = $("#old-stage");
      if ($oldStage.length) {
        oldVal = $oldStage.val();
        if (oldVal > 0) {
          newVal = this.element.find("#stage").val();
          shippingStageValue = this.options.stageValues.shipping;
          if ((oldVal < shippingStageValue) && (newVal >= shippingStageValue)) {
            res = window.confirm($L("invoicingTransaction.changeState.label"));
          }
        }
      }
      return res;
    }
  };

  $.widget("springcrm.invoicingtransaction", InvoicingTransactionWidget);

}).call(this);
