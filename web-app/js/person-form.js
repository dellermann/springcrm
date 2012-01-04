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
            imgDir: $picture.attr("data-img-dir")
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
