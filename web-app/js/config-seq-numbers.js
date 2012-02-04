(function ($) {

    "use strict";

    var checkAddOrgNumber,
        computeRndNumber,
        onChangeSeqNumberData,
        renderExample = null;

    checkAddOrgNumber = function () {
        var ctrlName = "," + $(this).attr("data-controller-name") + ",",
            ctrlNames = ",quote,salesOrder,invoice,dunning,creditMemo,";

        return ctrlNames.indexOf(ctrlName) >= 0;
    };

    computeRndNumber = function () {
        var diff,
            end,
            start;

        start = parseInt(
                this.find("td[headers*=seq-numbers-header-start-value] input")
                    .val(),
                10
            );
        end = parseInt(
                this.find("td[headers*=seq-numbers-header-end-value] input")
                    .val(),
                10
            );
        if (isNaN(start) || isNaN(end)) {
            return "";
        } else {
            diff = Math.abs(end - start);
            return String(Math.round(Math.random() * diff) + start);
        }
    };

    onChangeSeqNumberData = function (event) {
        var $target = $(event.target),
            $tr = $target.parents("tr");

        renderExample.call($tr);
        if ($tr.attr("data-controller-name") === "organization") {
            $tr.siblings()
                .each(function () {
                    var $this = $(this);

                    if (checkAddOrgNumber.call($this)) {
                        renderExample.call($this);
                    }
                });
        }
    };

    renderExample = function () {
        var $this = $(this),
            s = "",
            suffix;

        s = $this.find("td[headers*=seq-numbers-header-prefix] input")
            .val();
        if (s !== "") {
            s += "-";
        }
        s += computeRndNumber.call($this);
        suffix = $this.find("td[headers*=seq-numbers-header-suffix] input")
            .val();
        if (suffix !== "") {
            s += "-" + suffix;
        }
        if (checkAddOrgNumber.call($this)) {
            s += "-" + computeRndNumber.call(
                    $this.siblings("tr[data-controller-name=organization]")
                );
        }
        $this.find("td[headers*=seq-numbers-header-example]")
            .text(s);
    };

    $("#seq-numbers").change(onChangeSeqNumberData)
        .find("tbody tr")
            .each(renderExample);
}(jQuery));