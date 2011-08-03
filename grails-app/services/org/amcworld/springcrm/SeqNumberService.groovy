package org.amcworld.springcrm

import org.springframework.transaction.annotation.Transactional

class SeqNumberService {

    static transactional = true


	//-- Public methods -------------------------

	/**
	 * Retrieves the next available sequence number for the given controller
	 * name.
	 * 
	 * @param controllerName	the given controller name
	 * @return					the next available sequence number
	 */
	@Transactional(readOnly = true)
    BigInteger nextNumber(String controllerName) {
		def seqNumberInstance = SeqNumber.findByControllerName(controllerName)
		return seqNumberInstance ? seqNumberInstance.nextNumber : 1 
    }

	/**
	 * Retrieves the next available sequence number for the controller which is
	 * associated to the given class.
	 * 
	 * @param cls	the given class
	 * @return		the next available sequence number
	 */
	@Transactional(readOnly = true)
	BigInteger nextNumber(Class cls) {
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
	String format(String controllerName, BigInteger number) {
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
	String format(Class cls, BigInteger number) {
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
	String formatWithPrefix(String controllerName, BigInteger number) {
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
	String formatWithPrefix(Class cls, BigInteger number) {
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
	String formatWithSuffix(String controllerName, BigInteger number) {
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
	String formatWithSuffix(Class cls, BigInteger number) {
		return formatWithSuffix(classToControllerName(cls), number)
	}

	/**
	 * Increments the sequence number for the given controller and stores it
	 * in the database. If no entry for the controller exists a new one with
	 * a sequence number of 2 is created.
	 * 
	 * @param controllerName	the given controller name
	 */
	@Transactional
	void stepFurther(String controllerName) {
		def seqNumberInstance = SeqNumber.findByControllerName(controllerName)
		if (seqNumberInstance) {
			seqNumberInstance.nextNumber++
		} else {
			seqNumberInstance = new SeqNumber(
				controllerName:controllerName, startNumber:1, nextNumber:2
			)
		}
		seqNumberInstance.save(flush:true)
	}


	//-- Non-public methods ---------------------

	/**
	 * Obtains the name of the controller which is associated to the given
	 * class.
	 * 
	 * @param cls	the given class
	 * @return		the associated controller name
	 */
	protected static String classToControllerName(Class cls) {
		String className = cls.simpleName
		StringBuilder buf = new StringBuilder(className[0].toLowerCase())
		buf << className.substring(1)
		return buf.toString()
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
		BigInteger number = args.number
		boolean withPrefix = args.withPrefix ?: true
		boolean withSuffix = args.withSuffix ?: true

		def seqNumberInstance = SeqNumber.findByControllerName(controllerName)
		if (seqNumberInstance) {
			def s = new StringBuilder()
			if (withPrefix) s << seqNumberInstance.prefix
			if (s != '') s << '-'
			s << number ?: seqNumberInstance.nextNumber
			if (withSuffix && (seqNumberInstance.suffix != '')) {
				s << '-' << seqNumberInstance.suffix
			}
			return s.toString()
		} else {
			return (number ?: 1).toString()
		}
	}
}
