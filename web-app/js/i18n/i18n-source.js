//
// i18n-source.js
// Key definitions for i18n retrieval.
//
// The file defines the keys of localized messages which are used in JavaScript
// files.  The localized messages are obtained from the message resources of
// the server (files messages*.properties).  From them a file named "i18n.js"
// is generated in the same directory.  The translation is done by class
// "org.amcworld.springcrm.I18nResourceMapper".
//
// Lines starting with to slashes (//) and empty lines are ignored.  The file
// consists of an anonymous JavaScript array ([]) containing the message keys.
// The opening bracket must be the first character (excluding whitespaces) in
// the line.  The keys must be enclosed in apostrophes or quotation marks.
// Dots in keys are translated by underlines.  Keys in the form
// "my.key{sub.key1,sub.key2,sub.key3}" are translated to a JavaScript object
// as follows:
//     my_key {
//         sub_key1: "submsg1",
//         sub_key2: "submsg2",
//         sub_key3: "submsg3",
//     }
[
    'calendarEvent.allDay.label',
    'calendarEvent.axis.format',
    'calendarEvent.button.text{day,month,today,week}',
    'calendarEvent.column.format{day,month,week}',
    'calendarEvent.reminder.delete.label',
    'calendarEvent.reminder.unit.day',
    'calendarEvent.reminder.unit.days',
    'calendarEvent.reminder.unit.hour',
    'calendarEvent.reminder.unit.hours',
    'calendarEvent.reminder.unit.minute',
    'calendarEvent.reminder.unit.minutes',
    'calendarEvent.reminder.unit.week',
    'calendarEvent.reminder.unit.weeks',
    'calendarEvent.time.format{,agenda}',
    'calendarEvent.title.format{day,month,week}',
    'default.btn.down',
    'default.btn.up',
    'default.copyAddressWarning.billingAddr',
    'default.copyAddressWarning.mailingAddr',
    'default.copyAddressWarning.otherAddr',
    'default.copyAddressWarning.shippingAddr',
    'default.delete.confirm.msg',
    'default.delete.label',
    'default.format.date',
    'default.format.time',
    'default.search.label',
    'invoicingTransaction.addr.fromOrgBillingAddr',
    'invoicingTransaction.addr.fromOrgShippingAddr',
    'invoicingTransaction.billingAddr.copy',
    'invoicingTransaction.changeState.label',
    'invoicingTransaction.product.sel',
    'invoicingTransaction.service.sel',
    'invoicingTransaction.shippingAddr.copy',
    'invoicingTransaction.taxRate.label',
    'organization.billingAddr.copy',
    'organization.shippingAddr.copy',
    'person.addr.fromOrgBillingAddr',
    'person.addr.fromOrgShippingAddr',
    'person.mailingAddr.copy',
    'person.otherAddr.copy'
];
