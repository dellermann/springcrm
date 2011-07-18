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
    e.createdtime AS date_created,
    a.email1,
    a.email2,
    a.fax,
    IF(
        a.industry = '--None--',
        NULL,
        FIND_IN_SET(a.industry, 'Apparel,Banking,Biotechnology,Chemicals,Communications,Construction,Consulting,Education,Electronics,Energy,Engineering,Entertainment,Environmental,Finance,Food & Beverage,Government,Healthcare,Hospitality,Insurance,Machinery,Manufacturing,Media,Not For Profit,Recreation,Retail,Shipping,Technology,Telecommunications,Transportation,Utilities,IT,Bauwesen,öffentlicher Dienst,Handwerk,Other') + 999
      ) AS industry_id,
    e.modifiedtime AS last_updated,
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
    e.createdtime AS date_created,
    c.department,
    c.email AS email1,
    c.otheremail AS email2,
    c.fax,
    c.firstname AS first_name,
    c.title AS job_title,
    c.lastname AS last_name,
    e.modifiedtime AS last_updated,
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
    e.createdtime AS date_created,
    e.description,
    e.modifiedtime AS last_updated,
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
-- Products
--
-- The following original SELECT command produces an error in phpMyAdmin
-- during export. You must use the uncommented one.
SELECT
    p.productid AS id,
    0 AS version,
    IF(
        p.productcategory = '--None--',
        NULL,
        FIND_IN_SET(p.productcategory, 'Hardware,Software')
      ) AS category_id,
    p.commissionrate AS commission,
    e.createdtime AS date_created,
    e.description,
    e.modifiedtime AS last_updated,
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
-- Calls
SELECT
    a.activityid AS id,
    0 AS version,
    e1.createdtime AS date_created,
    e1.modifiedtime AS last_updated,
    e1.description AS notes,
    e2.crmid AS organization_id,
    ca.contactid AS person_id,
    -- NULL AS phone
    STR_TO_DATE(
        CONCAT(DATE_FORMAT(a.date_start, '%Y-%m-%d'), ' ', a.time_start),
        '%Y-%m-%d %H:%i'
      ) AS start,
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

--
-- Quotes
SELECT
    q.quoteid AS id,
    0 AS version,
    q.adjustment,
    qba.bill_country AS billing_addr_country,
    qba.bill_city AS billing_addr_location,
    qba.bill_pobox AS billing_addr_po_box,
    qba.bill_code AS billing_addr_postal_code,
    qba.bill_state AS billing_addr_state,
    qba.bill_street AS billing_addr_street,
    IF(
        q.carrier = '--None--',
        NULL,
        FIND_IN_SET(q.carrier, 'Abholung,kein,DHL,BlueDart,UPS')
      ) AS carrier_id,
    e.createdtime AS date_created,
    q.discount_amount,
    q.discount_percent,
    e.modifiedtime AS doc_date,
    '' AS footer_text,
    e.description AS header_text,
    e.modifiedtime AS last_updated,
    SUBSTRING(q.quote_no, 5) AS number,
    q.accountid AS organization_id,
    q.contactid AS person_id,
    qsa.ship_country AS shipping_addr_country,
    qsa.ship_city AS shipping_addr_location,
    qsa.ship_pobox AS shipping_addr_po_box,
    qsa.ship_code AS shipping_addr_postal_code,
    qsa.ship_state AS shipping_addr_state,
    qsa.ship_street AS shipping_addr_street,
    q.s_h_amount AS shipping_costs,
    IF(
        q.shipping = '',
        NULL,
        IF(
          STR_TO_DATE(q.shipping, '%d-%m-%Y') IS NULL,
          IF(
            STR_TO_DATE(q.shipping, '%Y-%m-%d') IS NULL,
            STR_TO_DATE(q.shipping, '%d.%m.%Y'),
            STR_TO_DATE(q.shipping, '%Y-%m-%d')
          ),
          STR_TO_DATE(q.shipping, '%d-%m-%Y')
        )
      ) AS shipping_date,
    19 AS shipping_tax,
    q.subject,
    'Q' AS type,
    'org.amcworld.springcrm.Quote' AS class,
    NULL AS due_date_payment,
    NULL AS payment_amount,
    NULL AS payment_date,
    NULL AS quote_id,
    NULL AS sales_order_id,
    NULL AS invoice_stage_id,
    IF(
        q.quotestage = '--None--',
        NULL,
        FIND_IN_SET(q.quotestage, 'Created,Reviewed,Delivered,Accepted,Rejected')
      ) AS quote_stage_id,
    q.validtill AS valid_until,
    NULL AS delivery_date,
    NULL AS due_date,
    NULL AS so_stage_id
  FROM vtiger_quotes AS q
    JOIN vtiger_quotesbillads AS qba
      ON qba.quotebilladdressid = q.quoteid
    JOIN vtiger_quotesshipads AS qsa
      ON qsa.quoteshipaddressid = q.quoteid
    JOIN vtiger_crmentity AS e
      ON e.crmid = q.quoteid AND e.setype = 'Quotes';

