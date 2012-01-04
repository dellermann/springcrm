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
