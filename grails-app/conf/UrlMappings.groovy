class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		'/'(view:'/index')
		'403'(view:'/forbidden')
//		'500'(view:'/error')
	}
}
