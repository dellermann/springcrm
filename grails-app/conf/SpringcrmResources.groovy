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
    about {
        dependsOn 'core'

        resource url: 'less/about.less', attrs: [rel: 'stylesheet/less', type: 'css']
    }

    calendarForm {
        dependsOn 'core'

        resource url: 'less/calendar.less', attrs: [rel: 'stylesheet/less', type: 'css']
        resource '/js/calendar-form.js'
    }

    calendarView {
        dependsOn 'core'

        resource url: 'less/calendar.less', attrs: [rel: 'stylesheet/less', type: 'css']
        resource '/css/fullcalendar.css'

        resource url: '/js/fullcalendar.min.js', exclude: 'minify'
    }

    calendarViewCalendar {
        dependsOn 'calendarView'

        resource '/js/app/calendar-view.js'
    }

    callForm {
        dependsOn 'core'

        resource '/js/call-form.js'
    }

    config {
        dependsOn 'core'

        resource url: 'less/config.less', attrs: [rel: 'stylesheet/less', type: 'css']
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

        resource url: '/less/reset.less', attrs: [media: 'all', rel: 'stylesheet/less', type: 'css'], bundle: 'core'
        resource url: '/less/main.less', attrs: [media: 'all', rel: 'stylesheet/less', type: 'css'], bundle: 'core'
        resource url: '/css/print.css', attrs: [media: 'print']
        resource url: '/less/jquery-ui-springcrm.less', attrs: [media: 'all', rel: 'stylesheet/less', type: 'css'], bundle: 'core'

        resource '/js/jquery.ui.datepicker-de.js'
        resource '/js/jquery.ba-bbq.min.js'
        resource '/js/app/core.js'
        resource '/js/app/ui.js'
        resource url: '/img/spinner.gif', attrs: [ width: 16, height: 16 ], disposition: 'inline'
    }

    document {
        dependsOn 'elfinder'

        resource url: 'less/document.less', attrs: [rel: 'stylesheet/less', type: 'css']

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

    install {
        dependsOn 'core'

        resource url: 'less/install.less', attrs: [rel: 'stylesheet/less', type: 'css']
    }

    invoicingTransaction {
        dependsOn 'core'

        resource url: 'less/invoicing-transaction.less', attrs: [rel: 'stylesheet/less', type: 'css']
    }

    invoicingTransactionForm {
        dependsOn 'invoicingTransaction'
        defaultBundle 'invoicing-transaction-form'

        resource '/js/app/invoicing-items.js'
        resource '/js/app/invoicing-transaction-form.js'
    }

    invoicingTransactionShow {
        dependsOn 'invoicingTransaction'
        defaultBundle 'invoicing-transaction-show'
    }

    login {
        dependsOn 'core'
        defaultBundle 'core'

        resource url: 'less/login.less', attrs: [rel: 'stylesheet/less', type: 'css'], bundle: 'core'
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
        defaultBundle 'core'

        resource url: 'less/overview.less', attrs: [rel: 'stylesheet/less', type: 'css'], bundle: 'core'

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

    project {
        dependsOn 'core'

        resource url: 'less/project.less', attrs: [rel: 'stylesheet/less', type: 'css']
    }

    projectForm {
        dependsOn 'project'

        resource '/js/project-form.js'
    }

    projectShow {
        dependsOn 'project, selectBoxIt, elfinder'

        resource '/js/app/project-show.js'
    }

    purchaseInvoicingForm {
        dependsOn 'invoicingTransactionForm'
        defaultBundle 'invoicing-transaction-form'

        resource '/js/purchase-invoice-form.js'
    }

    report {
        dependsOn 'core'

        resource url: 'less/report.less', attrs: [rel: 'stylesheet/less', type: 'css']
    }

    reportSalesJournal {
        dependsOn 'report, selectBoxIt'

        resource url: 'less/invoicing-transaction.less', attrs: [rel: 'stylesheet/less', type: 'css']

        resource '/js/app/report-sales-journal.js'
    }

    salesItemForm {
        dependsOn 'salesItemPricing'
    }

    salesItemPricing {
        dependsOn 'core'

        resource url: 'less/invoicing-transaction.less', attrs: [rel: 'stylesheet/less', type: 'css']
        resource url: 'less/pricing.less', attrs: [rel: 'stylesheet/less', type: 'css']

        resource '/js/app/sales-item-pricing.js'
    }

    selectBoxIt {
        dependsOn 'jquery-ui'

        resource '/css/jquery/default/jquery.selectBoxIt.css'
        resource url: '/js/jquery.selectBoxIt.min.js', exclude: 'minify'
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