--
-- Sales orders
SELECT
    so.salesorderid AS id,
    0 AS version,
    so.adjustment,
    soba.bill_country AS billing_addr_country,
    soba.bill_city AS billing_addr_location,
    soba.bill_pobox AS billing_addr_po_box,
    soba.bill_code AS billing_addr_postal_code,
    soba.bill_state AS billing_addr_state,
    soba.bill_street AS billing_addr_street,
    IF(
        so.carrier = '--None--',
        NULL,
        FIND_IN_SET(so.carrier, 'Abholung,kein,DHL,BlueDart,UPS')
      ) AS carrier_id,
    e.createdtime AS date_created,
    so.discount_amount,
    so.discount_percent,
    e.modifiedtime AS doc_date,
    '' AS footer_text,
    e.description AS header_text,
    e.modifiedtime AS last_updated,
    SUBSTRING(so.salesorder_no, 5) AS number,
    so.accountid AS organization_id,
    so.contactid AS person_id,
    sosa.ship_country AS shipping_addr_country,
    sosa.ship_city AS shipping_addr_location,
    sosa.ship_pobox AS shipping_addr_po_box,
    sosa.ship_code AS shipping_addr_postal_code,
    sosa.ship_state AS shipping_addr_state,
    sosa.ship_street AS shipping_addr_street,
    so.s_h_amount AS shipping_costs,
    NULL AS shipping_date,
    19 AS shipping_tax,
    so.subject,
    'O' AS type,
    'org.amcworld.springcrm.SalesOrder' AS class,
    NULL AS due_date_payment,
    NULL AS payment_amount,
    NULL AS payment_date,
    so.quoteid AS quote_id,
    NULL AS sales_order_id,
    NULL AS invoice_stage_id,
    NULL AS quote_stage_id,
    NULL AS valid_until,
    NULL AS delivery_date,
    so.duedate AS due_date,
    IF(
        so.sostatus = '--None--',
        NULL,
        FIND_IN_SET(so.sostatus, 'Created,Approved,Delivered')
      ) AS so_stage_id
  FROM vtiger_salesorder AS so
    JOIN vtiger_sobillads AS soba
      ON soba.sobilladdressid = so.salesorderid
    JOIN vtiger_soshipads AS sosa
      ON sosa.soshipaddressid = so.salesorderid
    JOIN vtiger_crmentity AS e
      ON e.crmid = so.salesorderid AND e.setype = 'SalesOrder';

--
-- Invoices
SELECT
    i.invoiceid AS id,
    0 AS version,
    i.adjustment,
    iba.bill_country AS billing_addr_country,
    iba.bill_city AS billing_addr_location,
    iba.bill_pobox AS billing_addr_po_box,
    iba.bill_code AS billing_addr_postal_code,
    iba.bill_state AS billing_addr_state,
    iba.bill_street AS billing_addr_street,
    NULL AS carrier_id,
    e.createdtime AS date_created,
    i.discount_amount,
    i.discount_percent,
    i.invoicedate AS doc_date,
    '' AS footer_text,
    e.description AS header_text,
    e.modifiedtime AS last_updated,
    SUBSTRING(i.invoice_no, 5) AS number,
    i.accountid AS organization_id,
    i.contactid AS person_id,
    isa.ship_country AS shipping_addr_country,
    isa.ship_city AS shipping_addr_location,
    isa.ship_pobox AS shipping_addr_po_box,
    isa.ship_code AS shipping_addr_postal_code,
    isa.ship_state AS shipping_addr_state,
    isa.ship_street AS shipping_addr_street,
    i.s_h_amount AS shipping_costs,
    NULL AS shipping_date,
    19 AS shipping_tax,
    i.subject,
    'I' AS type,
    'org.amcworld.springcrm.Invoice' AS class,
    i.duedate AS due_date_payment,
    NULL AS payment_amount,
    NULL AS payment_date,
    NULL AS quote_id,
    i.salesorderid AS sales_order_id,
    IF(
        i.invoicestatus = '--None--',
        NULL,
        FIND_IN_SET(i.invoicestatus, 'Created,Approved,Sent,Paid,Mahnung,inkasso,abgeschrieben,Credit Invoice')
      ) AS invoice_stage_id,
    NULL AS quote_stage_id,
    NULL AS valid_until,
    NULL AS delivery_date,
    NULL AS due_date,
    NULL AS so_stage_id
  FROM vtiger_invoice AS i
    JOIN vtiger_invoicebillads AS iba
      ON iba.invoicebilladdressid = i.invoiceid
    JOIN vtiger_invoiceshipads AS isa
      ON isa.invoiceshipaddressid = i.invoiceid
    JOIN vtiger_crmentity AS e
      ON e.crmid = i.invoiceid AND e.setype = 'Invoice'
  WHERE i.invoicestatus <> 'Credit Invoice';

--
-- Invoicing items
SELECT
    i.lineitem_id AS id,
    0 AS version,
    i.comment AS description,
    i.id AS invoicing_transaction_id,
    IF(
        s.servicename IS NULL,
        p.productname,
        s.servicename
      ) AS name,
    REPLACE(
        REPLACE(
          REPLACE(
            IF(
              s.service_no IS NULL,
              p.product_no,
              s.service_no
            ),
            'SRV-',
            'S-'
          ),
          'PRD-',
          'P-'
        ),
        'PRO-',
        'P-'
      ) AS number,
    i.quantity,
    19 AS tax,
    IF(
        s.service_usageunit IS NULL,
        p.usageunit,
        s.service_usageunit
      ) AS unit,
    i.listprice AS unit_price,
    i.sequence_no AS items_idx
  FROM vtiger_inventoryproductrel AS i
    NATURAL LEFT JOIN vtiger_products AS p
    LEFT JOIN vtiger_service AS s
      ON i.productid = s.serviceid;

-- vim:set ts=2 sw=2 sts=2:
