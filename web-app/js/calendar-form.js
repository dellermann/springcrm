/*
 * calendar-form.js
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * @fileOverview    Contains general classes which are used within this
 *                  application.
 * @author          Daniel Ellermann
 * @version         1.0
 */


(function ($, $L) {

    "use strict";

    var REMINDER_SEL_OPTIONS = [
            "1m", "5m", "10m", "15m", "20m", "30m", "45m", "1h", "2h", "3h",
            "4h", "5h", "6h", "8h", "12h", "1d", "2d", "3d", "1w", "2w", "4w"
        ],
        $LANG = $L,
        $tabs = $("#tabs-recurrence-type"),
        computeReminderSelector,
        initRecurrenceTypes,
        initReminders,
        jQuery = $,
        onChangeAllDay,
        onChangeRecurrenceEndType,
        onChangeTab,
        onClickReminderAddBtn = null,
        onClickReminderSelectors = null,
        onLoadedOrganization,
        onSelectOrganization,
        onSubmitForm;


    //-- Functions ------------------------------

    computeReminderSelector = function (value) {
        var $L = $LANG,
            i = -1,
            n,
            o,
            opts = REMINDER_SEL_OPTIONS,
            s = "",
            u = "",
            v;

        n = opts.length;
        while (++i < n) {
            o = opts[i];
            v = parseInt(o, 10);
            switch (o.charAt(o.length - 1)) {
            case "m":
                u = "minute";
                break;
            case "h":
                u = "hour";
                break;
            case "d":
                u = "day";
                break;
            case "w":
                u = "week";
                break;
            }
            if (v > 1) {
                u += "s";
            }
            s += '<option value="' + o + '"' +
                ((o === value) ? ' selected="selected"' : '') + '>' +
                String(v) + ' ' + $L("calendarEvent.reminder.unit." + u) +
                '</option>';
        }
        return '<li><select>' + s + '</select> ' +
            '<a href="#" class="button small red">' +
            $L('calendarEvent.reminder.delete.label') + '</a></li>';
    };

    initRecurrenceTypes = function () {
        var $ = jQuery,
            i = -1,
            n,
            recurType,
            wds;

        recurType =
            Number($("#tabs-recurrence-type input:radio:checked").val());
        if (recurType === 0) {
            $("#recurrence-end").hide();
        } else {
            $("#recurrence-interval-" + recurType)
                .val($("#recurrence-interval").val());
            $("#recurrence-monthDay-" + recurType)
                .val($("#recurrence-monthDay").val());
            $("#recurrence-weekdayOrd-" + recurType)
                .val($("#recurrence-weekdayOrd").val());
            $("#recurrence-month-" + recurType)
                .val($("#recurrence-month").val());

            if ((recurType === 30) || (recurType === 50)
                || (recurType === 70))
            {
                wds = $("#recurrence-weekdays").val().split(/,/);
                n = wds.length;
                if (recurType === 30) {
                    $("#tabs-recurrence-type-30 input:checkbox")
                        .attr("checked", false);
                    while (++i < n) {
                        $("#recurrence-weekdays-30-" + wds[i])
                            .attr("checked", true);
                    }
                } else if (n > 0) {
                    $("#recurrence-weekdays-" + recurType).val(wds[0]);
                }
            }
            $("#recurrence-end").show();
        }
        $tabs.tabs("select", "tabs-recurrence-type-" + recurType);
    };

    initReminders = function () {
        var $ = jQuery,
            i = -1,
            n,
            reminders,
            s = "",
            val = $("#reminders").val();

        if (val !== "") {
            reminders = val.split(/ /);
            n = reminders.length;
            if (n > 0) {
                s += '<ul>';
                while (++i < n) {
                    s += computeReminderSelector(reminders[i]);
                }
                s += '</ul>';
            }
        }
        $("#reminder-selectors").html(s)
            .click(onClickReminderSelectors);
        $("#reminder-add-btn").click(onClickReminderAddBtn);
    };

    onChangeAllDay = function () {
        var $ = jQuery,
            checked = this.checked;

        $("#start-time").toggleEnable(!checked);
        $("#end-time").toggleEnable(!checked);
    };

    onChangeRecurrenceEndType = function () {
        var $ = jQuery;

        switch (this.id) {
        case "recurrence.endType-until":
            $("#recurrence\\.until-date").enable()
                .focus();
            $("#recurrence\\.cnt").disable();
            break;
        case "recurrence.endType-count":
            $("#recurrence\\.cnt").enable()
                .focus();
            $("#recurrence\\.until-date").disable();
            break;
        case "recurrence.endType-none":
            $("#recurrence\\.until-date").disable();
            $("#recurrence\\.cnt").disable();
            break;
        }
    };

    onChangeTab = function (event) {
        var $ = jQuery,
            $target = $(event.target),
            id,
            val;

        if ($target.attr("name") === "recurrence.type") {
            val = Number($target.val());
            $tabs.tabs("select", "tabs-recurrence-type-" + val);
            $("#recurrence-end").toggle(val !== 0);
        } else {
            id = $target.parents(".ui-tabs-panel")
                .attr("id");
            if (id.match(/^tabs-recurrence-type-(\d+)$/)) {
                $("#recurrence-type-" + RegExp.$1).trigger("click");
            }
        }
    };

    onClickReminderAddBtn = function () {
        var $ = jQuery,
            $sel = $("#reminder-selectors"),
            $ul = $sel.find("ul"),
            s = computeReminderSelector();

        if ($ul.length === 0) {
            $ul = $("<ul/>").appendTo($sel);
        }
        $ul.append($(s));
        $("html").scrollTop($(this).position().top);
        return false;
    };

    onClickReminderSelectors = function (event) {
        var $ = jQuery,
            $target,
            target = event.target;

        $target = $(target);
        if ($target.is("a")) {
            $(target).parent()
                .remove();
            $("html").scrollTop($("#reminder-add-btn").position().top);
        }
        return false;
    };

    onLoadedOrganization = function (data) {
        var s = "";

        if (data.shippingAddrStreet) {
            s += data.shippingAddrStreet;
        }
        if (data.shippingAddrPostalCode ||
            data.shippingAddrLocation)
        {
            if (s !== "") {
                s += ", ";
            }
            if (data.shippingAddrPostalCode) {
                s += data.shippingAddrPostalCode + " ";
            }
            if (data.shippingAddrLocation) {
                s += data.shippingAddrLocation;
            }
        }
        $("#location").val(s);
    };

    onSelectOrganization = function (value) {
        var $ = jQuery;

        $.getJSON(
                $("#organization").data("get-url"),
                { id: value },
                onLoadedOrganization
            );
    };

    onSubmitForm = function () {
        var $ = jQuery,
            recurType,
            reminders = [],
            val,
            wds;

        recurType =
            Number($("#tabs-recurrence-type input:radio:checked").val());
        if (recurType > 0) {
            val = $("#recurrence-interval-" + recurType).val();
            $("#recurrence-interval").val(val || 1);
            $("#recurrence-monthDay")
                .val($("#recurrence-monthDay-" + recurType).val());
            $("#recurrence-weekdayOrd")
                .val($("#recurrence-weekdayOrd-" + recurType).val());
            $("#recurrence-month")
                .val($("#recurrence-month-" + recurType).val());
            if (recurType === 30) {
                wds = [];
                $("#tabs-recurrence-type-30 input:checkbox:checked")
                    .each(function () {
                        wds.push(Number($(this).val()));
                    });
                    wds.sort();
                $("#recurrence-weekdays").val(wds.join(","));
            } else if (recurType === 50) {
                $("#recurrence-weekdays")
                    .val($("#recurrence-weekdays-50").val());
            } else if (recurType === 70) {
                $("#recurrence-weekdays")
                    .val($("#recurrence-weekdays-70").val());
            }
        }

        $("#reminder-selectors select").each(function () {
                reminders.push($(this).val());
            });
        $("#reminders").val(reminders.join(" "));
        return true;
    };


    //-- Main -----------------------------------

    /* initialize autocompletion fields */
    $("#organization").autocompleteex({
            select: onSelectOrganization
        });

    /* initialize all-day checkbox */
    $("#allDay").change(onChangeAllDay)
        .triggerHandler("change");

    /* initialize tabs */
    $tabs.tabs()
        .change(onChangeTab);

    /* initialize recurrence controls */
    $("#recurrence-end input[name=recurrence\\.endType]")
        .change(onChangeRecurrenceEndType);
    $("input[name=recurrence\\.endType]:checked").triggerHandler("change");
    initRecurrenceTypes();

    /* initialize submit hook */
    $("#calendarEvent-form").bind("submit", onSubmitForm);

    /* initialize reminders */
    initReminders();

}(jQuery, $L));
