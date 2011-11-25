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
 * @fileOverview    Contains general classes which are used within this
 *                  application.
 * @author          Daniel Ellermann
 * @version         0.9
 */


(function (window, SPRINGCRM, $) {

    "use strict";

    var AddrFields = null,
        FixedSelAutocomplete = null,
        LightBox = null,
        Page = null,
        RemoteList = null,
        page = null;


    //== Classes ================================

    /**
     * Creates a new object to initialize the basic elements of the page.
     *
     * @class               Represents a web page in this application.
     * @constructor
     * @returns {Object}    the generated page initialization object
     */
    Page = function Page() {
        var $toolbar = $("#toolbar-container");

        if (!(this instanceof Page)) {
            return new Page();
        }


        //-- Instance variables -----------------

        /**
         * The jQuery object representing the body of the document.
         *
         * @type jQueryObject
         */
        this._$body = $("body");

        /**
         * The jQuery object representing the whole document.
         *
         * @type JQueryObject
         */
        this._$document = $(window.document);

        /**
         * The jQuery object representing the toolbar.
         *
         * @type JQueryObject
         */
        this._$toolbar = $toolbar;

        /**
         * The vertical offset of the toolbar when loading the page.
         *
         * @type Number
         */
        this._toolbarOffset = $toolbar.size() ? $toolbar.offset().top : 0;

        /**
         * The jQuery object representing the spinner which is used to indicate
         * long-term operations.
         *
         * @type JQueryObject
         */
        this._$spinner = $("#spinner");

        /**
         * The values which are displayed in time selectors.
         *
         * @type Array
         */
        this._timeValues = (function () {
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
        }());
    };

    /**
     * Enables the display of hints if the user sets the focus into a search
     * field.
     */
    Page.enableSearchFieldHints = function () {
        $(".search-field")
            .unbind("focus")
            .unbind("blur")
            .focus(function (e) { Page._swapSearchText(e, true); })
            .blur(function (e) { Page._swapSearchText(e, false); });
    };

    /**
     * Fills in the given date or the current date into the given field if it
     * is empty.
     *
     * @param {JQueryObject} $field the given input field
     * @param {Date} [date]         the given date; if not specified the
     *                              current date is used
     * @since                       0.9.10
     */
    Page.fillInDate = function ($field, date) {
        if ($field.val() === "") {
            $field.val($.formatDate(date, "date"))
                .trigger("change");
        }
    };

    /**
     * Exchanges the text of the search field.
     *
     * @param {Object} e    the event data
     * @param {Boolean} dir if <code>true</code> the initial search field
     *                      text is replaced by an empty string;
     *                      <code>false</code> otherwise
     * @protected
     */
    Page._swapSearchText = function (e, dir) {
        var $input,
            value1 = dir ? SPRINGCRM.getMessage("search") : "",
            value2 = dir ? "" : SPRINGCRM.getMessage("search");

        $input = $(e.currentTarget);
        if ($input.val() === value1) {
            $input.val(value2);
        }
    };

    Page.prototype = {

        //-- Public methods ---------------------

        /**
         * Initializes the page and their elements.
         */
        init: function () {
            if (this._$toolbar !== null) {
                this._$document.scroll($.proxy(this._onScrollDocument, this));
            }
            Page.enableSearchFieldHints();
            $("#search").next("a")
                .click(function () {
                    window.document.forms.searchableForm.submit();
                    return false;
                });
            $("#quick-access").change(this._onChangeQuickAccess);
            $("#main-menu > li").hover(this._onMenuHover);
            $(".menu").hover(this._onMenuHover);
            $(".delete-btn").click(this._onClickDeleteBtn);
            $(".date-input-date").change(this._onChangeDateInput)
                .datepicker({
                    changeMonth: true, changeYear: true, gotoCurrent: true,
                    selectOtherMonths: true, showButtonPanel: true,
                    showOtherMonths: true
                });
            $(".date-input-time").change(this._onChangeDateInput)
                .autocomplete({ source: this._timeValues });
            $("#spinner").click(function () {
                $(this).css("display", "none");
            });
            $("#autoNumber").change(this._onChangeAutoNumber)
                .triggerHandler("change");
            this._initAjaxEvents();
        },

        /**
         * Renders the font size selector.
         *
         * @param {String} url      the URL which is to call to persist the
         *                          currently selected font size
         * @param {String} defSize  the CSS font size value specifying the
         *                          font size to select as default
         */
        renderFontSizeSel: function (url, defSize) {
            var baseFontSize = SPRINGCRM.BASE_FONT_SIZE,
                currentSize,
                i = -1,
                numFontSizes = SPRINGCRM.NUM_FONT_SIZES,
                offset,
                s,
                size;

            offset = Math.floor(numFontSizes / 2);
            currentSize = parseInt(this._$body.css("font-size"), 10);
            if (defSize) {
                currentSize = parseInt(defSize, 10);
            }
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
            $("#font-size-sel").click($.proxy(
                    function (e) {
                        this._onChangeFontSize(e.target, url);
                    },
                    this
                ));
        },


        //-- Non-public methods -----------------

        /**
         * Initializes the handling of AJAX requests. The method cares about
         * display of a spinner view while loading data.
         *
         * @protected
         */
        _initAjaxEvents: function () {
            this._$spinner
                .ajaxSend(function () { $(this).show(); })
                .ajaxComplete(function () { $(this).hide(); });
        },

        /**
         * Called if the auto number check box was changed. The method enables
         * or disables the number field.
         *
         * @protected
         * @since 0.9.10
         */
        _onChangeAutoNumber: function () {
            $("#number").toggleEnable(!this.checked);
        },

        /**
         * Called if either the date or time part of a date/time input field
         * has changed. The method computes a formatted composed value in a
         * hidden date/time field.
         *
         * @protected
         */
        _onChangeDateInput: function () {
            var baseId,
                els,
                otherPartField,
                partId,
                type = "",
                val = "";

            els = this.form.elements;
            if (this.id.match(/^([\w\-]+)-(date|time)$/)) {
                baseId = RegExp.$1;
                partId = RegExp.$2;
                otherPartField =
                    els[baseId + "_" + ((partId === "date") ? "time" : "date")];
                if (partId === "date") {
                    val += this.value;
                    type = "date";
                    if (otherPartField) {
                        val += " " + otherPartField.value;
                        type += "time";
                    }
                } else {
                    if (otherPartField) {
                        val += otherPartField.value + " ";
                        type = "date";
                    }
                    val += this.value;
                    type += "time";
                }
                els[baseId].value = val;
            }
        },

        /**
         * Called if the font size of the document is to change.
         *
         * @param {Object} target   the target of the click event
         * @param {String} url      the URL which is to call to persist the
         *                          currently selected font size
         * @protected
         */
        _onChangeFontSize: function (target, url) {
            var $target = $(target),
                fontSize = $target.css("font-size");

            this._$body
                .css("font-size", fontSize);
            $target
                .addClass("current")
                .siblings(".current")
                    .removeClass("current");
            $.ajax({
                data: { key: "fontSize", value: fontSize },
                url: url
            });
        },

        /**
         * Called if an item of the quick access selector was selected. The
         * method calls the associated URL.
         *
         * @protected
         */
        _onChangeQuickAccess: function () {
            var $this = $(this),
                val;

            val = $this.val();
            $this.val("");
            if (val !== "") {
                window.location.href = val;
            }
        },

        /**
         * Called if the user clicks the button to delete a record.
         *
         * @returns {Boolean}   <code>true</code> to delete the record;
         *                      <code>false</code> to abort the operation
         * @protected
         * @since               0.9.10
         */
        _onClickDeleteBtn: function () {
            var $this = $(this),
                res,
                url;

            res = window.confirm(SPRINGCRM.getMessage('deleteConfirmMsg'));
            if (res) {
                url = $this.attr("href");
                if (url.indexOf("?") < 0) {
                    url += "?";
                } else {
                    url += "&";
                }
                $this.attr("href", url + "confirmed=1");
            }
            return res;
        },

        /**
         * Called if the user enters or leaves a menu. The method shows or
         * hides the submenu.
         *
         * @protected
         */
        _onMenuHover: function () {
            $(this).find("ul")
                .stop(true, true)
                .slideToggle();
        },

        /**
         * Called if the document is scrolled.
         *
         * @protected
         */
        _onScrollDocument: function () {
            if (this._$document.scrollTop() >= this._toolbarOffset) {
                this._$toolbar.addClass("fixed");
            } else {
                this._$toolbar.removeClass("fixed");
            }
        }
    };
    SPRINGCRM.Page = Page;

    /**
     * Creates a new autocomplete field which stores the selected labels and
     * values in separate input fields and forces the user to select a value.
     *
     * @class                               An autocomplete field which stores
     *                                      the selected labels and values in
     *                                      separate input fields and forces
     *                                      the user to select a value
     * @constructor
     * @param {Object} config               the configuration data for this
     *                                      input
     * @param {String} config.baseId        the base ID used to compute the IDs
     *                                      of the label and value input fields
     * @param {String} config.labelInputId  the ID of the input field where the
     *                                      user enters the search term and
     *                                      where the label of the selected
     *                                      item is displayed
     * @param {String} config.valueInputId  the ID of the (usually hidden) input
     *                                      field where the value of the
     *                                      selected item is stored
     * @param {Boolean} [config.combobox]   whether or not the autocomplete
     *                                      field is displayed as combobox. In
     *                                      this case an additional button is
     *                                      rendered beside the autocomplete
     *                                      input field.
     * @param {String} config.findUrl       the URL used to query for the
     *                                      entered search term
     * @param {String} [config.labelProp]   the name of the property in the
     *                                      returned JSON data where the labels
     *                                      of the selector items are stored
     * @param {String} [config.valueProp]   the name of the property in the
     *                                      returned JSON data where the values
     *                                      of the selector items are stored
     * @param {Object|Function} [config.parameters]
     *                                      any additional parameters which are
     *                                      sent to the server. If this is a
     *                                      function it will be called to
     *                                      produce additional parameters.
     * @param {Function} [config.onSelect]  a function which is called when the
     *                                      user selects an item. The function
     *                                      gets two parameters: the value and
     *                                      the label of the selected item.
     * @returns {Object}                    the generated autocomplete field
     *                                      object
     */
    FixedSelAutocomplete = function FixedSelAutocomplete(config) {
        var baseId = config.baseId;

        /* handle function call without new */
        if (!(this instanceof FixedSelAutocomplete)) {
            return new FixedSelAutocomplete(config);
        }


        //-- Instance variables -----------------

        /**
         * The input field where the user enters the search term and where the
         * label of the selected item is displayed.
         *
         * @type JQueryObject
         * @default "{baseId}"
         */
        this._$labelInput = $("#" + (config.labelInputId || baseId));

        /**
         * The (usually hidden) input field where the value of the selected
         * item is stored.
         *
         * @type JQueryObject
         * @default "{baseId}-id"
         */
        this._$valueInput = $("#" + (config.valueInputId || baseId + "-id"));

        /**
         * Determines whether or not the autocomplete field is displayed as
         * combobox. In this case an additional button is rendered beside the
         * autocomplete input field.
         *
         * @type Boolean
         * @default true
         */
        this._combobox = config.combobox || true;

        /**
         * The URL used to query for the entered search term.
         *
         * @type String
         */
        this._findUrl = config.findUrl;

        /**
         * The name of the property in the returned JSON data where the labels
         * of the selector items are stored.
         *
         * @type String
         * @default "name"
         */
        this._labelProp = config.labelProp || "name";

        /**
         * The name of the property in the returned JSON data where the values
         * of the selector items are stored.
         *
         * @type String
         * @default "id"
         */
        this._valueProp = config.valueProp || "id";

        /**
         * The previous selection value before a new selection was started.
         *
         * @type String
         * @default ""
         * @see FixedSelAutocomplete#_onFocus
         * @see FixedSelAutocomplete#_onBlur
         */
        this._oldValue = "";

        /**
         * The previous selection label before a new selection was started.
         *
         * @type String
         * @default ""
         * @see FixedSelAutocomplete#_onFocus
         * @see FixedSelAutocomplete#_onBlur
         */
        this._oldLabel = "";

        /**
         * Any additional parameters which are sent to the server. If this is a
         * function it will be called to produce additional parameters.
         *
         * @type Object|Function
         * @default {}
         */
        this._parameters = config.parameters || {};

        /**
         * A function which is called when the user selects an item. The
         * function gets two parameters: the value and the label of the
         * selected item.
         *
         * @type Function
         */
        this._onSelectFunc = config.onSelect;
    };

    FixedSelAutocomplete.prototype = {

        //-- Public methods ---------------------

        /**
         * Initializes the autocomplete field.
         */
        init: function () {
            this._$labelInput
                .focus($.proxy(this._onFocus, this))
                .blur($.proxy(this._onBlur, this))
                .autocomplete({
                    focus: $.proxy(this._onFocusItem, this),
                    select: $.proxy(this._onSelect, this),
                    source: $.proxy(this._sendFindRequest, this)
                });
            if (this._combobox) {
                this._$labelInput
                    .addClass("combobox")
                    .after($('<button/>', {
                        "class": "combobox",
                        click: $.proxy(this._onClickComboboxBtn, this),
                        text: "...",
                        type: "button"
                    }));
            }
        },


        //-- Non-public methods -----------------

        /**
         * Called if a successful AJAX response was received.
         *
         * @param {Function} response  the function which should be called to
         *                             render the received labels and values
         * @param {Object} data        the received JSON data
         * @private
         */
        _handleResponse: function (response, data) {
            var labelProp = this._labelProp,
                valueProp = this._valueProp;

            response($.map(data, function (item) {
                return { label: item[labelProp], value: item[valueProp] };
            }));
        },

        /**
         * Called if the user leaves the autocomplete field. The method
         * restores a previously stored label and value to force the user
         * select a concrete value.
         *
         * @see        FixedSelAutocomplete#_onFocus
         * @private
         */
        _onBlur: function () {
            this._$valueInput.val(this._oldValue);
            this._$labelInput.val(this._oldLabel);
        },

        /**
         * Called if the combobox button beside the autocomplete field was
         * clicked. The method starts a search for all entries.
         *
         * @param {Object} event    the event data
         * @private
         */
        _onClickComboboxBtn: function (event) {
            var $input = this._$labelInput;

            if ($input.autocomplete("widget").is(":visible")) {
                $input.autocomplete("close");
                return;
            }

            /* work around a bug (likely same cause as #5265) */
            $(event.target).blur();

            /* pass wildcard as value to search for, displaying all results */
            $input.autocomplete("search", "%");
            $input.focus();
        },

        /**
         * Called if the user enters the autocomplete field. The method stores
         * the currently active label and value for later recovery by the
         * {@link FixedSelAutocomplete#_onBlur} method.
         *
         * @private
         */
        _onFocus: function () {
            this._oldValue = this._$valueInput.val();
            this._oldLabel = this._$labelInput.val();
        },

        /**
         * Called if an item from the autocomplete selector was focused, but
         * not selected. The method displays the label of the item instead of
         * the value.
         *
         * @param {Object} event    any data about the event
         * @param {Object} ui       data of the autocomplete input field
         * @param {Object} ui.item  the data of the selected item (label and
         *                          value)
         * @return {Boolean}        always <code>false</code> to suppress
         *                          further handling of the event
         * @private
         */
        _onFocusItem: function (event, ui) {
            $(event.target).val(ui.item.label);
            return false;
        },

        /**
         * Called if an item from the autocomplete selector was chosen. The
         * method stores the selected label and value in the properties
         * {@link FixedSelAutocomplete#oldLabel} and
         * {@link FixedSelAutocomplete#oldValue} as well as in the input
         * fields.
         *
         * @param {Object} event    any data about the event
         * @param {Object} ui       data of the autocomplete input field
         * @param {Object} ui.item  the data of the selected item (label and
         *                          value)
         * @return {Boolean}        always <code>false</code> to suppress
         *                          further handling of the event
         * @private
         */
        _onSelect: function (event, ui) {
            var item = ui.item,
                s;

            s = item.label;
            this._oldLabel = s;
            $(event.target).val(s);
            s = item.value;
            this._oldValue = s;
            this._$valueInput.val(s);

            if (this._onSelectFunc) {
                this._onSelectFunc.call(this, item.value, item.label);
            }

            return false;
        },

        /**
         * Sends a request to the server using the entered search term as base
         * for querying particular items.
         *
         * @param {Object} request         information about the search request
         * @param {String} request.term    the entered search term
         * @param {Function} response      the function which should be called
         *                                 to render the received labels and
         *                                 values
         * @private
         */
        _sendFindRequest: function (request, response) {
            var p = this._parameters,
                params = {};

            if (p) {
                params = $.isFunction(p) ? p.call(this) : p;
            }
            $.extend(params, { name: request.term });

            $.ajax({
                context: this, data: params, dataType: "json",
                /** @ignore */
                success: function (data) {
                    this._handleResponse(response, data);
                },
                url: this._findUrl
            });
        }
    };
    SPRINGCRM.FixedSelAutocomplete = FixedSelAutocomplete;


    /**
     * Creates a new handling mechanism for address fields. Usually, there are
     * two address field blocks side by side which content is interchangeable.
     *
     * @class                                   Represents a handling mechanism
     *                                          for address field blocks.
     * @constructor
     * @param {Object} config                   the configuration data
     * @param {String} config.leftPrefix        the prefix of the fields in the
     *                                          left address block
     * @param {String} config.rightPrefix       the prefix of the fields in the
     *                                          right address block
     * @param {String} [config.retrieveOrgUrl]  the URL used to retrieve the
     *                                          data of an organization; may be
     *                                          <code>null</code>
     * @param {String} [config.orgFieldId]      the ID of the field used to
     *                                          obtain the selected
     *                                          organization; may be
     *                                          <code>null</code>
     * @returns {Object}                        the generated address fields
     *                                          object
     */
    AddrFields = function (config) {

        /* handle function call without new */
        if (!(this instanceof AddrFields)) {
            return new AddrFields(config);
        }


        //-- Instance variables -----------------

        /**
         * The prefix of the fields in the left address block.
         *
         * @type String
         */
        this._leftPrefix = config.leftPrefix;

        /**
         * The prefix of the fields in the right address block.
         *
         * @type String
         */
        this._rightPrefix = config.rightPrefix;

        /**
         * The URL used to retrieve the data of an organization; may be
         * <code>null</code>.
         *
         * @type String
         */
        this._retrieveOrgUrl = config.retrieveOrgUrl;

        /**
         * The ID of the field used to obtain the selected organization.
         *
         * @type String
         * @default "organization-id"
         */
        this._orgFieldId = config.orgFieldId || "organization-id";

        /**
         * The menu which belongs to the left address block.
         *
         * @type JQueryObject
         */
        this._$leftMenu = $(".left-address .menu");

        /**
         * The menu which belongs to the right address block.
         *
         * @type JQueryObject
         */
        this._$rightMenu = $(".right-address .menu");
    };

    /**
     * The names of the fields the addresses consist of.
     *
     * @type Array
     * @constant
     * @default [ "Street", "PoBox", "PostalCode", "Location", "State", "Country" ]
     */
    AddrFields.ADDRESS_FIELDS = [
        "Street", "PoBox", "PostalCode", "Location", "State", "Country"
    ];

    AddrFields.prototype = {

        //-- Public methods ---------------------

        addMenuItemCopy: function (left, text) {
            var f = left ? this.copyToLeft : this.copyToRight;

            $("<li/>", {
                    text: text,
                    click: $.proxy(f, this)
                })
                .appendTo(this._getMenu(left));
        },

        addMenuItemLoadFromOrganization: function (left, text, orgPrefix) {
            $("<li/>", {
                    text: text,
                    click: $.proxy(function () {
                            if (left) {
                                this.loadFromOrganizationToLeft(orgPrefix);
                            } else {
                                this.loadFromOrganizationToRight(orgPrefix);
                            }
                        }, this)
                })
                .appendTo(this._getMenu(left));
        },

        /**
         * Copies the address from the right block to the left one.
         */
        copyToLeft: function () {
            this._copyAddress(this._rightPrefix, this._leftPrefix);
        },

        /**
         * Copies the address from the left block to the right one.
         */
        copyToRight: function () {
            this._copyAddress(this._leftPrefix, this._rightPrefix);
        },

        /**
         * Retrieves the address with the given prefix from the organization
         * and fills the left address block.
         *
         * @param {String} orgPrefix    the prefix in the JSON data object
         *                              containing the address of the
         *                              organization which will be filled into
         */
        loadFromOrganizationToLeft: function (orgPrefix) {
            this._loadFromOrganization(this._leftPrefix, orgPrefix);
        },

        /**
         * Retrieves the address with the given prefix from the organization
         * and fills the right address block.
         *
         * @param {String} orgPrefix    the prefix in the JSON data object
         *                              containing the address of the
         *                              organization which will be filled into
         */
        loadFromOrganizationToRight: function (orgPrefix) {
            this._loadFromOrganization(this._rightPrefix, orgPrefix);
        },


        //-- Non-public methods -----------------

        /**
         * Copies the contents of the address fields from one side to the
         * other.
         *
         * @param {String} fromPrefix   the field prefix where to copy from
         * @param {String} toPrefix     the field prefix where to copy to
         * @private
         */
        _copyAddress: function (fromPrefix, toPrefix) {
            var addrFields = AddrFields.ADDRESS_FIELDS,
                f,
                gaf = this._getField,
                i = -1,
                msg = SPRINGCRM.getMessage("copyAddressWarning_" + toPrefix),
                n = addrFields.length;

            if (!this._doesExist(toPrefix) || window.confirm(msg)) {
                while (++i < n) {
                    f = addrFields[i];
                    gaf(toPrefix, f).val(gaf(fromPrefix, f).val());
                }
            }
        },

        /**
         * Checks whether or not the address block with the given prefix is
         * fully or partially filled out.
         *
         * @param {String} prefix   the prefix of the address fields to test
         * @returns {Boolean}       <code>true</code> if the address fields in
         *                          the block were filled out;
         *                          <code>false</code> otherwise
         * @protected
         */
        _doesExist: function (prefix) {
            var addrFields = AddrFields.ADDRESS_FIELDS,
                i = -1,
                n = addrFields.length,
                res = false;

            while (++i < n) {
                res = res || this._getField(prefix, addrFields[i]).val() !== "";
            }
            return res;
        },

        /**
         * Fills the address data into the address block with the given prefix.
         * If the address block is fully or partially filled out a warning is
         * displayed.
         *
         * @param {String} prefix       the prefix of the address fields which
         *                              are to fill out
         * @param {String} orgPrefix    the prefix used to extract the address
         *                              values from the JSON data
         * @param {Object} data         the address data
         * @private
         */
        _fillAddress: function (prefix, orgPrefix, data) {
            var addrFields = AddrFields.ADDRESS_FIELDS,
                f,
                i = -1,
                msg = SPRINGCRM.getMessage("copyAddressWarning_" + prefix),
                n = addrFields.length;

            if (!this._doesExist(prefix) || window.confirm(msg)) {
                while (++i < n) {
                    f = addrFields[i];
                    this._getField(prefix, f).val(data[orgPrefix + f]);
                }
            }
        },

        /**
         * Gets the address field with the given prefix and name.
         *
         * @param {String} prefix   the given prefix
         * @param {String} name     the given name
         * @returns {JQueryObject}  the address field
         * @protected
         */
        _getField: function (prefix, name) {
            return $("#" + prefix + name);
        },

        /**
         * Gets the <code>&lt;ul></code> of either the left or right menu.
         *
         * @param {Boolean} left    <code>true</code> if the left menu is to
         *                          return; <code>false</code> otherwise
         * @return {JQueryObject}   the menu unordered list
         * @protected
         */
        _getMenu: function (left) {
            var $menu = left ? this._$leftMenu : this._$rightMenu,
                $ul;

            $ul = $menu.find("ul");
            if ($ul.length === 0) {
                $ul = $("<ul/>").appendTo($menu);
            }
            return $ul;
        },

        /**
         * Retrieves the data of the organization which is selected in the
         * field with ID <code>organization-sel</code> and stores the address
         * in the address block with the given prefix.
         *
         * @param {String} prefix       the prefix of the address fields which
         *                              are to fill out
         * @param {String} orgPrefix    the prefix used to extract the address
         *                              values from the JSON data
         * @private
         */
        _loadFromOrganization: function (prefix, orgPrefix) {
            var url = this._retrieveOrgUrl;

            if (url) {
                $.ajax({
                    url: url, dataType: "json",
                    data: { id: $("#" + this._orgFieldId).val() },
                    context: {
                        instance: this, orgPrefix: orgPrefix, prefix: prefix
                    },
                    success: function (data) {
                        this.instance._fillAddress(
                            this.prefix, this.orgPrefix, data
                        );
                    }
                });
            }
        }
    };
    SPRINGCRM.AddrFields = AddrFields;


    /**
     * Creates a application adapted lightbox instance.
     *
     * @param {Object} config           the lightbox configuration data; see
     *                                  <a href="http://leandrovieira.com/projects/jquery/lightbox/" target="_blank">LightBox homepage</a>
     *                                  for more details
     * @param {String} config.imgDir    the path where all the images for the
     *                                  buttons of the lightbox window are
     *                                  stored
     * @returns {Object}                the created LightBox instance
     */
    LightBox = function (config) {
        var imgDir;

        /* handle function call without new */
        if (!(this instanceof LightBox)) {
            return new LightBox(config);
        }

        config = config || {};
        imgDir = config.imgDir || "img/lightbox";
        config.imageLoading = config.imageLoading || imgDir + "/lightbox-ico-loading.gif";
        config.imageBtnClose = config.imageBtnClose || imgDir + "/lightbox-btn-close.gif";
        config.imageBtnPrev = config.imageBtnPrev || imgDir + "/lightbox-btn-prev.gif";
        config.imageBtnNext = config.imageBtnNext || imgDir + "/lightbox-btn-next.gif";
        this.config = config;
    };

    LightBox.prototype = {

        //-- Public methods ---------------------

        /**
         * Activates the lightbox for the given selector.
         *
         * @param {String|JQueryObject} selector    either a selector or a
         *                                          jQuery object for which the
         *                                          lightbox should be
         *                                          activated
         * @returns {Object}                        this LightBox object
         */
        activate: function (selector) {
            if (typeof(selector) === "string") {
                selector = $(selector);
            }
            selector.lightBox(this.config);
            return this;
        }
    };
    SPRINGCRM.LightBox = LightBox;


    /**
     * Creates a set of list which load their content from the remote server.
     *
     * @param {String} returnUrl        the URL which is appended to links in
     *                                  the body of the table in order to lead
     *                                  the user back to the current page
     * @param {String} [sel]            the selector which indicates the
     *                                  containers that load their content from
     *                                  the server
     * @param {String} [containerSel]   the selector which selects the area of
     *                                  the container where the loaded content
     *                                  is to display
     * @returns {Object}                the created RemoteList instance
     */
    RemoteList = function (returnUrl, sel, containerSel) {

        /* handle function call without new */
        if (!(this instanceof RemoteList)) {
            return new RemoteList(sel, containerSel);
        }

        sel = sel || "[itemtype=http://www.amc-world.de/data/xml/springcrm/list-vocabulary][itemscope]";

        /**
         * The containers which load their content from remote servers.
         *
         * @type JQueryObject
         * @default $("[itemtype=http://www.amc-world.de/data/xml/springcrm/list-vocabulary][itemscope]")
         */
        this._$containers = $(sel);

        /**
         * The selector which selects the area of the container where the
         * loaded content is to display.
         *
         * @type String
         * @default ".fieldset-content"
         */
        this._containerSel = containerSel || ".fieldset-content";

        /**
         * The URL which is appended to links in the body of the table in order
         * to lead the user back to the current page.
         *
         * @type String
         */
        this._returnUrl = returnUrl;
    };

    RemoteList.prototype = {

        //-- Public methods ---------------------

        /**
         * Initializes all containers which load their content from remote
         * server.
         */
        initialize: function () {
            var instance = this;

            this._$containers
                .each(function () {
                    instance._initContainer(this);
                });
        },


        //-- Non-public methods -----------------

        /**
         * Initializes the given container.
         *
         * @param {Object} container    the given container
         * @private
         */
        _initContainer: function (container) {
            var $container = $(container),
                url;

            url = $container.find("link")
                .attr("href");
            this._loadContent($container, url);
        },

        /**
         * Loads the HTML content from the given URL and places it in the
         * stated container. Furthermore, the method rewrites all links in the
         * returned HTML content in order to load them via AJAX.
         *
         * @param {JQueryObject} $container the given container
         * @param {String} url              the URL to load
         * @private
         */
        _loadContent: function ($container, url) {
            var instance = this;

            $container.find(this._containerSel)
                .load(
                    url,
                    function () {
                        var $c = $container;

                        $c.find("thead a")
                            .add(".paginator a")
                            .click(function (event) {
                                var url = $(this).attr("href");

                                event.preventDefault();
                                instance._loadContent($container, url);
                            });
                        $c.find("tbody .button")
                            .each(function () {
                                var $this = $(this),
                                    url;

                                url = $this.attr("href");
                                url += ((url.indexOf("?") < 0) ? "?" : "&") +
                                    "returnUrl=" + instance._returnUrl;
                                $this.attr("href", url);
                            });
                        $c.find(".delete-btn")
                            .click(Page.prototype._onClickDeleteBtn);
                    }
                );
        }
    };
    SPRINGCRM.RemoteList = RemoteList;


    //== jQuery extensions ======================

    /**
     * @name    jQuery
     * @class   Extends the jQuery class.
     */
    $.extend({

        /**
         * Formats the given number as currency, that is with two decimal
         * digits. For example, the number 1423 is formatted as "1.423,00".
         *
         * @name                jQuery#formatCurrency
         * @param {Number} x    the given number
         * @returns {String}    the formatted number
         * @function
         */
        formatCurrency: function (x) {
            return $.formatNumber(x, 2);
        },

        /**
         * Formats the given date with either the given format or the localized
         * date and time format as specified in the messages
         * <code>dateFormat</code> and <code>timeFormat</code>.
         *
         * @name                    jQuery#formatDate
         * @param {Date} [d]        the given date; defaults to the current
         *                          date and time
         * @param {String} [type]   the type of string which is to parse;
         *                          possible values are "date", "time", and
         *                          "datetime". Defaults to "datetime". The
         *                          parameter is ignored if parameter "format"
         *                          is set.
         * @param {String} [format] uses the given format for parsing; this
         *                          parameter takes precedence over "type"
         * @returns {String}        the formatted date
         * @function
         */
        formatDate: function (d, type, format) {
            var delimiter,
                f = function (x) {
                    var s = x.toFixed();
                    if (s.length < 2) {
                        s = "0" + s;
                    }
                    return s;
                },
                regexp = /\W/,
                res = "",
                token;

            d = d || new Date();
            type = type || "datetime";
            if (!format) {
                format = "";
                if ((type === "date") || (type === "datetime")) {
                    format += SPRINGCRM.getMessage("dateFormat");
                }
                if (type === "datetime") {
                    format += " ";
                }
                if ((type === "time") || (type === "datetime")) {
                    format += SPRINGCRM.getMessage("timeFormat");
                }
            }

            while (format) {
                if (regexp.test(format)) {
                    token = RegExp.leftContext;
                    delimiter = RegExp.lastMatch;
                    format = RegExp.rightContext;
                } else {
                    token = format;
                    delimiter = "";
                    format = "";
                }
                switch (token) {
                case 'd':
                    res += d.getDate();
                    break;
                case 'dd':
                    res += f(d.getDate());
                    break;
                case 'M':
                    res += d.getMonth() + 1;
                    break;
                case 'MM':
                    res += f(d.getMonth() + 1);
                    break;
                case 'y':
                    res += d.getYear();
                    break;
                case 'yy':
                    res += f(d.getYear() - 100);
                    break;
                case 'yyy':
                case 'yyyy':
                    res += String(d.getFullYear());
                    break;
                case 'H':
                    res += d.getHours();
                    break;
                case 'HH':
                    res += f(d.getHours());
                    break;
                case 'm':
                    res += d.getMinutes();
                    break;
                case 'mm':
                    res += f(d.getMinutes());
                    break;
                }
                res += delimiter;
            }

            return res;
        },

        /**
         * Formats the given number with the given precision.
         *
         * @name                jQuery#formatNumber
         * @param {Number} x    the given number
         * @param {Number} [n]  the precision; if not set the precision remains
         *                      as it is
         * @returns {String}    the formatted number
         * @function
         */
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

        /**
         * Parses the given string as a date and time value with either the
         * given format or the localized date and time format as specified in
         * the messages <code>dateFormat</code> and <code>timeFormat</code>.
         *
         * @name                        jQuery#parseDate
         * @param {String} s            the given string containing date or
         *                              time parts or both; if it contains date
         *                              and time parts they must be separated
         *                              by a space character
         * @param {String} [type]       the type of string which is to parse;
         *                              possible values are "date", "time", and
         *                              "datetime". Defaults to "datetime". The
         *                              parameter is ignored if parameter
         *                              "format" is set.
         * @param {String} [format]     uses the given format for parsing; this
         *                              parameter takes precedence over "type"
         * @param {Number} [baseYear]   the year which acts as limit for year
         *                              specifications without century: years
         *                              before the base year are treated as
         *                              after 2000, all other years before
         *                              2000; defaults to 35
         * @returns {Date}              the parsed date; <code>null</code> if
         *                              the given string was empty
         * @throws Error                if the given string does not represent
         *                              a valid date according to the specified
         *                              or default format
         * @function
         */
        parseDate: function (s, type, format, baseYear) {
            var day = 1,
                hours = 0,
                minutes = 0,
                month = 0,
                part = "",
                pos = 0,
                regexp = /\W/,
                token,
                year = 1970;

            if (!$.trim(s)) {
                return null;
            }
            type = type || "datetime";
            if (!format) {
                format = "";
                if ((type === "date") || (type === "datetime")) {
                    format += SPRINGCRM.getMessage("dateFormat");
                }
                if (type === "datetime") {
                    format += " ";
                }
                if ((type === "time") || (type === "datetime")) {
                    format += SPRINGCRM.getMessage("timeFormat");
                }
            }
            baseYear = baseYear || 35;

            while (format) {
                if (regexp.test(format)) {
                    token = RegExp.leftContext;
                    pos = s.indexOf(RegExp.lastMatch);
                    if ((pos < 0) || (pos + 1 >= s.length)) {
                        throw new Error("Invalid date or time format: " + s);
                    }
                    part = s.substring(0, pos);
                    s = s.substring(pos + 1);
                    format = RegExp.rightContext;
                } else {
                    token = format;
                    part = s;
                    format = "";
                }

                switch (token) {
                case 'd':
                case 'dd':
                    day = parseInt(part, 10);
                    break;
                case 'M':
                case 'MM':
                    month = parseInt(part, 10) - 1;
                    break;
                case 'y':
                case 'yy':
                case 'yyy':
                case 'yyyy':
                    year = parseInt(part, 10);
                    if (year < 100) {
                        year += (year < baseYear) ? 2000 : 1900;
                    }
                    break;
                case 'H':
                case 'HH':
                    hours = parseInt(part, 10);
                    break;
                case 'm':
                case 'mm':
                    minutes = parseInt(part, 10);
                    break;
                }
            }

            if (isNaN(year) || isNaN(month) || isNaN(day) || isNaN(hours)
                || isNaN(minutes))
            {
                throw new Error("Invalid date or time format: " + s);
            }
            return new Date(year, month, day, hours, minutes, 0, 0);
        },

        /**
         * Parses the given localized number string.
         *
         * @name                jQuery#parseNumber
         * @param {String} s    the given string
         * @returns {Number}    the parsed number; <code>NaN</code> if the
         *                      given string is not a number
         * @function
         */
        parseNumber: function (s) {
            return (s === "") ? 0
                    : parseFloat(s.replace(/\./, "").replace(/,/, "."));
        }
    });
    $.fn.extend({

        /**
         * Disables the elements in the jQuery object.
         *
         * @name                    jQuery#disable
         * @returns {JQueryObject}  this jQuery object
         * @function
         * @since                   0.9.12
         */
        disable: function () {
            return $(this).each(function () {
                $(this).attr("disabled", "disabled")
                    .addClass("disabled");
            });
        },

        /**
         * Enables the elements in the jQuery object.
         *
         * @name                    jQuery#enable
         * @returns {JQueryObject}  this jQuery object
         * @function
         * @since                   0.9.12
         */
        enable: function () {
            return $(this).each(function () {
                $(this).removeAttr("disabled")
                    .removeClass("disabled");
            });
        },

        /**
         * Enables or disables the elements in the jQuery object depending on
         * the given state.
         *
         * @name                                jQuery#enable
         * @param {Boolean|String|JQueryObject} either a boolean value
         *                                      indicating whether or not to
         *                                      enable the elements, a string
         *                                      representing a selector of a
         *                                      checkbox, or a jQueryObject
         *                                      representing a checkbox which
         *                                      checked state is obtained
         * @returns {JQueryObject}              this jQuery object
         * @function
         * @since                               0.9.12
         */
        toggleEnable: function (enable) {
            var b;

            if (enable.constructor === Boolean) {
                b = enable;
            } else {
                b = $(enable).is(":checked");
            }
            if (b) {
                $(this).enable();
            } else {
                $(this).disable();
            }
        }
    });


    //== Main ===================================

    page = new Page();
    page.init();
    SPRINGCRM.page = page;

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
}(this, SPRINGCRM, jQuery));

// vim:set ts=4 sw=4 sts=4:
