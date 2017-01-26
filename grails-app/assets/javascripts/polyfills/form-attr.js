/*
 * Copyright (c) ChiChou
 * http://stackoverflow.com/a/26696165/214325
 */

(function($) {

    /**
     * Polyfill for HTML5 form attr
     */
    var isIE11, sampleElement;
    sampleElement = $('[form]').get(0);
    isIE11 = !window.ActiveXObject && 'ActiveXObject' in window;
    if (sampleElement && window.HTMLFormElement &&
        sampleElement.form instanceof HTMLFormElement && !isIE11)
    {
        return;
    }

    /**
     * Append a field to a form
     */
    $.fn.appendField = function(data) {
        var $form;
        if (!this.is('form')) {
            return;
        }
        if (!$.isArray(data) && data.name && data.value) {
            data = [data];
        }
        $form = this;
        $.each(data, function(i, item) {
            $('<input/>')
                .attr('type', 'hidden')
                .attr('name', item.name)
                .val(item.value)
                .appendTo($form);
        });
        return $form;
    };

    /**
     * Find all input fields with form attribute point to jQuery object
     */
    $('form[id]')
        .submit(function() {
            var data, id;

            id = $(this).attr('id');
            data = $('[form=' + id + ']').serializeArray();
            $(this).appendField(data);
        }).each(function() {
            var $fields, form, id;

            form = this;
            id = $(this).attr('id');
            $fields = $('[form=' + id + ']');
            $fields
                .filter('button, input')
                .filter('[type=reset],[type=submit]')
                .click(function() {
                    var type;
                    type = this.type.toLowerCase();
                    if (type === 'reset') {
                        form.reset();
                        $fields.each(function() {
                            this.value = this.defaultValue;
                            this.checked = this.defaultChecked;
                        }).filter('select').each(function() {
                            $(this).find('option').each(function() {
                                this.selected = this.defaultSelected;
                            });
                        });
                    } else if (type.match(/^submit|image$/i)) {
                        $(form).appendField({
                            name: this.name,
                            value: this.value
                        }).submit();
                    }
                });
        });
})(jQuery);
