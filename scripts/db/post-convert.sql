--
-- remove duplicated person data
UPDATE person
  SET other_addr_country = '',
    other_addr_location = '',
    other_addr_po_box = '',
    other_addr_postal_code = '',
    other_addr_state = '',
    other_addr_street = ''
  WHERE TRIM(mailing_addr_country) = TRIM(other_addr_country)
    AND TRIM(mailing_addr_location) = TRIM(other_addr_location)
    AND TRIM(mailing_addr_po_box) = TRIM(other_addr_po_box)
    AND TRIM(mailing_addr_postal_code) = TRIM(other_addr_postal_code)
    AND TRIM(mailing_addr_state) = TRIM(other_addr_state)
    AND TRIM(mailing_addr_street) = TRIM(other_addr_street);
UPDATE person
  SET phone_other = ''
  WHERE TRIM(phone) = TRIM(phone_other);
  
--
-- fix abbreviations and malformed data in organization and person records
UPDATE organization
  SET billing_addr_street = REPLACE(
      REPLACE(billing_addr_street, 'str.', 'straße'),
      'Str.', 'Straße'
    ),
    shipping_addr_street = REPLACE(
      REPLACE(shipping_addr_street, 'str.', 'straße'),
      'Str.', 'Straße'
    );
UPDATE person
  SET mailing_addr_street = REPLACE(
      REPLACE(mailing_addr_street, 'str.', 'straße'),
      'Str.', 'Straße'
    ),
    other_addr_street = REPLACE(
      REPLACE(other_addr_street, 'str.', 'straße'),
      'Str.', 'Straße'
    );
UPDATE organization
  SET billing_addr_street = LEFT(
      billing_addr_street, CHAR_LENGTH(billing_addr_street) - 1
    )
  WHERE RIGHT(billing_addr_street, 1) = ',';
UPDATE organization
  SET shipping_addr_street = LEFT(
      shipping_addr_street, CHAR_LENGTH(shipping_addr_street) - 1
    )
  WHERE RIGHT(shipping_addr_street, 1) = ',';
UPDATE organization
  SET website = CONCAT('http://', website)
  WHERE website <> ''
    AND website NOT RLIKE '^https?://';
UPDATE person
  SET mailing_addr_street = LEFT(
      mailing_addr_street, CHAR_LENGTH(mailing_addr_street) - 1
    )
  WHERE RIGHT(mailing_addr_street, 1) = ',';
UPDATE person
  SET other_addr_street = LEFT(
      other_addr_street, CHAR_LENGTH(other_addr_street) - 1
    )
  WHERE RIGHT(other_addr_street, 1) = ',';

--
-- fix selector values and invalid entries
UPDATE service
  SET category_id = category_id + 1999,
    unit_id = unit_id + 299;

UPDATE product
  SET category_id = category_id + 2999,
    unit_id = unit_id + 299;

UPDATE invoicing_transaction
  SET carrier_id = carrier_id + 499,
    quote_stage_id = quote_stage_id + 599,
    so_stage_id = so_stage_id + 799,
    invoice_stage_id = invoice_stage_id + 899
UPDATE invoicing_transaction
  SET person_id = NULL
  WHERE person_id = 0;

--
-- Fix units
UPDATE invoicing_item SET items_idx = items_idx - 1;
UPDATE invoicing_item SET unit = 'Stunden' WHERE unit = 'Hours';
UPDATE invoicing_item SET unit = 'Einheiten' WHERE unit = 'Incidents';
UPDATE invoicing_item SET unit = 'Karton' WHERE unit = 'Box';
UPDATE invoicing_item SET unit = 'm' WHERE unit = 'M';

--
-- compute terms and conditions entries
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

--
-- update sequence numbers
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

--
-- entries which are to fix manually
SELECT
    'Bitte Organisation zuordnen.' AS action,
    p.id,
    p.last_name,
    p.first_name
  FROM person AS p
  WHERE organization_id = 0
  ORDER BY last_name, first_name;
