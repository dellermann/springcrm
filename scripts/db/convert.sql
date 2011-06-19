--
-- Organizations
SELECT
    a.accountid AS id,
    0 AS version,
    aba.bill_country AS billing_addr_country,
    aba.bill_city AS billing_addr_location,
    aba.bill_pobox AS billing_addr_po_box,
    aba.bill_code AS billing_addr_postal_code,
    aba.bill_state AS billing_addr_state,
    aba.bill_street AS billing_addr_street,
    NOW() AS date_created,
    a.email1,
    a.email2,
    a.fax,
    IF(
        a.industry = '--None--',
        NULL,
        FIND_IN_SET(a.industry, 'Apparel,Banking,Biotechnology,Chemicals,Communications,Construction,Consulting,Education,Electronics,Energy,Engineering,Entertainment,Environmental,Finance,Food & Beverage,Government,Healthcare,Hospitality,Insurance,Machinery,Manufacturing,Media,Not For Profit,Recreation,Retail,Shipping,Technology,Telecommunications,Transportation,Utilities,IT,Bauwesen,öffentlicher Dienst,Handwerk,Other') + 999
      ) AS industry_id,
    NOW() AS last_updated,
    acf.cf_454 AS legal_form,
    a.accountname AS name,
    e.description AS notes,
    a.employees AS num_employees,
    SUBSTRING(a.account_no, 5) AS number,
    a.ownership AS owner,
    a.phone,
    a.otherphone AS phone_other,
    IF(
        a.rating = '--None--',
        NULL,
        FIND_IN_SET(a.rating, 'Acquired,Active,Market Failed,kein Interesse,Shutdown') + 199
      ) AS rating_id,
    asa.ship_country AS shipping_addr_country,
    asa.ship_city AS shipping_addr_location,
    asa.ship_pobox AS shipping_addr_po_box,
    asa.ship_code AS shipping_addr_postal_code,
    asa.ship_state AS shipping_addr_state,
    asa.ship_street AS shipping_addr_street,
    IF(
        a.account_type IS NULL
          OR a.account_type = ''
          OR a.account_type = '--None--',
        NULL,
        FIND_IN_SET(a.account_type, 'Customer,Competitor,Partner,Prospect,Reseller,Investor,Other') + 99
      ) AS type_id,
    a.website
  FROM vtiger_account AS a
    JOIN vtiger_accountbillads AS aba
      ON aba.accountaddressid = a.accountid
    JOIN vtiger_accountshipads AS asa
      ON asa.accountaddressid = a.accountid
    NATURAL JOIN vtiger_accountscf AS acf
    JOIN vtiger_crmentity AS e
      ON e.crmid = a.accountid AND e.setype = 'Accounts';

--
-- Persons
SELECT
    c.contactid AS id,
    0 AS version,
    cs.assistant,
    cs.birthday,
    NOW() AS date_created,
    c.department,
    c.email AS email1,
    c.otheremail AS email2,
    c.fax,
    c.firstname AS first_name,
    c.title AS job_title,
    c.lastname AS last_name,
    NOW() AS last_updated,
    ca.mailingcountry AS mailing_addr_country,
    ca.mailingcity AS mailing_addr_location,
    ca.mailingpobox AS mailing_addr_po_box,
    ca.mailingzip AS mailing_addr_postal_code,
    ca.mailingstate AS mailing_addr_state,
    ca.mailingstreet AS mailing_addr_street,
    c.mobile,
    e.description AS notes,
    SUBSTRING(c.contact_no, 5) AS number,
    c.accountid AS organization_id,
    ca.othercountry AS other_addr_country,
    ca.othercity AS other_addr_location,
    ca.otherpobox AS other_addr_po_box,
    ca.otherzip AS other_addr_postal_code,
    ca.otherstate AS other_addr_state,
    ca.otherstreet AS other_addr_street,
    c.phone,
    cs.assistantphone AS phone_assistant,
    cs.homephone AS phone_home,
    cs.otherphone AS phone_other,
    IF(
        c.salutation = ''
          OR c.salutation = '--None--',
        NULL,
        FIND_IN_SET(c.salutation, 'Mr.,Ms.')
      ) AS salutation_id
  FROM vtiger_contactdetails AS c
    JOIN vtiger_contactsubdetails AS cs
      ON cs.contactsubscriptionid = c.contactid
    JOIN vtiger_contactaddress AS ca
      ON ca.contactaddressid = c.contactid
    JOIN vtiger_crmentity AS e
      ON e.crmid = c.contactid AND e.setype = 'Contacts';

--
-- Services
--
-- The following original SELECT command produces an error in phpMyAdmin
-- during export. You must use the uncommented one.
/*
SELECT
    s.serviceid AS id,
    0 AS version,
    IF(
        s.servicecategory = '--None--',
        NULL,
        FIND_IN_SET(s.servicecategory, 'Unterstützung,Installation/Konfiguration,Umstellung,Anpassung,Schulung,Programmierung,Beratung,Grafik und Design,Datenverarbeitung') + 1999
      ) AS category_id,
    s.commissionrate AS commission,
    NOW() AS date_created,
    e.description,
    NOW() AS last_updated,
    s.servicename AS name,
    SUBSTRING(s.service_no, 5) AS number,
    s.qty_per_unit AS quantity,
    NULL AS sales_end,
    NULL AS sales_start,
    400 AS tax_class_id,
    FIND_IN_SET(
        s.service_usageunit, 'Stück,Incidents,Box,Hours'
      ) + 299 AS unit_id,
    s.unit_price
  FROM vtiger_service AS s
    JOIN vtiger_crmentity AS e
      ON e.crmid = s.serviceid AND e.setype = 'Services';
*/
SELECT
    s.serviceid AS id,
    0 AS version,
    IF(
        s.servicecategory = '--None--',
        NULL,
        FIND_IN_SET(s.servicecategory, 'Unterstützung,Installation/Konfiguration,Umstellung,Anpassung,Schulung,Programmierung,Beratung,Grafik und Design,Datenverarbeitung')
      ) AS category_id,
    s.commissionrate AS commission,
    NOW() AS date_created,
    e.description,
    NOW() AS last_updated,
    s.servicename AS name,
    SUBSTRING(s.service_no, 5) AS number,
    s.qty_per_unit AS quantity,
    NULL AS sales_end,
    NULL AS sales_start,
    400 AS tax_class_id,
    FIND_IN_SET(
        s.service_usageunit, 'Stück,Incidents,Box,Hours'
      ) AS unit_id,
    s.unit_price
  FROM vtiger_service AS s
    JOIN vtiger_crmentity AS e
      ON e.crmid = s.serviceid AND e.setype = 'Services';
--
-- in the destination database execute the following command:
UPDATE service SET
    category_id = category_id + 1999,
    unit_id = unit_id + 299;

--
-- Products
--
-- The following original SELECT command produces an error in phpMyAdmin
-- during export. You must use the uncommented one.
/*
SELECT
    p.productid AS id,
    0 AS version,
    IF(
        p.productcategory = '--None--',
        NULL,
        FIND_IN_SET(p.productcategory, 'Hardware,Software') + 2999
      ) AS category_id,
    p.commissionrate AS commission,
    NOW() AS date_created,
    e.description,
    NOW() AS last_updated,
    IF(
        p.manufacturer = '--None--',
        NULL,
        p.manufacturer
      ) AS manufacturer,
    p.productname AS name,
    SUBSTRING(p.product_no, 5) AS number,
    p.qty_per_unit AS quantity,
    v.vendorname AS retailer,
    NULL AS sales_end,
    NULL AS sales_start,
    400 AS tax_class_id,
    FIND_IN_SET(
        p.usageunit, 'Stück,Incidents,Box,Hours,Minutes,M'
      ) + 299 AS unit_id,
    p.unit_price,
    p.weight
  FROM vtiger_products AS p
    LEFT JOIN vtiger_vendor AS v ON p.vendor_id = v.vendorid
    JOIN vtiger_crmentity AS e
      ON e.crmid = p.productid AND e.setype = 'Products';
*/
SELECT
    p.productid AS id,
    0 AS version,
    IF(
        p.productcategory = '--None--',
        NULL,
        FIND_IN_SET(p.productcategory, 'Hardware,Software')
      ) AS category_id,
    p.commissionrate AS commission,
    NOW() AS date_created,
    e.description,
    NOW() AS last_updated,
    IF(
        p.manufacturer = '--None--',
        NULL,
        p.manufacturer
      ) AS manufacturer,
    p.productname AS name,
    SUBSTRING(p.product_no, 5) AS number,
    p.qty_per_unit AS quantity,
    v.vendorname AS retailer,
    NULL AS sales_end,
    NULL AS sales_start,
    400 AS tax_class_id,
    FIND_IN_SET(
        p.usageunit, 'Stück,Incidents,Box,Hours,Minutes,M'
      ) AS unit_id,
    p.unit_price,
    p.weight
  FROM vtiger_products AS p
    LEFT JOIN vtiger_vendor AS v ON p.vendor_id = v.vendorid
    JOIN vtiger_crmentity AS e
      ON e.crmid = p.productid AND e.setype = 'Products';
--
-- in the destination database execute the following command:
UPDATE product SET
    category_id = category_id + 2999,
    unit_id = unit_id + 299;

--
-- Calls
SELECT
    a.activityid AS id,
    0 AS version,
    NOW() AS date_created,
    NOW() AS last_updated,
    e1.description AS notes,
    e2.crmid AS organization_id,
    ca.contactid AS person_id,
    -- NULL AS phone
    STR_TO_DATE(
        CONCAT(DATE_FORMAT(a.date_start, '%Y-%m-%d'), ' ', a.time_start),
        '%Y-%m-%d %H:%i'
      ) as start,
    CASE a.eventstatus
        WHEN 'Planned' THEN 'planned'
        WHEN 'Held' THEN 'completed'
        WHEN 'erledigt' THEN 'acknowledged'
        ELSE NULL
      END AS status,
    a.subject,
    'incoming' AS type
  FROM vtiger_activity AS a
    LEFT JOIN vtiger_cntactivityrel AS ca
      ON a.activityid = ca.activityid
    LEFT JOIN vtiger_crmentity AS e1
      ON a.activityid = e1.crmid AND e1.setype = 'Calendar'
    LEFT JOIN vtiger_seactivityrel AS sa
      ON a.activityid = sa.activityid 
    JOIN vtiger_crmentity AS e2
      ON e2.crmid = sa.crmid AND e2.setype = 'Accounts'
  WHERE a.activitytype = 'Call';

-- vim:set ts=2 sw=2 sts=2:
