package org.amcworld.springcrm

class LoginFilters {

    def filters = {
        login(controller:'*', action:'*') {
            before = {
				if (!session?.user
					&& actionName != 'login' && actionName != 'authenticate') 
				{
					redirect(controller: 'user', action: 'login')
					return false
				}
            }
        }

		permission(controller:'*', action:'*') {
			before = {
				User user = session?.user
				if (user && controllerName &&
					!(controllerName in ['notification', 'searchable', 'overview']) &&
					!(actionName in ['login', 'authenticate', 'logout']))
				{
					if (!user.checkAllowedControllers([controllerName])) {
						redirect(uri:'/forbidden.gsp')
						return false
					}
				}
			}
		}
    }
    
}
