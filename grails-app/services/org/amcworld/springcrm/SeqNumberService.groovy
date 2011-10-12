package org.amcworld.springcrm

import org.codehaus.groovy.grails.commons.ArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsClass
import org.springframework.transaction.annotation.Transactional

class SeqNumberService {

    static transactional = true
	def grailsApplication


	//-- Public methods -------------------------

	/**
	 * Retrieves the next available sequence number for the given controller
	 * name.
	 * 
	 * @param controllerName	the given controller name
	 * @return					the next available sequence number
	 */
	@Transactional(readOnly = true)
    int nextNumber(String controllerName) {
		SeqNumber seq = loadSeqNumber(controllerName)
		GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
			'Domain', controllerName
		)
		Integer num
		try {
			num = cls.clazz.'maxNumber'(seq)
		} catch (Exception e) {
			def c = cls.clazz.'createCriteria'()
			num = c.get {
				projections {
					max('number')
				}
				between('number', seq.startValue, seq.endValue)
			}
		}
		return (num == null || num < seq.startValue) ? seq.startValue : num + 1
    }

	/**
	 * Retrieves the next available sequence number for the controller which is
	 * associated to the given class.
	 * 
	 * @param cls	the given class
	 * @return		the next available sequence number
	 */
	@Transactional(readOnly = true)
	int nextNumber(Class cls) {
		return nextNumber(classToControllerName(cls))
	}

	/**
	 * Returns the next sequence number for the given controller formatted with
	 * prefix and suffix, if any.
	 * 
	 * @param controllerName	the given controller name
	 * @return					the formatted next sequence number
	 */
	@Transactional(readOnly = true)
	String nextFullNumber(String controllerName) {
		return formatNumber([controllerName:controllerName])
	}
	
	/**
	 * Returns the next sequence number for the controller which is associated
	 * to the given class formatted with prefix and suffix, if any.
	 * 
	 * @param cls	the given class
	 * @return		the formatted next sequence number
	 */
	@Transactional(readOnly = true)
	String nextFullNumber(Class cls) {
		return nextFullNumber(classToControllerName(cls))
	}

	/**
	 * Loads the sequence number data for the given controller.
	 *  
	 * @param controllerName	the given controller name
	 * @return					the sequence number data; <code>null</code> if
	 * 							no such data are stored for the given
	 * 							controller
	 */
	@Transactional(readOnly = true)
	SeqNumber loadSeqNumber(String controllerName) {
		return SeqNumber.findByControllerName(controllerName)
	}
	
	/**
	 * Loads the sequence number data for the controller which is associated to
	 * the given class.
	 *  
	 * @param controllerName	the given class
	 * @return					the sequence number data; <code>null</code> if
	 * 							no such data are stored for the controller
	 */
	@Transactional(readOnly = true)
	SeqNumber loadSeqNumber(Class cls) {
		return loadSeqNumber(classToControllerName(cls))
	}

	/**
	 * Formats the given sequence number as specified in the number schema for
	 * the given controller.
	 * 
	 * @param controllerName	the given controller name
	 * @param number			the given number
	 * @return					the formatted number
	 */
	@Transactional(readOnly = true)
	String format(String controllerName, int number) {
		return formatNumber([controllerName:controllerName, number:number])
	}
	
	/**
	 * Formats the given sequence number as specified in the number schema for
	 * the controller which is associated to the given class.
	 * 
	 * @param cls		the given class
	 * @param number	the given number
	 * @return			the formatted number
	 */
	@Transactional(readOnly = true)
	String format(Class cls, int number) {
		return format(classToControllerName(cls), number)
	}
	
	/**
	 * Formats the given sequence number with the prefix which is defined for
	 * the given controller.
	 * 
	 * @param controllerName	the given controller name
	 * @param number			the given number
	 * @return					the formatted number
	 */
	@Transactional(readOnly = true)
	String formatWithPrefix(String controllerName, int number) {
		return formatNumber(
			[controllerName:controllerName, number:number, withSuffix:false]
		)
	}
	
	/**
	 * Formats the given sequence number with the prefix which is defined for
	 * the controller which is associated to the given class.
	 * 
	 * @param cls		the given class
	 * @param number	the given number
	 * @return			the formatted number
	 */
	@Transactional(readOnly = true)
	String formatWithPrefix(Class cls, int number) {
		return formatWithPrefix(classToControllerName(cls), number)
	}

	/**
	 * Formats the given sequence number with the suffix which is defined for
	 * the given controller.
	 * 
	 * @param controllerName	the given controller name
	 * @param number			the given number
	 * @return					the formatted number
	 */
	@Transactional(readOnly = true)
	String formatWithSuffix(String controllerName, int number) {
		return formatNumber(
			[controllerName:controllerName, number:number, withPrefix:false]
		)
	}
	
	/**
	 * Formats the given sequence number with the suffix which is defined for
	 * the controller which is associated to the given class.
	 * 
	 * @param cls		the given class
	 * @param number	the given number
	 * @return			the formatted number
	 */
	@Transactional(readOnly = true)
	String formatWithSuffix(Class cls, int number) {
		return formatWithSuffix(classToControllerName(cls), number)
	}


	//-- Non-public methods ---------------------

	/**
	 * Obtains the name of the controller which is associated to the given
	 * class.
	 * 
	 * @param cls	the given class
	 * @return		the associated controller name
	 */
	protected String classToControllerName(Class cls) {
		ArtefactHandler handler = grailsApplication.getArtefactType(cls)
		GrailsClass gc = grailsApplication.getArtefact(handler.type, cls.name)
		return gc?.logicalPropertyName
	}

	/**
	 * Formats a sequence number as specified in the given arguments.
	 * 
	 * @param controllerName	the name of the controller that sequence number
	 * 							is to return
	 * @param number			the number to format; if not specified the next
	 * 							available sequence number for the given
	 * 							controller is used
	 * @param withPrefix		if <code>true</code> or not specified the
	 * 							prefix is added to the returned string
	 * @param withSuffix		if <code>true</code> or not specified the
	 * 							suffix is added to the returned string
	 * @return					the formatted sequence number
	 */
	protected String formatNumber(Map args) {
		if (args == null) args = [:]
		String controllerName = args.controllerName
		Integer number = args.number
		boolean withPrefix = args.withPrefix ?: true
		boolean withSuffix = args.withSuffix ?: true

		def seqNumberInstance = SeqNumber.findByControllerName(controllerName)
		if (seqNumberInstance) {
			def s = new StringBuilder()
			if (withPrefix) s << seqNumberInstance.prefix
			if (s != '') s << '-'
			s << number ?: nextNumber(controllerName)
			if (withSuffix && (seqNumberInstance.suffix != '')) {
				s << '-' << seqNumberInstance.suffix
			}
			return s.toString()
		} else {
			return (number ?: 1).toString()
		}
	}
}
