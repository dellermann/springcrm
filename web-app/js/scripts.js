/*
 * scripts.js
 * General scripting.
 *
 * $Id$
 *
 * Copyright (c) 2011, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


/**
 * @module springcrm
 */
(function (window, pkg, $) {
    
    "use strict";
    
    var Bootstrap;
    
    /* definitions */
    pkg.BASE_FONT_SIZE = 11; // px
    pkg.NUM_FONT_SIZES = 5;
    pkg.ADDRESS_FIELS = [ 
        "Street", "PoBox", "PostalCode", "Location", "State", "Country"
    ];
    
    /* classes */
    Bootstrap = function Bootstrap() {
        this.$body = $("body");
        this.$document = $(window.document);
        this.$search = $("#search");
        this.$toolbar = $("#toolbar-container");
        this.initialSearchText = this.$search.val();
        if (this.$toolbar.size()) {
            this.toolbarOffset = this.$toolbar.offset().top;
        }
        this.$spinner = $("#spinner");
        this.timeValues = this._computeTimeValues();
    };
    Bootstrap.prototype = {
            
        //-- Public methods ------------------------
    
        initPage: function () {
            if (this.$toolbar !== null) {
                this.$document.scroll($.proxy(this.onScrollDocument, this));
            }
            this.$search
                .focus($.proxy(
                    function (e) { this.swapSearchText(e, true); }, this
                ))
                .blur($.proxy(
                    function (e) { this.swapSearchText(e, false); }, this
                ));
            $("#quick-access").change(this.onChangeQuickAccess);
            $("#main-menu > li").hover(this.onMainMenuHover);
            $(".header-with-menu .menu").hover(this.onOptionMenuHover);
            $(".date-input-date").change(this.onChangeDateInput)
                .datepicker({
                    changeMonth: true, changeYear: true, gotoCurrent: true,
                    selectOtherMonths: true, showButtonPanel: true, 
                    showOtherMonths: true
                });
            $(".date-input-time").autocomplete({ source: this.timeValues });
            this._renderFontSizeSel();
            this._initAjaxEvents();
        },
        
        onChangeDateInput: function () {
            var $this = $(this),
                $otherPartField,
                baseId,
                id,
                partId,
                val = "";
            
            id = $this.attr("id");
            if (id.match(/^([\w\-]+)-(date|time)$/)) {
                baseId = RegExp.$1;
                partId = RegExp.$2;
                $otherPartField = 
                    $("#" + baseId + "-" + ((partId === "date") ? "time" : "date"));
                if (partId === "date") {
                    val += $this.val();
                    if ($otherPartField.length) {
                        val += " " + $otherPartField.val();
                    }
                } else {
                    if ($otherPartField.length) {
                        val += $otherPartField.val() + " ";
                    }
                    val += $this.val();
                }
                $("#" + baseId).val(val);
            }
        },
        
        onChangeFontSize: function (e) {
            var $target = $(e.target);
            
            this.$body
                .css("font-size", $target.css("font-size"));
            $target
                .addClass("current")
                .siblings(".current")
                    .removeClass("current");
        },
        
        onChangeQuickAccess: function () {
            var $this = $(this),
                val;
            
            val = $this.val();
            $this.val("");
            if (val !== "") {
                window.location.href = val;
            }
        },
    
        onMainMenuHover: function () {
            $(this).find("ul")
                .slideToggle();
        },
        
        onOptionMenuHover: function () {
            $(this).find("ul")
                .slideToggle();
        },
        
        onScrollDocument: function () {
            if (this.$document.scrollTop() >= this.toolbarOffset) {
                this.$toolbar.addClass("fixed");
            } else {
                this.$toolbar.removeClass("fixed");
            }
        },
        
        swapSearchText: function (e, dir) {
            var $input,
                value1 = dir ? this.initialSearchText : "",
                value2 = dir ? "" : this.initialSearchText;
            
            $input = $(e.currentTarget);
            if ($input.val() === value1) {
                $input.val(value2);
            }
        },
        
        
        //-- Non-public methods ---------------------
        
        _computeTimeValues: function () {
            var h = -1,
                hh,
                res = [];
            
            for (h = -1; ++h < 24; ) {
                hh = h.toString();
                if (hh.length < 2) {
                    hh = "0" + hh;
                }
                res.push(hh + ":00");
                res.push(hh + ":30");
            }
            return res;
        },
        
        _initAjaxEvents: function () {
            this.$spinner
                .ajaxSend(function () { $(this).show(); })
                .ajaxComplete(function () { $(this).hide(); });
        },
    
        _renderFontSizeSel: function () {
            var baseFontSize = pkg.BASE_FONT_SIZE,
                currentSize,
                i = -1,
                numFontSizes = pkg.NUM_FONT_SIZES,
                offset,
                s,
                size;
            
            offset = Math.floor(pkg.NUM_FONT_SIZES / 2);
            currentSize = parseInt(this.$body.css("font-size"), 10);
            s = '<ul id="font-size-sel">';
            for (; ++i < numFontSizes; ) {
                size = baseFontSize - offset + i;
                s += '<li ';
                if (size === currentSize) {
                    s += 'class="current" ';
                }
                s += 'style="font-size: ' + String(size) + 'px;">A</li>';
            }
            $("#app-version").after(s + '</ul>');
            $("#font-size-sel").click($.proxy(this.onChangeFontSize, this));
        }
    };
    
    pkg.Bootstrap = Bootstrap;
    
    /* static functions */
    pkg.copyAddress = function (from, to) {
        var addrFields,
            f,
            i = -1,
            n,
            p = pkg;
        
        addrFields = p.ADDRESS_FIELS;
        if (!pkg._doesAddressExists(to) 
            || window.confirm(p.messages["copyAddressWarning_" + to]))
        {
            for (i = -1, n = addrFields.length; ++i < n; ) {
                f = addrFields[i];
                p._getAddressField(to, f).val(p._getAddressField(from, f).val());
            }
        }
    };
    
    pkg.onClickSubmit = function (formId) {
        $("#" + formId).submit();
    };
    
    pkg.retrieveAddrFromOrganization = function (prefix, orgPrefix, url) {
        $.ajax({
            url: url, dataType: "json",
            data: { id: $("#organization-sel").val() },
            context: { p: pkg, prefix: prefix, orgPrefix: orgPrefix },
            success: function (data) {
                var addrFields,
                    f,
                    i = -1,
                    n,
                    orgPrefix,
                    p = this.p,
                    prefix = this.prefix;
                
                if (!p._doesAddressExists(prefix)
                    || window.confirm(p.messages["copyAddressWarning_" + prefix]))
                {
                    orgPrefix = this.orgPrefix;
                    addrFields = p.ADDRESS_FIELS;
                    for (i = -1, n = addrFields.length; ++i < n; ) {
                        f = addrFields[i];
                        p._getAddressField(prefix, f).val(data[orgPrefix + f]);
                    }
                }
            }
        });
    };
    
    /* private static functions */
    pkg._getAddressField = function (prefix, name) {
        return $("#" + prefix + name);
    };
    
    pkg._doesAddressExists = function (prefix) {
        var addrFields,
            i = -1,
            n,
            p = pkg,
            res = false;
        
        addrFields = p.ADDRESS_FIELS;
        for (i = -1, n = addrFields.length; ++i < n; ) {
            res = res || p._getAddressField(prefix, addrFields[i]).val() !== "";
        }
        return res;
    };
    
    /* jQuery extensions */
    $.extend({
        formatCurrency: function (x) {
            return $.formatNumber(x, 2);
        },
        
        formatNumber: function (x, n) {
            var i,
                s;
            
            x *= 1;
            s = (n === null) ? x.toString() : x.toFixed(n);
            s = s.replace(/\./, ",");
            i = s.lastIndexOf(",") - 3;
            while (i > 0) {
                s = s.substring(0, i) + "." + s.substring(i);
                i -= 3;
            }
            return s;
        },
        
        parseNumber: function (s) {
            return (s === "") ? 0 
                    : parseFloat(s.replace(/\./, "").replace(/,/, "."));
        }
    });
    
    /* main */
    pkg.bootstrap = new pkg.Bootstrap();
    pkg.bootstrap.initPage();
    
    /* TODO: handle info boxes in content tables */
    /*
    $(".content-table").mouseover(function (e) {
            var target = e.target;
            if (target.tagName == "a") {
                $(target).siblings(".info-box")
                    .show();
            }
        });
    */
}(this, springcrm, jQuery));

// vim:set ts=4 sw=4 sts=4:
