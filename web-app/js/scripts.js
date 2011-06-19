(function($) {

var pkg = springcrm;

/* definitions */
pkg.BASE_FONT_SIZE = 11; // px
pkg.NUM_FONT_SIZES = 5;
pkg.ADDRESS_FIELS = [ 
    "Street", "PoBox", "PostalCode", "Location", "State", "Country"
];

/* classes */
pkg.Bootstrap = function Bootstrap() {
	this.$body = $("body");
	this.$document = $(document);
	this.$search = $("#search");
	this.$toolbar = $("#toolbar-container");
	this.initialSearchText = this.$search.val();
	if (this.$toolbar.size()) this.toolbarOffset = this.$toolbar.offset().top;
	this.$spinner = $("#spinner");
	this.timeValues = this._computeTimeValues();
}
pkg.Bootstrap.prototype = {
		
	//-- Public methods ------------------------

	initPage: function() {
		if (this.$toolbar != null) {
			this.$document.scroll(this, this.onScrollDocument);
		}
		this.$search
			.focus(this, function(e) { e.data.swapSearchText(e, true); })
	    	.blur(this, function(e) { e.data.swapSearchText(e, false); })
		$("#quick-access").change(this.onChangeQuickAccess);
		$("#main-menu > li").hover(this.onMainMenuHover);
		$(".header-with-menu .menu").hover(this.onOptionMenuHover);
		$(".date-input-date").change(this.onChangeDateInput)
			.datepicker({
				changeMonth: true, changeYear: true, gotoCurrent: true,
				selectOtherMonths: true, showButtonPanel: true, 
				showOtherMonths: true
			});
		$(".date-input-time").autocomplete({ source: this.timeValues });
		this._renderFontSizeSel();
		this._initAjaxEvents();
	},
	
	onChangeDateInput: function() {
		var $this = $(this);
		var id = $this.attr("id");
		if (id.match(/^([\w-]+)-(date|time)$/)) {
			var baseId = RegExp.$1;
			var partId = RegExp.$2;
			var $otherPartField = 
				$("#" + baseId + "-" + ((partId == "date") ? "time" : "date"));
			var val = "";
			if (partId == "date") {
				val += $this.val();
				if ($otherPartField.length) val += " " + $otherPartField.val();
			} else {
				if ($otherPartField.length) val += $otherPartField.val() + " ";
				val += $this.val();
			}
			$("#" + baseId).val(val);
		}
	},
	
	onChangeFontSize: function(e) {
	    var $target = $(e.target);
	    var bootstrap = e.data;
	    bootstrap.$body
	    	.css("font-size", $target.css("font-size"));
	    $(this).find("li")
	        .removeClass("current");
	    $target.addClass("current");
	},
	
	onChangeQuickAccess: function() {
		var $this = $(this);
		var val = $this.val();
		$this.val("");
		if (val != "") location.href = val;
	},

	onMainMenuHover: function() {
		$(this).find("ul")
			.slideToggle();
	},
	
	onOptionMenuHover: function() {
		$(this).find("ul")
			.slideToggle();
	},
	
	onScrollDocument: function(e) {
		var bootstrap = e.data;
	    if (bootstrap.$document.scrollTop() >= bootstrap.toolbarOffset) {
	    	bootstrap.$toolbar.addClass("fixed");
	    } else {
	    	bootstrap.$toolbar.removeClass("fixed");
	    }
	},
	
	swapSearchText: function(e, dir) {
		var value1 = dir ? this.initialSearchText : "";
		var value2 = dir ? "" : this.initialSearchText;
        var $input = $(e.currentTarget);
        if ($input.val() == value1) $input.val(value2);
	},
	
	
	//-- Non-public methods ---------------------
	
	_computeTimeValues: function() {
		var res = [];
		for (var h = -1; ++h < 24; ) {
			var hh = h + "";
			if (hh.length < 2) hh = "0" + hh;
			res.push(hh + ":00");
			res.push(hh + ":30");
		}
		return res;
	},
	
	_initAjaxEvents: function() {
		this.$spinner
			.ajaxSend(function() { $(this).show(); })
			.ajaxComplete(function() { $(this).hide(); });
	},

	_renderFontSizeSel: function() {
		var offset = Math.floor(pkg.NUM_FONT_SIZES / 2);
		var currentSize = parseInt(this.$body.css("font-size"));
		var s = '<ul id="font-size-sel">';
		for (var i = -1; ++i < pkg.NUM_FONT_SIZES; ) {
		    var size = pkg.BASE_FONT_SIZE - offset + i;
		    s += '<li ';
		    if (size == currentSize) s += 'class="current" ';
		    s += 'style="font-size: ' + size + 'px;">A</li>';
		}
		$("#app-version").after(s + '</ul>');
		$("#font-size-sel").click(this, this.onChangeFontSize);
	}
}

/* static functions */
pkg.copyAddress = function(from, to) {
	var p = pkg;
	var addrFields = p.ADDRESS_FIELS;
	if (!pkg._doesAddressExists(to) 
		|| confirm(p.messages["copyAddressWarning_" + to]))
	{
		for (var i = -1, n = addrFields.length; ++i < n; ) {
			var f = addrFields[i];
			p._getAddressField(to, f).val(p._getAddressField(from, f).val());
		}
	}
}

pkg.onClickSubmit = function(formId) {
	$("#" + formId).submit();
}

pkg.retrieveAddrFromOrganization = function(prefix, orgPrefix, url) {
	$.ajax({
		url: url, dataType: "json",
		data: { id: $("#organization-sel").val() },
		context: { p: pkg, prefix: prefix, orgPrefix: orgPrefix },
		success: function(data) {
			var p = this.p;
			var prefix = this.prefix;
			if (!p._doesAddressExists(prefix)
				|| confirm(p.messages["copyAddressWarning_" + prefix]))
			{
				var orgPrefix = this.orgPrefix;
				var addrFields = p.ADDRESS_FIELS;
				for (var i = -1, n = addrFields.length; ++i < n; ) {
					var f = addrFields[i];
					p._getAddressField(prefix, f).val(data[orgPrefix + f]);
				}
			}
		}
	});
}

/* private static functions */
pkg._getAddressField = function(prefix, name) {
	return $("#" + prefix + name);
}

pkg._doesAddressExists = function(prefix) {
	var p = pkg;
	var addrFields = p.ADDRESS_FIELS;
	var res = false;
	for (var i = -1, n = addrFields.length; ++i < n; ) {
		res |= p._getAddressField(prefix, addrFields[i]).val() != "";
	}
	return res;
}

/* jQuery extensions */
$.extend({
	formatCurrency: function(x) {
		return $.formatNumber(x, 2);
	},
	
	formatNumber: function(x, n) {
		x *= 1;
		var s = (n == null) ? x.toString() : x.toFixed(n);
		s = s.replace(/\./, ",");
		var i = s.lastIndexOf(",") - 3;
		while (i > 0) {
			s = s.substring(0, i) + "." + s.substring(i);
			i -= 3;
		}
		return s;
	},
	
	parseNumber: function(s) {
		return (s == "") ? 0 
				: parseFloat(s.replace(/\./, "").replace(/,/, "."));
	}
});

/* main */
pkg.bootstrap = new pkg.Bootstrap();
pkg.bootstrap.initPage();

/* TODO: handle info boxes in content tables */
/*
$(".content-table").mouseover(function(e) {
        var target = e.target;
        if (target.tagName == "a") {
            $(target).siblings(".info-box")
                .show();
        }
    });
*/

})(jQuery);

// vim:set ts=4 sw=4 sts=4:
