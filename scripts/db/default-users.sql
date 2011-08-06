INSERT INTO user (id, version, admin, allowed_modules, date_created, email,
  fax, first_name, last_name, last_updated, mobile, password, phone,
  phone_home, user_name)
VALUES
(1, 0, NOW(), 0b1, 'all', 'd.ellermann@amc-world.de', '030 8321475-90', 'Daniel', 'Ellermann', NOW(), '0172 3952663', 'test', '030 8321475-0', '030 68327723', 'dellermann'),
(2, 0, NOW(), 0b0, 'all', 'r.kirchner@amc-world.de', '030 8321475-90', 'Robert', 'Kirchner', NOW(), '0179 7799849', 'test', '030 8321475-0', NULL, 'rkirchner'),
(3, 0, NOW(), 0b0, 'all', 'a.ellermann@amc-world.de', '030 8321475-90', 'Anke', 'Ellermann', NOW(), '0173 8158355', 'test', '030 8321475-0', '030 68327724', 'aellermann');
