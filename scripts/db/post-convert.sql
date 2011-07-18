--
-- in the destination database execute the following commands
UPDATE service
  SET
    category_id = category_id + 1999,
    unit_id = unit_id + 299;

UPDATE product
  SET
    category_id = category_id + 2999,
    unit_id = unit_id + 299;

UPDATE invoicing_transaction
  SET
    carrier_id = carrier_id + 499,
    quote_stage_id = quote_stage_id + 599,
    so_stage_id = so_stage_id + 799,
    invoice_stage_id = invoice_stage_id + 899
UPDATE invoicing_transaction
  SET person_id = NULL
  WHERE person_id = 0;

UPDATE invoicing_item SET items_idx = items_idx - 1;
UPDATE invoicing_item SET unit = 'Stunden' WHERE unit = 'Hours';
UPDATE invoicing_item SET unit = 'Einheiten' WHERE unit = 'Incidents';
UPDATE invoicing_item SET unit = 'Karton' WHERE unit = 'Box';
UPDATE invoicing_item SET unit = 'm' WHERE unit = 'M';

INSERT INTO invoicing_transaction_terms_and_conditions (
    invoicing_transaction_terms_and_conditions_id, terms_and_conditions_id
  )
  SELECT invoicing_transaction_id, 700
    FROM invoicing_item
    WHERE LEFT(number, 1) = 'S'
    GROUP BY invoicing_transaction_id;
INSERT INTO invoicing_transaction_terms_and_conditions (
    invoicing_transaction_terms_and_conditions_id, terms_and_conditions_id
  )
  SELECT invoicing_transaction_id, 701
    FROM invoicing_item
    WHERE LEFT(number, 1) = 'P'
    GROUP BY invoicing_transaction_id;

UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM invoicing_transaction WHERE type = 'Q'
    )
  WHERE class_name = 'org.amcworld.springcrm.Quote';
UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM invoicing_transaction WHERE type = 'O'
    )
  WHERE class_name = 'org.amcworld.springcrm.SalesOrder';
UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM invoicing_transaction WHERE type = 'I'
    )
  WHERE class_name = 'org.amcworld.springcrm.Invoice';
UPDATE seq_number
  SET next_number = (SELECT MAX(number) + 1 FROM product)
  WHERE class_name = 'org.amcworld.springcrm.Product';
UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM service WHERE number < 10500
    )
  WHERE class_name = 'org.amcworld.springcrm.Service';
UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM organization WHERE number < 90000
    )
  WHERE class_name = 'org.amcworld.springcrm.Organization';
UPDATE seq_number
  SET next_number = (SELECT MAX(number) + 1 FROM person)
  WHERE class_name = 'org.amcworld.springcrm.Person';
