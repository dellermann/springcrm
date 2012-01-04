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
            url = $phone.attr("data-load-person-phone-numbers-url");
            data.id = $personId.val();
        } else if ($orgId.val()) {
            url = $phone.attr("data-load-organization-phone-numbers-url");
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
