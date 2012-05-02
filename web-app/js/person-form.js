/*
 * person-form.js
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
    var $picture,
        jQuery = $,
        onClickDeleteDocumentLink;

    onClickDeleteDocumentLink = function () {
        var $ = jQuery;

        $("#pictureRemove").val(1);
        $(".document-preview").remove();
        $(".document-preview-links").remove();
    };

    $("#organization").autocompleteex();

    $picture = $("#picture");
    $picture.lightbox({
            imgDir: $picture.data("img-dir")
        });
    $(".document-delete").wrapInner(
            $('<a href="#">').click(onClickDeleteDocumentLink)
        );

    $("#addresses").addrfields({
            leftPrefix: "mailingAddr",
            menuItems: [
                {
                    action: "loadFromOrganization", propPrefix: "billingAddr",
                    side: "left",
                    text: $L("person.addr.fromOrgBillingAddr")
                },
                {
                    action: "loadFromOrganization", propPrefix: "shippingAddr",
                    side: "left",
                    text: $L("person.addr.fromOrgShippingAddr")
                },
                {
                    action: "copy", side: "left",
                    text: $L("person.mailingAddr.copy")
                },
                {
                    action: "loadFromOrganization", propPrefix: "billingAddr",
                    side: "right",
                    text: $L("person.addr.fromOrgBillingAddr")
                },
                {
                    action: "loadFromOrganization", propPrefix: "shippingAddr",
                    side: "right",
                    text: $L("person.addr.fromOrgShippingAddr")
                },
                {
                    action: "copy", side: "right",
                    text: $L("person.otherAddr.copy")
                }
            ],
            organizationId: "#organization\\.id",
            rightPrefix: "otherAddr"
        });
}(jQuery, $L));
