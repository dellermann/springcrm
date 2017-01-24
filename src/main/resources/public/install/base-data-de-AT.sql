--
-- base-data-de-AT.sql
-- Base data for installer, German (Austria)
--
-- Copyright (c) 2011-2017, Daniel Ellermann
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.
--


-- Rules for this file
-- 1. Empty lines and lines starting with -- after optional white spaces
--    (comments) are ignored.
-- 2. All other lines must be valid SQL commands such as DELETE FROM or
--    INSERT INTO.  No white spaces before the SQL commands are permitted.
-- 3. SQL commands must not be terminated by a semicolon.
-- 4. Line breaks within SQL commands are not permitted.


-- empty all tables
DELETE FROM calendar_event
DELETE FROM config
DELETE FROM google_data_sync_status
DELETE FROM invoicing_item
DELETE FROM invoicing_transaction
DELETE FROM invoicing_transaction_terms_and_conditions
DELETE FROM ldap_sync_status
DELETE FROM lru_entry
DELETE FROM note
DELETE FROM organization
DELETE FROM panel
DELETE FROM person
DELETE FROM phone_call
DELETE FROM project
DELETE FROM project_document
DELETE FROM project_item
DELETE FROM purchase_invoice
DELETE FROM purchase_invoice_item
DELETE FROM reminder
DELETE FROM sales_item
DELETE FROM sales_item_pricing
DELETE FROM sales_item_pricing_item
DELETE FROM sel_value
DELETE FROM seq_number
DELETE FROM user_data
DELETE FROM user_setting

