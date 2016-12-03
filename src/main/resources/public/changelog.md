## 2.1.4

* Turnover overview for all organizations, optionally filterable by year.

## 2.1.3

* Settings for the list of unpaid bills: minimum unpaid amount, sort
  criterion and order, maximum number of items.
* New panel containing currently active projects.
* Fix wrap and wrong vertical alignment in V.A.T calculator.

## 2.1.2

* Re-implement search feature

## 2.1.1

* Positive and negative assessments of organizations and persons
* Boilerplates for inclusion in text controls
* Configurable number of items per page in list views

## 2.1.0

* Upgrade to Grails 3
* Update underlying libraries like Bootstrap and FontAwesome
* Lot of bug fixes and code optimizations

[comment]: STOP

## 2.0.20

* Wrong character set when displaying changelog on Windows
  ([issue #84](https://github.com/dellermann/springcrm/issues/84))
* Lonely "next" button in lists with one item
  ([issue #5](https://github.com/dellermann/springcrm/issues/5))

## 2.0.19 (RC 2)

* Display changelog at each new version
  ([issue #80](https://github.com/dellermann/springcrm/issues/80))
* Display payment date at invoices and reminders
  ([issue #72](https://github.com/dellermann/springcrm/issues/72))
* Bug fix: entering a delivery or payment date in invoices, credit notes and
  reminders didn't change stage to "delivered" or "paid", respectively
  ([issue #82](https://github.com/dellermann/springcrm/issues/82))

## 2.0.18 (RC 1)

* Scroll new invoice items to middle of the page instead of to the top
* Bug fix: more precision at currency calculation
* Bug fix: refresh token at Google synchronization was not evaluated correctly
* Bugfixes in call form and address fields

## 2.0.17

* New report displaying the turnover of a selected client
  ([issue #71](https://github.com/dellermann/springcrm/issues/71))
* Missing entity name on empty lists
  ([issue #81](https://github.com/dellermann/springcrm/issues/81))
* Bugfix in installer
