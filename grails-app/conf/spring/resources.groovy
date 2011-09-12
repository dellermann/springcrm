// Place your Spring DSL code here
beans = {
    appEditorRegistrar(org.amcworld.springcrm.util.AppPropertyEditorRegistrar) { 
		messageSource = ref('messageSource') 
	} 
}
