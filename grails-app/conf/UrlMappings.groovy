class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		'/'(controller:'overview', action:'index')
		'403'(view:'/forbidden')
//		'500'(view:'/error')
	}
}