-- insert basic selection values
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1, 0, 'Herr', 10, 'org.amcworld.springcrm.Salutation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2, 0, 'Frau', 20, 'org.amcworld.springcrm.Salutation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (100, 0, 'Kunde', 10, 'org.amcworld.springcrm.OrgType')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (101, 0, 'Wettbewerber', 20, 'org.amcworld.springcrm.OrgType')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (102, 0, 'Partner', 30, 'org.amcworld.springcrm.OrgType')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (103, 0, 'Interessent', 40, 'org.amcworld.springcrm.OrgType')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (104, 0, 'Verkäufer', 50, 'org.amcworld.springcrm.OrgType')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (105, 0, 'Investor', 60, 'org.amcworld.springcrm.OrgType')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (106, 0, 'sonstiges', 9000, 'org.amcworld.springcrm.OrgType')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (200, 0, 'akquiriert', 10, 'org.amcworld.springcrm.Rating')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (201, 0, 'aktiv', 20, 'org.amcworld.springcrm.Rating')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (202, 0, 'Markt verfehlt', 30, 'org.amcworld.springcrm.Rating')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (203, 0, 'kein Interesse', 40, 'org.amcworld.springcrm.Rating')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (204, 0, 'stillgelegt', 50, 'org.amcworld.springcrm.Rating')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (300, 0, 'Stück', 10, 'org.amcworld.springcrm.Unit')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (301, 0, 'Einheiten', 20, 'org.amcworld.springcrm.Unit')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (302, 0, 'Kartons', 30, 'org.amcworld.springcrm.Unit')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (303, 0, 'Stunden', 40, 'org.amcworld.springcrm.Unit')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (304, 0, 'Minuten', 50, 'org.amcworld.springcrm.Unit')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (305, 0, 'm', 60, 'org.amcworld.springcrm.Unit')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (306, 0, 'kg', 70, 'org.amcworld.springcrm.Unit')
INSERT INTO sel_value (id, version, name, order_id, class, tax_value) VALUES (400, 0, '20 %', 10, 'org.amcworld.springcrm.TaxRate', 0.2)
INSERT INTO sel_value (id, version, name, order_id, class, tax_value) VALUES (401, 0, '10 %', 20, 'org.amcworld.springcrm.TaxRate', 0.1)
INSERT INTO sel_value (id, version, name, order_id, class, tax_value) VALUES (402, 0, '12 %', 30, 'org.amcworld.springcrm.TaxRate', 0.12)
INSERT INTO sel_value (id, version, name, order_id, class, tax_value) VALUES (403, 0, '19 %', 40, 'org.amcworld.springcrm.TaxRate', 0.19)
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (500, 0, 'Abholung', 10, 'org.amcworld.springcrm.Carrier')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (501, 0, 'elektronisch', 20, 'org.amcworld.springcrm.Carrier')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (502, 0, 'Österreichische Post', 30, 'org.amcworld.springcrm.Carrier')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (503, 0, 'DHL', 40, 'org.amcworld.springcrm.Carrier')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (504, 0, 'UPS', 50, 'org.amcworld.springcrm.Carrier')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (505, 0, 'FedEx', 60, 'org.amcworld.springcrm.Carrier')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (506, 0, 'GLS', 70, 'org.amcworld.springcrm.Carrier')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (507, 0, 'TNT', 80, 'org.amcworld.springcrm.Carrier')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (600, 0, 'erstellt', 10, 'org.amcworld.springcrm.QuoteStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (601, 0, 'durchgesehen', 20, 'org.amcworld.springcrm.QuoteStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (602, 0, 'versendet', 30, 'org.amcworld.springcrm.QuoteStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (603, 0, 'akzeptiert', 40, 'org.amcworld.springcrm.QuoteStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (604, 0, 'abgelehnt', 50, 'org.amcworld.springcrm.QuoteStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (700, 0, 'Dienstleistungen', 10, 'org.amcworld.springcrm.TermsAndConditions')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (701, 0, 'Waren', 20, 'org.amcworld.springcrm.TermsAndConditions')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (800, 0, 'erstellt', 10, 'org.amcworld.springcrm.SalesOrderStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (801, 0, 'durchgesehen', 20, 'org.amcworld.springcrm.SalesOrderStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (802, 0, 'versendet', 30, 'org.amcworld.springcrm.SalesOrderStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (803, 0, 'abgeschlossen', 40, 'org.amcworld.springcrm.SalesOrderStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (804, 0, 'storniert', 50, 'org.amcworld.springcrm.SalesOrderStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (900, 0, 'erstellt', 10, 'org.amcworld.springcrm.InvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (901, 0, 'durchgesehen', 20, 'org.amcworld.springcrm.InvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (902, 0, 'versendet', 30, 'org.amcworld.springcrm.InvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (903, 0, 'bezahlt', 40, 'org.amcworld.springcrm.InvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (904, 0, 'gemahnt', 50, 'org.amcworld.springcrm.InvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (905, 0, 'inkasso', 60, 'org.amcworld.springcrm.InvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (906, 0, 'ausgebucht', 70, 'org.amcworld.springcrm.InvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (907, 0, 'storniert', 80, 'org.amcworld.springcrm.InvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1000, 0, 'Bekleidungsindustrie', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1001, 0, 'Banken', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1002, 0, 'Biotechnologie', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1003, 0, 'Chemie', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1004, 0, 'Kommunikation', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1005, 0, 'Anlagenbau', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1006, 0, 'Beratung', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1007, 0, 'Bildungswesen', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1008, 0, 'Elektronik', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1009, 0, 'Energie', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1010, 0, 'Ingenieurwesen', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1011, 0, 'Unterhaltung', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1012, 0, 'Umwelt', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1013, 0, 'Finanzwesen', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1014, 0, 'Essen und Trinken', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1015, 0, 'Regierung und Behörden', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1016, 0, 'Gesundheitswesen', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1017, 0, 'Übernachtung', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1018, 0, 'Versicherungen', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1019, 0, 'Maschinenbau', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1020, 0, 'Fertigung', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1021, 0, 'Medien', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1022, 0, 'Wohlfahrt', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1023, 0, 'Freizeit und Erholung', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1024, 0, 'Einzelhandel', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1025, 0, 'Speditionen', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1026, 0, 'Technologie', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1027, 0, 'Telekommunikation', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1028, 0, 'Transport und Verkehr', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1029, 0, 'Versorgungseinrichtungen', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1030, 0, 'IT', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1031, 0, 'Bauwesen', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1032, 0, 'Öffentlicher Dienst', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1033, 0, 'Handwerk', 0, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1034, 0, 'sonstiges', 9000, 'org.amcworld.springcrm.Industry')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1100, 0, 'niedrig', 0, 'org.amcworld.springcrm.TicketPriority')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1101, 0, 'normal', 10, 'org.amcworld.springcrm.TicketPriority')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1102, 0, 'hoch', 20, 'org.amcworld.springcrm.TicketPriority')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1103, 0, 'Notfall', 30, 'org.amcworld.springcrm.TicketPriority')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2000, 0, 'Unterstützung', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2001, 0, 'Installation/Konfiguration', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2002, 0, 'Umstellung', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2003, 0, 'Anpassung', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2004, 0, 'Schulung', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2005, 0, 'Programmierung', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2006, 0, 'Beratung', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2007, 0, 'Grafik und Design', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2008, 0, 'Datenverarbeitung', 0, 'org.amcworld.springcrm.ServiceCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2100, 0, 'eingegangen', 10, 'org.amcworld.springcrm.PurchaseInvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2101, 0, 'geprüft', 20, 'org.amcworld.springcrm.PurchaseInvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2102, 0, 'bezahlt', 30, 'org.amcworld.springcrm.PurchaseInvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2103, 0, 'zurückgewiesen', 40, 'org.amcworld.springcrm.PurchaseInvoiceStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2200, 0, 'erstellt', 10, 'org.amcworld.springcrm.DunningStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2201, 0, 'durchgesehen', 20, 'org.amcworld.springcrm.DunningStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2202, 0, 'versendet', 30, 'org.amcworld.springcrm.DunningStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2203, 0, 'bezahlt', 40, 'org.amcworld.springcrm.DunningStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2204, 0, 'inkasso', 50, 'org.amcworld.springcrm.DunningStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2205, 0, 'ausgebucht', 60, 'org.amcworld.springcrm.DunningStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2206, 0, 'storniert', 70, 'org.amcworld.springcrm.DunningStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2300, 0, '1. Mahnung', 10, 'org.amcworld.springcrm.DunningLevel')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2301, 0, '2. Mahnung', 20, 'org.amcworld.springcrm.DunningLevel')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2302, 0, '3. Mahnung', 30, 'org.amcworld.springcrm.DunningLevel')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2400, 0, 'bar', 10, 'org.amcworld.springcrm.PaymentMethod')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2401, 0, 'Überweisung', 20, 'org.amcworld.springcrm.PaymentMethod')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2402, 0, 'Abbuchung', 30, 'org.amcworld.springcrm.PaymentMethod')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2403, 0, 'Scheck', 40, 'org.amcworld.springcrm.PaymentMethod')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2404, 0, 'Verrechnung', 50, 'org.amcworld.springcrm.PaymentMethod')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2500, 0, 'erstellt', 10, 'org.amcworld.springcrm.CreditMemoStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2501, 0, 'durchgesehen', 20, 'org.amcworld.springcrm.CreditMemoStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2502, 0, 'versendet', 30, 'org.amcworld.springcrm.CreditMemoStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2503, 0, 'bezahlt', 40, 'org.amcworld.springcrm.CreditMemoStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2504, 0, 'storniert', 50, 'org.amcworld.springcrm.CreditMemoStage')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2600, 0, 'in Bearbeitung', 10, 'org.amcworld.springcrm.ProjectStatus');
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2601, 0, 'pausiert', 20, 'org.amcworld.springcrm.ProjectStatus');
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2602, 0, 'wartet auf Kunden', 30, 'org.amcworld.springcrm.ProjectStatus');
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2603, 0, 'wartet auf Lieferanten', 40, 'org.amcworld.springcrm.ProjectStatus');
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2604, 0, 'abgeschlossen', 50, 'org.amcworld.springcrm.ProjectStatus');
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (2605, 0, 'abgebrochen', 60, 'org.amcworld.springcrm.ProjectStatus');
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3000, 0, 'Hardware', 0, 'org.amcworld.springcrm.ProductCategory')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3001, 0, 'Software', 0, 'org.amcworld.springcrm.ProductCategory')

