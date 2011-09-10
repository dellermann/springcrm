-- ==============================================
--
--   Invoicing transactions
--
-- ==============================================

--
-- Computes the total net value
UPDATE invoicing_transaction AS t
  SET total = (
	    SELECT
	        SUM(i.quantity * i.unit_price)
	      FROM invoicing_item AS i
	      WHERE i.invoicing_transaction_id = t.id
	  ) + t.shipping_costs;

--
-- Compute the total gross value
UPDATE invoicing_transaction AS t
  SET total = t.total + (
      SELECT
          SUM(i.quantity * i.unit_price * i.tax / 100)
        FROM invoicing_item AS i
        WHERE i.invoicing_transaction_id = t.id
    ) + t.shipping_costs * t.shipping_tax / 100;

--
-- Compute the total gross value
UPDATE invoicing_transaction AS t
  SET total = t.total - t.total * t.discount_percent / 100 -
    t.discount_amount + t.adjustment;

--
-- Make column NOT NULL
ALTER TABLE invoicing_transaction
  CHANGE total total DECIMAL(19, 2) NOT NULL;


-- ==============================================
--
--   Purchase invoices
--
-- ==============================================

--
-- Computes the total net value
UPDATE purchase_invoice AS p
  SET total = (
      SELECT
          SUM(i.quantity * i.unit_price)
        FROM purchase_invoice_item AS i
        WHERE i.invoice_id = p.id
    ) + p.shipping_costs;

--
-- Compute the total gross value
UPDATE purchase_invoice AS p
  SET total = p.total + (
      SELECT
          SUM(i.quantity * i.unit_price * i.tax / 100)
        FROM purchase_invoice_item AS i
        WHERE i.invoice_id = p.id
    ) + p.shipping_costs * p.shipping_tax / 100;

--
-- Compute the total gross value
UPDATE purchase_invoice AS p
  SET total = p.total - p.total * p.discount_percent / 100 -
    p.discount_amount + p.adjustment;

--
-- Make column NOT NULL
ALTER TABLE purchase_invoice
  CHANGE total total DECIMAL(19, 2) NOT NULL;
