package org.amcworld.springcrm

class LoginFilters {

    def filters = {
        login(controller: '*', controllerExclude: 'i18n', action: '*',
              actionExclude: 'login|authenticate')
        {
            before = {
				if (!session?.user) {
					redirect(controller: 'user', action: 'login')
					return false
				}
            }
        }

		permission(controller: '*',
                   controllerExclude: 'notification|searchable|overview|i18n',
                   action: '*',
                   actionExclude: 'login|authenticate|logout')
        {
			before = {
				User user = session?.user
				if (user && controllerName) {
					if (!user.checkAllowedControllers([controllerName])) {
						redirect(uri: '/forbidden.gsp')
						return false
					}
				}
			}
		}
    }

}