-- insert sequence number predefinitions
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (1, 0, 'quote', 99999, 'A', 10000, '', 100)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (2, 0, 'salesOrder', 99999, 'B', 10000, '', 110)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (3, 0, 'invoice', 99999, 'R', 10000, '', 120)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (4, 0, 'dunning', 99999, 'M', 10000, '', 130)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (5, 0, 'work', 99999, 'S', 10000, '', 210)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (6, 0, 'product', 99999, 'P', 10000, '', 200)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (7, 0, 'organization', 99999, 'O', 10000, '', 10)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (8, 0, 'person', 99999, 'E', 10000, '', 20)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (9, 0, 'note', 99999, 'N', 10000, '', 300)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (10, 0, 'creditMemo', 99999, 'G', 10000, '', 140)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (11, 0, 'project', 99999, 'J', 10000, '', 310)
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix, order_id) VALUES (12, 0, 'ticket', 99999, 'T', 10000, '', 400)

-- insert basic configurations
INSERT INTO config (version, name, value) VALUES (0, 'baseDataLocale', 'de-AT')
INSERT INTO config (version, name, value) VALUES (0, 'currency', 'EUR')
INSERT INTO config (version, name, value) VALUES (0, 'dbVersion', '1')
INSERT INTO config (version, name) VALUES (0, 'ldapBindDn')
INSERT INTO config (version, name) VALUES (0, 'ldapBindPasswd')
INSERT INTO config (version, name) VALUES (0, 'ldapContactDn')
INSERT INTO config (version, name) VALUES (0, 'ldapHost')
INSERT INTO config (version, name) VALUES (0, 'ldapPort')
INSERT INTO config (version, name, value) VALUES (0, 'numFractionDigits', '2')
INSERT INTO config (version, name, value) VALUES (0, 'numFractionDigitsExt', '2')
