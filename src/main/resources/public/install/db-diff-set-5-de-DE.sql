--
-- db-diff-set-5-de-DE.sql
-- DB difference set, version 5, German (Germany)
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


-- insert basic selection values
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3100, 0, 'männlich', 0, 'org.amcworld.springcrm.Gender')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3101, 0, 'weiblich', 10, 'org.amcworld.springcrm.Gender')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3102, 0, 'drittes Geschlecht', 20, 'org.amcworld.springcrm.Gender')

INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3120, 0, 'ledig', 0, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3121, 0, 'verheirated', 10, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3122, 0, 'verwitwet', 20, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3123, 0, 'geschieden', 30, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3124, 0, 'Ehe aufgehoben', 40, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3125, 0, 'in eingetragener Lebenspartnerschaft', 50, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3126, 0, 'durch Tod aufgelöste Lebenspartnerschaft', 60, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3127, 0, 'aufgehobene Lebenspartnerschaft', 70, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3128, 0, 'durch Todeserklärung aufgelöste Lebenspartnerschaft', 80, 'org.amcworld.springcrm.CivilStatus')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3129, 0, 'nicht bekannt', 90, 'org.amcworld.springcrm.CivilStatus')

INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3140, 0, '1 – Alleinstehende, Alleinlebende', 0, 'org.amcworld.springcrm.TaxBracket')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3141, 0, '2 – Alleinerziehende', 10, 'org.amcworld.springcrm.TaxBracket')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3142, 0, '3 – Verwitwete, Verheiratete (Partner in Klasse 5)', 20, 'org.amcworld.springcrm.TaxBracket')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3143, 0, '4 – Verheiratete (beide in Klasse 4)', 30, 'org.amcworld.springcrm.TaxBracket')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3144, 0, '5 – Verheiratete (Partner in Klasse 3)', 40, 'org.amcworld.springcrm.TaxBracket')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3145, 0, '6 – Zweitjob', 50, 'org.amcworld.springcrm.TaxBracket')

INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3200, 0, 'in Schulausbildung', 0, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3201, 0, 'Hauptschulabschluss', 10, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3202, 0, 'Mittlerer Schulabschluss', 20, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3203, 0, 'Fachhochschulreife', 30, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3204, 0, 'Abitur', 40, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3220, 0, 'Berufsabschluss', 200, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3221, 0, 'Handwerksmeister', 210, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3222, 0, 'Industriemeister', 220, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3223, 0, 'Fachmeister', 230, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3224, 0, 'Landwirtschaftsmeister', 240, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3225, 0, 'Hauswirtschaftsmeister', 250, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3226, 0, 'Fachwirt/Fachkaufmann', 260, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3250, 0, 'Bachelor', 500, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3251, 0, 'Bakkalaureus', 510, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3252, 0, 'Magister', 520, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3253, 0, 'Diplom (FH)', 530, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3254, 0, 'Diplom (DH)', 540, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3255, 0, 'Diplom', 550, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3256, 0, 'Master', 560, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3257, 0, 'Lizenziat', 570, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3258, 0, 'Meisterschüler', 580, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3259, 0, 'Doktor', 590, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3290, 0, 'sonstiger Abschluss', 900, 'org.amcworld.springcrm.Graduation')
INSERT INTO sel_value (id, version, name, order_id, class) VALUES (3299, 0, 'kein Abschluss', 990, 'org.amcworld.springcrm.Graduation')
