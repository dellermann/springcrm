--
-- db-diff-set-1-de-DE.sql
-- DB difference set, version 1, German (Germany)
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


-- insert basic selection values
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1100, 0, 'niedrig', 0, 'org.amcworld.springcrm.TicketPriority')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1101, 0, 'normal', 10, 'org.amcworld.springcrm.TicketPriority')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1102, 0, 'hoch', 20, 'org.amcworld.springcrm.TicketPriority')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (1103, 0, 'Notfall', 30, 'org.amcworld.springcrm.TicketPriority')

-- insert sequence number predefinitions
INSERT INTO seq_number (id, version, controller_name, end_value, prefix, start_value, suffix) VALUES (12, 0, 'ticket', 99999, 'T', 10000, '')

-- insert basic configurations
INSERT INTO config (version, name, value) VALUES (0, 'baseDataLocale', 'de-DE')
