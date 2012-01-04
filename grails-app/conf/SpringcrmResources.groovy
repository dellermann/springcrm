modules = {
    calendarForm {
        dependsOn 'core'

        resource '/js/calendar-form.js'
    }

    calendarView {
        dependsOn 'core'

        resource '/css/fullcalendar.css'
        resource '/js/fullcalendar.min.js'
    }

    calendarViewCalendar {
        dependsOn 'calendarView'

        resource '/js/calendar-view.js'
    }

    callForm {
        dependsOn 'core'

        resource '/js/call-form.js'
    }

    core {
        dependsOn 'jquery, jquery-ui'

        resource '/css/styles.css'
        resource '/css/jquery-ui-springcrm.css'
        resource '/js/jquery.ui.datepicker-de.js'
        resource '/js/init.js'
        resource '/js/i18n/i18n-source.js'
        resource '/js/scripts.js'
        resource url: '/img/spinner.gif', attrs: [ width: 16, height: 16 ], disposition: 'inline'
    }

    invoicingTransactionForm {
        dependsOn 'core'
        defaultBundle 'invoicing-transaction-form'

        resource '/js/invoicing-items.js'
        resource '/js/invoicing-transaction-form.js'
    }

    noteForm {
        dependsOn 'core'

        resource url: '/js/tiny_mce/jquery.tinymce.js', disposition: 'defer'
        resource '/js/note-form.js'
    }

    organizationForm {
        dependsOn 'core'

        resource '/js/organization-form.js'
    }

    overview {
        dependsOn 'core'

        resource '/js/overview.js'
    }

    personForm {
        dependsOn 'personShow'

        resource '/js/person-form.js'
    }

    personShow {
        dependsOn 'core'

        resource url: '/css/jquery.lightbox.css', attrs: [ media: 'screen' ]
        resource '/js/jquery.lightbox.min.js'
    }

    purchaseInvoicingForm {
        dependsOn 'invoicingTransactionForm'
        defaultBundle 'invoicing-transaction-form'

        resource '/js/purchase-invoice-form.js'
    }


    //-- Overrides ------------------------------

    overrides {
        jquery {
            resource id: 'js', disposition: 'defer'
        }
        'jquery-dev' {
            resource id: 'js', disposition: 'defer'
        }
        'jquery-ui' {
            resource id: 'js', disposition: 'defer'
        }
        'jquery-ui-dev' {
            dependsOn 'jquery-dev, jquery-theme'

            resource id: 'js', disposition: 'defer'
        }
    }
}
