package org.amcworld.springcrm

import org.springframework.transaction.annotation.Transactional

class SeqNumberService {

    static transactional = true

	@Transactional(readOnly = true)
    BigInteger nextNumber(String controllerName) {
		def seqNumberInstance = SeqNumber.findByControllerName(controllerName)
		return seqNumberInstance ? seqNumberInstance.nextNumber : 1 
    }
	
	@Transactional(readOnly = true)
	BigInteger nextNumber(Class cls) {
		return nextNumber(classToControllerName(cls))
	}

	@Transactional(readOnly = true)
	String nextFullNumber(String controllerName) {
		return formatNumber([controllerName:controllerName])
	}
	
	@Transactional(readOnly = true)
	String nextFullNumber(Class cls) {
		return nextFullNumber(classToControllerName(cls))
	}

	@Transactional(readOnly = true)
	SeqNumber loadSeqNumber(String controllerName) {
		return SeqNumber.findByControllerName(controllerName)
	}
	
	@Transactional(readOnly = true)
	SeqNumber loadSeqNumber(Class cls) {
		return loadSeqNumber(classToControllerName(cls))
	}

	@Transactional(readOnly = true)
	String format(String controllerName, BigInteger number) {
		return formatNumber([controllerName:controllerName, number:number])
	}
	
	@Transactional(readOnly = true)
	String format(Class cls, BigInteger number) {
		return format(classToControllerName(cls), number)
	}
	
	@Transactional(readOnly = true)
	String formatWithPrefix(String controllerName, BigInteger number) {
		return formatNumber(
			[controllerName:controllerName, number:number, withSuffix:false]
		)
	}
	
	@Transactional(readOnly = true)
	String formatWithPrefix(Class cls, BigInteger number) {
		return formatWithPrefix(classToControllerName(cls), number)
	}

	@Transactional(readOnly = true)
	String formatWithSuffix(String controllerName, BigInteger number) {
		return formatNumber(
			[controllerName:controllerName, number:number, withPrefix:false]
		)
	}
	
	@Transactional(readOnly = true)
	String formatWithSuffix(Class cls, BigInteger number) {
		return formatWithSuffix(classToControllerName(cls), number)
	}

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
		seqNumberInstance.save(failOnError:true)
	}


	//-- Non-public methods ---------------------

	protected static String classToControllerName(Class cls) {
		String className = cls.simpleName
		StringBuilder buf = new StringBuilder(className[0].toLowerCase())
		buf << className.substring(1)
		return buf.toString()
	}

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
