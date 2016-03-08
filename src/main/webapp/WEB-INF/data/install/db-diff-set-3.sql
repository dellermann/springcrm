--
-- db-diff-set-3.sql
-- DB difference set, version 3
--
-- Copyright (c) 2011-2013, Daniel Ellermann
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


-- update invoicing transaction addresses
UPDATE `invoicing_transaction` SET billing_addr_country='' WHERE billing_addr_country IS NULL;
UPDATE `invoicing_transaction` SET billing_addr_location='' WHERE billing_addr_location IS NULL;
UPDATE `invoicing_transaction` SET billing_addr_po_box='' WHERE billing_addr_po_box IS NULL;
UPDATE `invoicing_transaction` SET billing_addr_postal_code='' WHERE billing_addr_postal_code IS NULL;
UPDATE `invoicing_transaction` SET billing_addr_state='' WHERE billing_addr_state IS NULL;
UPDATE `invoicing_transaction` SET billing_addr_street='' WHERE billing_addr_street IS NULL;
UPDATE `invoicing_transaction` SET shipping_addr_country='' WHERE shipping_addr_country IS NULL;
UPDATE `invoicing_transaction` SET shipping_addr_location='' WHERE shipping_addr_location IS NULL;
UPDATE `invoicing_transaction` SET shipping_addr_po_box='' WHERE shipping_addr_po_box IS NULL;
UPDATE `invoicing_transaction` SET shipping_addr_postal_code='' WHERE shipping_addr_postal_code IS NULL;
UPDATE `invoicing_transaction` SET shipping_addr_state='' WHERE shipping_addr_state IS NULL;
UPDATE `invoicing_transaction` SET shipping_addr_street='' WHERE shipping_addr_street IS NULL;

-- update organization addresses
UPDATE `organization` SET billing_addr_country='' WHERE billing_addr_country IS NULL;
UPDATE `organization` SET billing_addr_location='' WHERE billing_addr_location IS NULL;
UPDATE `organization` SET billing_addr_po_box='' WHERE billing_addr_po_box IS NULL;
UPDATE `organization` SET billing_addr_postal_code='' WHERE billing_addr_postal_code IS NULL;
UPDATE `organization` SET billing_addr_state='' WHERE billing_addr_state IS NULL;
UPDATE `organization` SET billing_addr_street='' WHERE billing_addr_street IS NULL;
UPDATE `organization` SET shipping_addr_country='' WHERE shipping_addr_country IS NULL;
UPDATE `organization` SET shipping_addr_location='' WHERE shipping_addr_location IS NULL;
UPDATE `organization` SET shipping_addr_po_box='' WHERE shipping_addr_po_box IS NULL;
UPDATE `organization` SET shipping_addr_postal_code='' WHERE shipping_addr_postal_code IS NULL;
UPDATE `organization` SET shipping_addr_state='' WHERE shipping_addr_state IS NULL;
UPDATE `organization` SET shipping_addr_street='' WHERE shipping_addr_street IS NULL;

-- update person addresses
UPDATE `person` SET mailing_addr_country='' WHERE mailing_addr_country IS NULL;
UPDATE `person` SET mailing_addr_location='' WHERE mailing_addr_location IS NULL;
UPDATE `person` SET mailing_addr_po_box='' WHERE mailing_addr_po_box IS NULL;
UPDATE `person` SET mailing_addr_postal_code='' WHERE mailing_addr_postal_code IS NULL;
UPDATE `person` SET mailing_addr_state='' WHERE mailing_addr_state IS NULL;
UPDATE `person` SET mailing_addr_street='' WHERE mailing_addr_street IS NULL;
UPDATE `person` SET other_addr_country='' WHERE other_addr_country IS NULL;
UPDATE `person` SET other_addr_location='' WHERE other_addr_location IS NULL;
UPDATE `person` SET other_addr_po_box='' WHERE other_addr_po_box IS NULL;
UPDATE `person` SET other_addr_postal_code='' WHERE other_addr_postal_code IS NULL;
UPDATE `person` SET other_addr_state='' WHERE other_addr_state IS NULL;
UPDATE `person` SET other_addr_street='' WHERE other_addr_street IS NULL;

-- update ticket addresses
UPDATE `ticket` SET address_country='' WHERE address_country IS NULL;
UPDATE `ticket` SET address_location='' WHERE address_location IS NULL;
UPDATE `ticket` SET address_po_box='' WHERE address_po_box IS NULL;
UPDATE `ticket` SET address_postal_code='' WHERE address_postal_code IS NULL;
UPDATE `ticket` SET address_state='' WHERE address_state IS NULL;
UPDATE `ticket` SET address_street='' WHERE address_street IS NULL;
