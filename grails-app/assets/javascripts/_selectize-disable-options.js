/**
 * Plugin: "disable_options" (selectize.js)
 * Copyright (c) 2013 Mondo Robot & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @authors Jake Myers <jmyers0022@gmail.com>, Vaughn Draughon <vaughn@rocksolidwebdesign.com>
 */

 Selectize.define('disable_options', function(options) {
  var self = this;

  options = $.extend({
    'disableOptions': []
  }, options);

  self.onFocus = (function() {
    var original = self.onFocus;

    return function() {
      var disableOptions = {};

      original.apply(this, arguments);

      $.extend(
        disableOptions,
        options.disableOptions, 
        this.plugins.settings.disable_options.disableOptions
      );
      $.each(disableOptions, function(index, option) {
        self.$dropdown_content.find('[data-value="' + String(option) + '"]').addClass('option-disabled');
      });
    };
  })();

  self.onOptionSelect = (function() {
    var original = self.onOptionSelect;

    return function(e) {
      var value, $target, $option;

      if (e.preventDefault) {
        e.preventDefault();
        e.stopPropagation();
      }

      $target = $(e.currentTarget);

      if ($target.hasClass('option-disabled')) {
        return;
      } else if ($target.hasClass('create')) {
        self.createItem();
      } else {
        value = $target.attr('data-value');
        if (value) {
          self.lastQuery = null;
          self.setTextboxValue('');
          self.addItem(value);
          if (!self.settings.hideSelected && e.type && /mouse/.test(e.type)) {
            self.setActiveOption(self.getOption(value));
          }
        }

        self.blur();
      }
      return original.apply(this, arguments);
    };
  })();
});

