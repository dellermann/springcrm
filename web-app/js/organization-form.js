/*
 * organization-form.js
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


(function($, $L) {

    "use strict";

    var $recType = $("#recType"),
        initRecTypes,
        onClickRecType;

    initRecTypes = function () {
        var $this = $(this);

        if ($recType.val() & $this.val()) {
            $this.attr("checked", true);
        }
    };

    onClickRecType = function () {
        var $rt = $recType,
            $this = $(this);

        if (this.checked) {
            $rt.val($rt.val() | $this.val());
        } else {
            $rt.val($rt.val() & ~$this.val());
        }
    };

    $("#addresses").addrfields({
            leftPrefix: "billingAddr",
            menuItems: [
                {
                    action: "copy", side: "left",
                    text: $L("organization.billingAddr.copy")
                },
                {
                    action: "copy", side: "right",
                    text: $L("organization.shippingAddr.copy")
                }
            ],
            rightPrefix: "shippingAddr"
        });

    $(".rec-type").click(onClickRecType)
        .each(initRecTypes);
}(jQuery, $L));
