SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Datenbank: 'springcrm'
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle 'sel_value'
--

CREATE TABLE IF NOT EXISTS sel_value (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  version bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  order_id int(11) NOT NULL,
  class varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=42 ;

--
-- Daten für Tabelle 'sel_value'
--

TRUNCATE TABLE sel_value;
INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(1, 0, 'Herr', 10, 'org.amcworld.springcrm.Salutation'),
(2, 0, 'Frau', 20, 'org.amcworld.springcrm.Salutation');

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(100, 0, 'Kunde', 10, 'org.amcworld.springcrm.OrgType'),
(101, 0, 'Wettbewerber', 20, 'org.amcworld.springcrm.OrgType'),
(102, 0, 'Partner', 30, 'org.amcworld.springcrm.OrgType'),
(103, 0, 'Interessent', 40, 'org.amcworld.springcrm.OrgType'),
(104, 0, 'Verkäufer', 50, 'org.amcworld.springcrm.OrgType'),
(105, 0, 'Investor', 60, 'org.amcworld.springcrm.OrgType'),
(106, 0, 'sonstiges', 9000, 'org.amcworld.springcrm.OrgType');

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(200, 0, 'akquiriert', 10, 'org.amcworld.springcrm.Rating'),
(201, 0, 'aktiv', 20, 'org.amcworld.springcrm.Rating'),
(202, 0, 'Markt verfehlt', 30, 'org.amcworld.springcrm.Rating'),
(203, 0, 'kein Interesse', 40, 'org.amcworld.springcrm.Rating'),
(204, 0, 'stillgelegt', 50, 'org.amcworld.springcrm.Rating');

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(300, 0, 'Stück', 10, 'org.amcworld.springcrm.Unit'),
(301, 0, 'Einheiten', 20, 'org.amcworld.springcrm.Unit'),
(302, 0, 'Karton', 30, 'org.amcworld.springcrm.Unit'),
(303, 0, 'Stunden', 40, 'org.amcworld.springcrm.Unit'),
(304, 0, 'Minuten', 50, 'org.amcworld.springcrm.Unit'),
(305, 0, 'm', 60, 'org.amcworld.springcrm.Unit'),
(306, 0, 'kg', 70, 'org.amcworld.springcrm.Unit');

INSERT INTO sel_value (id, version, `name`, order_id, class, tax_value) VALUES
(400, 0, '19 %', 10, 'org.amcworld.springcrm.TaxClass', 0.19),
(401, 0, '7 %', 20, 'org.amcworld.springcrm.TaxClass', 0.07);

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(500, 0, 'Abholung', 10, 'org.amcworld.springcrm.Carrier'),
(501, 0, 'elektronisch', 20, 'org.amcworld.springcrm.Carrier'),
(502, 0, 'DHL', 30, 'org.amcworld.springcrm.Carrier'),
(503, 0, 'DPD', 40, 'org.amcworld.springcrm.Carrier'),
(504, 0, 'UPS', 50, 'org.amcworld.springcrm.Carrier');

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(600, 0, 'erstellt', 10, 'org.amcworld.springcrm.QuoteStage'),
(601, 0, 'durchgesehen', 20, 'org.amcworld.springcrm.QuoteStage'),
(602, 0, 'versendet', 30, 'org.amcworld.springcrm.QuoteStage'),
(603, 0, 'akzeptiert', 40, 'org.amcworld.springcrm.QuoteStage'),
(604, 0, 'abgelehnt', 50, 'org.amcworld.springcrm.QuoteStage');

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(700, 0, 'Dienstleistungen', 10, 'org.amcworld.springcrm.TermsAndConditions'),
(701, 0, 'Waren', 20, 'org.amcworld.springcrm.TermsAndConditions');

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(1000, 0, 'Bekleidungsindustrie', 0, 'org.amcworld.springcrm.Industry'),
(1001, 0, 'Banken', 0, 'org.amcworld.springcrm.Industry'),
(1002, 0, 'Biotechnologie', 0, 'org.amcworld.springcrm.Industry'),
(1003, 0, 'Chemie', 0, 'org.amcworld.springcrm.Industry'),
(1004, 0, 'Kommunikation', 0, 'org.amcworld.springcrm.Industry'),
(1005, 0, 'Anlagenbau', 0, 'org.amcworld.springcrm.Industry'),
(1006, 0, 'Beratung', 0, 'org.amcworld.springcrm.Industry'),
(1007, 0, 'Bildungswesen', 0, 'org.amcworld.springcrm.Industry'),
(1008, 0, 'Elektronik', 0, 'org.amcworld.springcrm.Industry'),
(1009, 0, 'Energie', 0, 'org.amcworld.springcrm.Industry'),
(1010, 0, 'Ingenieurwesen', 0, 'org.amcworld.springcrm.Industry'),
(1011, 0, 'Unterhaltung', 0, 'org.amcworld.springcrm.Industry'),
(1012, 0, 'Umwelt', 0, 'org.amcworld.springcrm.Industry'),
(1013, 0, 'Finanzwesen', 0, 'org.amcworld.springcrm.Industry'),
(1014, 0, 'Essen und Trinken', 0, 'org.amcworld.springcrm.Industry'),
(1015, 0, 'Regierung und Behörden', 0, 'org.amcworld.springcrm.Industry'),
(1016, 0, 'Gesundheitswesen', 0, 'org.amcworld.springcrm.Industry'),
(1017, 0, 'Übernachtung', 0, 'org.amcworld.springcrm.Industry'),
(1018, 0, 'Versicherungen', 0, 'org.amcworld.springcrm.Industry'),
(1019, 0, 'Maschinenbau', 0, 'org.amcworld.springcrm.Industry'),
(1020, 0, 'Fertigung', 0, 'org.amcworld.springcrm.Industry'),
(1021, 0, 'Medien', 0, 'org.amcworld.springcrm.Industry'),
(1022, 0, 'Wohlfahrt', 0, 'org.amcworld.springcrm.Industry'),
(1023, 0, 'Freizeit und Erholung', 0, 'org.amcworld.springcrm.Industry'),
(1024, 0, 'Einzelhandel', 0, 'org.amcworld.springcrm.Industry'),
(1025, 0, 'Speditionen', 0, 'org.amcworld.springcrm.Industry'),
(1026, 0, 'Technologie', 0, 'org.amcworld.springcrm.Industry'),
(1027, 0, 'Telekommunikation', 0, 'org.amcworld.springcrm.Industry'),
(1028, 0, 'Transport und Verkehr', 0, 'org.amcworld.springcrm.Industry'),
(1029, 0, 'Versorgungseinrichtungen', 0, 'org.amcworld.springcrm.Industry'),
(1030, 0, 'IT', 0, 'org.amcworld.springcrm.Industry'),
(1031, 0, 'Bauwesen', 0, 'org.amcworld.springcrm.Industry'),
(1032, 0, 'Öffentlicher Dienst', 0, 'org.amcworld.springcrm.Industry'),
(1033, 0, 'Handwerk', 0, 'org.amcworld.springcrm.Industry'),
(1034, 0, 'sonstiges', 9000, 'org.amcworld.springcrm.Industry');

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(2000, 0, 'Unterstützung', 0, 'org.amcworld.springcrm.ServiceCategory'),
(2001, 0, 'Installation/Konfiguration', 0, 'org.amcworld.springcrm.ServiceCategory'),
(2002, 0, 'Umstellung', 0, 'org.amcworld.springcrm.ServiceCategory'),
(2003, 0, 'Anpassung', 0, 'org.amcworld.springcrm.ServiceCategory'),
(2004, 0, 'Schulung', 0, 'org.amcworld.springcrm.ServiceCategory'),
(2005, 0, 'Programmierung', 0, 'org.amcworld.springcrm.ServiceCategory'),
(2006, 0, 'Beratung', 0, 'org.amcworld.springcrm.ServiceCategory'),
(2007, 0, 'Grafik und Design', 0, 'org.amcworld.springcrm.ServiceCategory'),
(2008, 0, 'Datenverarbeitung', 0, 'org.amcworld.springcrm.ServiceCategory');

INSERT INTO sel_value (id, version, `name`, order_id, class) VALUES
(3000, 0, 'Hardware', 0, 'org.amcworld.springcrm.ProductCategory'),
(3001, 0, 'Software', 0, 'org.amcworld.springcrm.ProductCategory');
