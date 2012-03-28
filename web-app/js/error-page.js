/*
 * error-page.js
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

    var FIELDS = ["name", "email", "description"],
        $form = $("#bugreport-form"),
        $reportData = $("#report-data"),
        origText = null,
        rewriteXml,
        submitForm;

    rewriteXml = function() {
        var f,
            fields = FIELDS,
            i = -1,
            n = fields.length,
            text = origText,
            value;

        while (++i < n) {
            f = fields[i];
            value = $($form[0].elements[f]).val()
                .replace("&", "&amp;")
                .replace("<", "&lt;");
            text = text.replace("%" + f + "%", value);
        }
        $reportData.text(text);
        return text;
    };

    submitForm = function() {
        var $f = $form,
            xml = rewriteXml();

        $.ajax({
            data: {xml: xml},
            dataType: "html",
            success: function(html) {
                $f.replaceWith(html);
            },
            url: $f.attr("data-report-error-url")
        });
    };

    origText = $reportData.text();
    $("#accordion").accordion({ active: 2 });
    $form.change(rewriteXml)
        .find("button")
            .click(submitForm);
    rewriteXml();
}(jQuery));
