/*
 * SpringcrmResources.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


modules = {
    calendarForm {
        dependsOn 'core'

        resource '/js/calendar-form.js'
    }

    calendarView {
        dependsOn 'core'

        resource '/css/fullcalendar.css'
        resource url: '/js/fullcalendar.min.js', exclude: 'minify'
    }

    calendarViewCalendar {
        dependsOn 'calendarView'

        resource '/js/calendar-view.js'
    }

    callForm {
        dependsOn 'core'

        resource '/js/call-form.js'
    }

    config {
        dependsOn 'core'
    }

    configSelValues {
        dependsOn 'config'
        defaultBundle 'config'

        resource '/js/config-sel-values.js'
    }

    configSeqNumbers {
        dependsOn 'config'
        defaultBundle 'config'

        resource '/js/config-seq-numbers.js'
    }

    core {
        dependsOn 'jquery-ui, jquery-json'

        resource url: '/css/reset.css', attrs: [ media: 'all' ]
        resource url: '/css/styles.css', attrs: [ media: 'all' ]
        resource url: '/css/print.css', attrs: [ media: 'print' ]
        resource '/css/jquery-ui-springcrm.css'
        resource '/js/jquery.ui.datepicker-de.js'
        resource '/js/jquery.ba-bbq.min.js'
        resource '/js/core.js'
        resource '/js/scripts.js'
        resource url: '/img/spinner.gif', attrs: [ width: 16, height: 16 ], disposition: 'inline'
    }

    document {
        dependsOn 'elfinder'

        resource '/js/document.js'
    }

    elfinder {
        dependsOn 'jquery-ui'

        resource url: '/css/elfinder/elfinder.min.css', exclude: 'minify'
        resource '/css/elfinder/theme.css'
        resource url: '/js/elfinder/elfinder.min.js', exclude: 'minify'
        resource '/js/elfinder/i18n/elfinder.de.js'
    }

    error {
        dependsOn 'jquery-ui'

        resource '/js/error-page.js'
    }

    invoicingTransactionForm {
        dependsOn 'core'
        defaultBundle 'invoicing-transaction-form'

        resource '/js/app/invoicing-items.js'
        resource '/js/app/invoicing-transaction-form.js'
    }

    'jquery-ui-selectmenu' {
        dependsOn 'jquery-ui'

        resource '/css/jquery/default/jquery.ui.selectmenu.css'
        resource '/js/jquery.ui.selectmenu.js'
    }

    noteForm {
        dependsOn 'core, tinyMce'

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

    projectForm {
        dependsOn 'core'

        resource '/js/project-form.js'
    }

    projectShow {
        dependsOn 'core, jquery-ui-selectmenu, elfinder'

        resource '/js/project-show.js'
    }

    purchaseInvoicingForm {
        dependsOn 'invoicingTransactionForm'
        defaultBundle 'invoicing-transaction-form'

        resource '/js/purchase-invoice-form.js'
    }

    reportSalesJournal {
        dependsOn 'core, jquery-ui-selectmenu'

        resource '/js/report-sales-journal.js'
    }

    salesItemForm {
        dependsOn 'salesItemPricing'
    }

    salesItemPricing {
        dependsOn 'core'

        resource '/js/sales-item-pricing.js'
    }

    tinyMce {
        resource url: '/js/tiny_mce/jquery.tinymce.js', exclude: 'minify'
    }


    //-- Overrides ------------------------------

    overrides {
        jquery {
            defaultBundle 'jquery'

            resource id: 'js', disposition: 'defer'
        }
        'jquery-dev' {
            defaultBundle 'jquery-dev'

            resource id: 'js', disposition: 'defer'
        }
        'jquery-json' {
            defaultBundle 'jquery'

            resource id: 'jquery-json', exclude: 'minify'
        }
        'jquery-ui' {
            defaultBundle 'jquery'

            resource id: 'js', disposition: 'defer'
        }
        'jquery-ui-dev' {
            defaultBundle 'jquery-dev'
            dependsOn 'jquery-dev, jquery-theme'

            resource id: 'js', disposition: 'defer'
        }
    }
}
