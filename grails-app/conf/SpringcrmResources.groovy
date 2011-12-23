modules = {
    core {
        dependsOn 'jquery, jquery-ui'

        resource '/css/styles.css'
        resource '/js/jquery.ui.datepicker-de.js'
        resource '/js/init.js'
        resource '/js/i18n/i18n-source.js'
        resource '/js/scripts.js'
        resource url: '/img/spinner.gif', attrs: [ width: 16, height: 16 ]
    }

    overview {
        dependsOn 'core'

        resource '/js/overview.js'
    }

    overrides {
        jquery {
            resource id: 'js', disposition: 'defer'
        }
        'jquery-ui' {
            resource id: 'js', disposition: 'defer'
        }
    }
}
