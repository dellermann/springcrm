/**
 * 
 */

(function($) {

var pkg = springcrm;

/* classes */
pkg.InvoicingItems = function InvoicingItems(tableId, formName, imgPath,
											 productListUrl,
											 serviceListUrl) {
	this.tableId = tableId;
	this.formName = formName;
	this.imgPath = imgPath;
	this.productListUrl = productListUrl;
	this.serviceListUrl = serviceListUrl;
	this.$table = $("#" + tableId);
	this.$subTotal = $("#invoicing-items-subtotal");
	this.$discountPercent = $("#discountPercent");
	this.$discountFromPercent = $("#invoicing-items-discount-from-percent");
	this.$discountAmount = $("#discountAmount");
	this.$shippingCosts = $("#shippingCosts");
	this.$adjustment = $("#adjustment");
	this.$total = $("#invoicing-items-total");
}
pkg.InvoicingItems.prototype = {
	addInvoicingItem: function(e) {
		var ii = e.data;
		var table = ii.tableId;
		var imgPath = ii.imgPath;
		var $tbody = $("#invoicing-items");
		var pos = $tbody.find("tr").length;
		var s = '<tr><td headers="' + table + 
			'-pos" class="invoicing-items-pos">' + (pos + 1) + 
			'.</td><td headers="' + table + 
			'-number" class="invoicing-items-number">' +
			'<input type="text" name="items[' + pos + 
			'].number" size="10" /></td><td headers="' + table + 
			'-quantity" class="invoicing-items-quantity">' +
			'<input type="text" name="items[' + pos + 
			'].quantity" size="4" /></td><td headers="' + table + 
			'-unit" class="invoicing-items-unit">' +
			'<input type="text" name="items[' + pos + 
			'].unit" size="5" /></td><td headers="' + table +
			'-name" class="invoicing-items-name">' +
			'<input type="text" name="items[' + pos + 
			'].name" size="28" />&nbsp;<a href="javascript:void 0;" ' +
			'class="select-btn-products"><img src="' + imgPath + 
			'/products.png" alt="' + pkg.messages["productSel"] + '" title="' + 
			pkg.messages["productSel"] + 
			'" width="16" height="16" style="vertical-align: middle;" /></a>' +
			'&nbsp;<a href="javascript:void 0;" class="select-btn-services">' +
			'<img src="' + imgPath + '/services.png" alt="' + 
			pkg.messages["serviceSel"] + '" title="' + 
			pkg.messages["serviceSel"] + 
			'" width="16" height="16" style="vertical-align: middle;" /></a>' +
			'<br /><textarea name="items[' + pos +
			'].description" cols="30" rows="3"></textarea></td><td headers="' + 
			table + '-unit-price" class="invoicing-items-unit-price">' +
			'<input type="text" name="items[' + pos + 
			'].unitPrice" size="8" value="0,00" class="currency" />&nbsp;€</td>' + 
			'<td headers="' + table + 
			'-total" class="invoicing-items-total">' +
			'<span class="value">0,00</span>&nbsp;€</td>' + 
			'<td headers="' + table + 
			'-tax" class="invoicing-items-tax">' +
			'<input type="text" name="items[' + pos + 
			'].tax" size="4" />&nbsp;%</td>' +
			'<td class="invoicing-items-buttons">' +
			'<a href="javascript:void 0;" class="up-btn"><img src="' + 
			imgPath + '/up.png" alt="' + pkg.messages["upBtn"] + '" title="' + 
			pkg.messages["upBtn"] + '" width="16" height="16" /></a>' +
			'<a href="javascript:void 0;" class="down-btn"><img src="' + 
			imgPath + '/down.png" alt="' + pkg.messages["downBtn"] + '" title="' + 
			pkg.messages["downBtn"] + '" width="16" height="16" /></a>' +
			'<a href="javascript:void 0;" class="remove-btn"><img src="' + 
			imgPath + '/remove.png" alt="' + pkg.messages["removeBtn"] + '" title="' + 
			pkg.messages["removeBtn"] + 
			'" width="16" height="16" /></a></td></tr>';
		$tbody.append(s);
	},
	
	computeFooterValues: function() {
		var subTotal = 0;
		$("#invoicing-items .invoicing-items-total .value").each(function() {
				subTotal += $.parseNumber($(this).text());
			});
		this.$subTotal.text($.formatCurrency(subTotal));
		
		var discountPercent = $.parseNumber(this.$discountPercent.val());
		var discount = subTotal * discountPercent / 100;
		this.$discountFromPercent.text($.formatCurrency(discount));
		discount += $.parseNumber(this.$discountAmount.val());
		
		var shippingCosts = $.parseNumber(this.$shippingCosts.val());
		var adjustment = $.parseNumber(this.$adjustment.val());
		var total = subTotal - discount + shippingCosts + adjustment;
		this.$total.text($.formatCurrency(total));
	},
		
	init: function() {
		this.$table
			.click(this, this.onClick)
			.change(this, this.onChange)
			.focusin(this, this.onFocusIn)
			.focusout(this, this.onFocusOut);
		$("#add-invoicing-item-btn").click(this, this.addInvoicingItem);
		this.computeFooterValues();
	},

	moveInvoicingItem: function($a, dir) {
		var f = pkg.InvoicingItems._fixInvoicingItemPos;
		var $tr = $a.parents("tr");
		var $allTrs = $tr.parent()
			.children();
		var pos = $allTrs.index($tr);
		if ((dir < 0) && (pos > 0)) {
			$tr.prev()
					.before($tr)
					.each(f)
				.end()
				.each(f);
		} else if (pos < $allTrs.length - 1) {
			$tr.next()
					.after($tr)
					.each(f)
				.end()
				.each(f);
		}
	},
	
	onChange: function(e) {
		var ii = e.data;
		var input = e.target;
		var $input = $(input);
		var name = $input.attr("name");
		var els = input.form.elements;
		var parts = name.match(/^items\[(\d+)\]\.(\w+)$/);
		if (parts) {
			var pos = parts[1];			var field = parts[2];
			var $tr = $input.parents("tr");
			switch (field) {
			case "quantity":
				var qty = $.parseNumber($input.val());
				var unitPrice = $.parseNumber(
					$(els["items[" + pos + "].unitPrice"]).val()
				);
				$tr.find(".invoicing-items-total .value")
					.text($.formatCurrency(qty * unitPrice));
				break;
			case "unitPrice":
				var unitPrice = $.parseNumber($input.val());
				var qty = $.parseNumber(
					$(els["items[" + pos + "].quantity"]).val()
				);
				$tr.find(".invoicing-items-total .value")
					.text($.formatCurrency(qty * unitPrice));
				break;
			} 
		}
		
		ii.computeFooterValues();
	},
	
	onClick: function(e) {
		var ii = e.data;
		var $a = $(e.target).closest("a");
		switch ($a.attr("class")) {
		case "up-btn":
			ii.moveInvoicingItem($a, -1);
			return true;
		case "down-btn":
			ii.moveInvoicingItem($a, 1);
			return true;
		case "remove-btn":
			ii.removeInvoicingItem($a);
			return true;
		case "select-btn-products":
			ii.showInventorySelector($a, "products");
			return true;
		case "select-btn-services":
			ii.showInventorySelector($a, "services");
			return true;
		default:
			return true;
		}
	},
	
	onFocusIn: function(e) {
		var $target = $(e.target);
		if ($target.hasClass("currency")) {
			var val = $.parseNumber($target.val());
			$target.val(val ? $.formatNumber(val, null) : "");
		}
	},
	
	onFocusOut: function(e) {
		var $target = $(e.target);
		if ($target.hasClass("currency")) {
			$target.val($.formatCurrency($.parseNumber($target.val())));
		}
	},

	removeInvoicingItem: function($a) {
		$a.parents("tr")
			.each(function() {
				var $this = $(this);
				var pos = $this.parent()
					.children()
					.index($this) + 1;
				$this.nextAll()
					.each(function(i) {
						$(this).find("td:first-child")
							.text((pos + i) + ".");
					});
			})
			.remove();
	},
	
	showInventorySelector: function($a, type, url) {
		var $tr = $a.parents("tr");
		var pos = $tr.parent()
			.children()
			.index($tr);
		if (!url) {
			url = (type == "products") ? this.productListUrl 
					: this.serviceListUrl;
		}
		this._loadInventorySelector(type, url, pos);
	},
	
	
	//-- Non-public methods ---------------------

	_loadInventorySelector: function(type, url, pos) {
		$.ajax({
			url: url,
			context: { invoicingItems: this, type: type, pos: pos },
			success: function(html) {
				var type = this.type;
				var pos = this.pos;
				$("#inventory-selector-" + type).html(html)
					.unbind("click")
					.click(this.invoicingItems, function(e) {
						var target = e.target;
						if (target.tagName.toLowerCase() != "a") return true;
						var $target = $(target);
						if ($target.hasClass("select-link")) {
							e.data._retrieveInventoryItem(
								type, $target.attr("href"), pos
							);
						} else {
							e.data._loadInventorySelector(
								type, $target.attr("href"), pos
							);
						}
						return false;
					})
					.dialog({ minWidth: 700, minHeight: 400, modal: true });
			}
		});
	},
	
	_retrieveInventoryItem: function(type, url, pos) {
		$.ajax({
			url: url,
			context: { invoicingItems: this, type: type, pos: pos },
			dataType: "json",
			success: function(data) {
				var type = this.type;
				var pos = this.pos;
				var prefix = "items[" + pos + "].";
				var els = document.forms[this.invoicingItems.formName].elements;
				els[prefix + "number"].value = data.fullNumber;
				var item = data.inventoryItem;
				var qty = item.quantity;
				els[prefix + "quantity"].value = qty;
				els[prefix + "unit"].value = item.unit.name;
				els[prefix + "name"].value = item.name;
				els[prefix + "description"].value = item.description;
				var unitPrice = item.unitPrice;
				var unitPriceInput = els[prefix + "unitPrice"];
				unitPriceInput.value = $.formatCurrency(unitPrice);
				$(unitPriceInput).parents("tr")
					.find(".invoicing-items-total .value")
						.text($.formatCurrency(qty * unitPrice));
				els[prefix + "tax"].value = item.tax.taxValue;
				this.invoicingItems.computeFooterValues();
				$("#inventory-selector-" + type).dialog("close");
			}
		});
	}
}

/* static methods */
pkg.InvoicingItems._fixInvoicingItemPos = function() {
	var $this = $(this);
	var pos = $this.parent()
		.children()
		.index($this);
	$this.find("td:first-child")
		.text((pos + 1) + ".");
	$this.find(":input")
		.each(function() {
			var $this = $(this);
			var name = $this.attr("name");
			if (name.match(/^items\[\d+\]\.(\w+)$/)) {
				$this.attr(
					"name", "items[" + pos + "]." + RegExp.$1
				);
			}
		});
}

})(jQuery);
