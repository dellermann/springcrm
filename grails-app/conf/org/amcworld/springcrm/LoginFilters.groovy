package org.amcworld.springcrm

class LoginFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                if (!session?.user && actionName != 'login' && actionName != 'authenticate') {
					redirect(controller: 'user', action: 'login')
					return false
				}
            }
        }
    }
    
}
