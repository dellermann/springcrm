/*
 * call-form.js
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


(function ($) {

    "use strict";

    var $organizationId = $("#organization\\.id"),
        $phone = $("#phone"),
        onLoadedPhoneNumbers,
        onLoadPhoneNumbers,
        phoneNumbers = null;

    onLoadedPhoneNumbers = function (data, term) {
        var d,
            i = -1,
            l = [],
            n = data.length,
            respData = [],
            val;

        while (++i < n) {
            d = data[i];
            if (d) {
                val = d.toLowerCase();
                if (val) {
                    l.push(val);
                    if (val.indexOf(term) >= 0) {
                        respData.push(val);
                    }
                }
            }
        }
        phoneNumbers = l;
        return respData;
    };

    onLoadPhoneNumbers = function (request, response) {
        var $orgId = $organizationId,
            $personId = $("#person\\.id"),
            data = {},
            term = request.term.toLowerCase(),
            url = null;

        if ($personId.val()) {
            url = $phone.data("load-person-phone-numbers-url");
            data.id = $personId.val();
        } else if ($orgId.val()) {
            url = $phone.data("load-organization-phone-numbers-url");
            data.id = $orgId.val();
        }
        if (!phoneNumbers) {
            if (url) {
                $.getJSON(url, data, function (data) {
                    response(onLoadedPhoneNumbers(data, term));
                });
            }
        } else {
            response($.grep(
                phoneNumbers, function (val) {
                    return val.indexOf(term) >= 0;
                }
            ));
        }
    };

    $("#organization").autocompleteex({
            select: function () {
                phoneNumbers = undefined;
            }
        });
    $("#person").autocompleteex({
            loadParameters: function () {
                return { organization: $organizationId.val() };
            },
            select: function () {
                phoneNumbers = undefined;
            }
        });
    $phone.autocomplete({
            source: onLoadPhoneNumbers
        });

}(jQuery));
