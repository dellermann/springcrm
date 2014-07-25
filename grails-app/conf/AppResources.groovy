/*
 * AppResources.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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
    config {
        dependsOn 'core'

        resource url: 'less/config.less', attrs: [rel: 'stylesheet/less', type: 'css']
    }

    configMail {
        dependsOn 'config'
        defaultBundle 'config'

        resource '/js/app/config-mail.js'
    }

    configSelValues {
        dependsOn 'config, mustache'
        defaultBundle 'config'

        resource '/js/app/config-sel-values.js'
    }

    configSeqNumbers {
        dependsOn 'config'
        defaultBundle 'config'

        resource '/js/app/config-seq-numbers.js'
    }

    core {
        dependsOn 'jquery-ui, jquery-json, test'

        resource url: '/less/reset.less', attrs: [media: 'all', rel: 'stylesheet/less', type: 'css'], bundle: 'core'
        resource url: '/less/main.less', attrs: [media: 'all', rel: 'stylesheet/less', type: 'css'], bundle: 'core'
        resource url: '/css/print.css', attrs: [media: 'print']
        resource url: '/less/jquery-ui-springcrm.less', attrs: [media: 'all', rel: 'stylesheet/less', type: 'css'], bundle: 'core'

        resource '/js/jquery.ui.datepicker-de.js'
        resource '/js/jquery.ba-bbq.min.js'
        resource '/js/jquery.autosize-min.js'
        resource '/js/app/core.js'
        resource '/js/app/ui.js'
    }

    error {
        dependsOn 'jquery-ui'

        resource '/js/error-page.js'
    }

    frontend {
        dependsOn 'core, jquery-storage'

        resource url: 'less/frontend.less', attrs: [rel: 'stylesheet/less', type: 'css']

        resource '/js/app/frontend.js'
    }

    helpdeskForm {
        dependsOn 'core'

        resource '/js/app/helpdesk-form.js'
    }

    install {
        dependsOn 'core'

        resource url: 'less/install.less', attrs: [rel: 'stylesheet/less', type: 'css']
    }

    invoicingTransactionForm {
        dependsOn 'invoicingTransaction, mustache'
        defaultBundle 'invoicing-transaction-form'

        resource '/js/app/invoicing-items.js'
        resource '/js/app/invoicing-transaction-form.js'
    }

    invoicingTransactionShow {
        dependsOn 'invoicingTransaction'
        defaultBundle 'invoicing-transaction-show'
    }

    'jquery-storage' {
        dependsOn 'jquery'

        resource 'js/jquery.storageapi.min.js'
    }

    login {
        dependsOn 'core'
        defaultBundle 'core'

        resource url: 'less/login.less', attrs: [rel: 'stylesheet/less', type: 'css'], bundle: 'core'
    }

    mustache {
        resource '/js/mustache.js'
    }

    noteForm {
        dependsOn 'core'

        resource '/js/app/note-form.js'
    }

    projectForm {
        dependsOn 'project'

        resource '/js/project-form.js'
    }

    projectShow {
        dependsOn 'project, selectBoxIt, elfinder'

        resource '/js/app/project-show.js'
    }

    salesItemForm {
        dependsOn 'salesItemPricing'
    }

    salesItemPricing {
        dependsOn 'core, mustache'

        resource url: 'less/invoicing-transaction.less', attrs: [rel: 'stylesheet/less', type: 'css']
        resource url: 'less/pricing.less', attrs: [rel: 'stylesheet/less', type: 'css']

        resource '/js/app/sales-item-pricing.js'
    }

    searchResults {
        dependsOn 'jquery-ui'

        resource url: '/js/app/search.js'
    }

    selectBoxIt {
        dependsOn 'jquery-ui'

        resource '/css/jquery/default/jquery.selectBoxIt.css'
        resource url: '/js/jquery.selectBoxIt.min.js', exclude: 'minify'
    }

    settingsGoogleAuth {
        dependsOn 'core'

        resource '/js/app/settings-google-auth.js'
    }

    test {

        /*
         * This resource is for test environment, only.  See
         * AppTestResources.groovy for more information.
         */
    }

    ticket {
        dependsOn 'core'

        resource url: 'less/helpdesk.less', attrs: [rel: 'stylesheet/less', type: 'css']

        resource '/js/app/ticket.js'
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

