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
  SET billing_addr_country = REPLACE(billing_addr_country, 'DE', 'Deutschland'),
    billing_addr_street = REPLACE(
      REPLACE(billing_addr_street, 'str.', 'straße'),
      'Str.', 'Straße'
    ),
    shipping_addr_country = REPLACE(shipping_addr_country, 'DE', 'Deutschland'),
    shipping_addr_street = REPLACE(
      REPLACE(shipping_addr_street, 'str.', 'straße'),
      'Str.', 'Straße'
    );
UPDATE person
  SET mailing_addr_country = REPLACE(mailing_addr_country, 'DE', 'Deutschland'),
    mailing_addr_street = REPLACE(
      REPLACE(mailing_addr_street, 'str.', 'straße'),
      'Str.', 'Straße'
    ),
    other_addr_country = REPLACE(other_addr_country, 'DE', 'Deutschland'),
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
  WHERE controller_name = 'quote';
UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM invoicing_transaction WHERE type = 'O'
    )
  WHERE controller_name = 'salesOrder';
UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM invoicing_transaction WHERE type = 'I'
    )
  WHERE controller_name = 'invoice';
UPDATE seq_number
  SET next_number = (SELECT MAX(number) + 1 FROM product)
  WHERE controller_name = 'product';
UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM service WHERE number < 10500
    )
  WHERE controller_name = 'service';
UPDATE seq_number
  SET next_number = (
      SELECT MAX(number) + 1 FROM organization WHERE number < 90000
    )
  WHERE controller_name = 'organization';
UPDATE seq_number
  SET next_number = (SELECT MAX(number) + 1 FROM person)
  WHERE controller_name = 'person';
UPDATE seq_number
  SET next_number = (SELECT MAX(number) + 1 FROM note)
  WHERE controller_name = 'note';

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
