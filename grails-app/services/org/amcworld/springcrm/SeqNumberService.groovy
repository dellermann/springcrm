package org.amcworld.springcrm

import org.springframework.transaction.annotation.Transactional

class SeqNumberService {

    static transactional = true

	@Transactional(readOnly = true)
    BigInteger nextNumber(Class cls) {
		def seqNumberInstance = SeqNumber.findByClassName(cls.name)
		return seqNumberInstance ? seqNumberInstance.nextNumber : 1 
    }
	
	@Transactional(readOnly = true)
	String nextFullNumber(Class cls) {
		return formatNumber([cls:cls])
	}
	
	@Transactional(readOnly = true)
	SeqNumber loadSeqNumber(Class cls) {
		return SeqNumber.findByClassName(cls.name)
	}
	
	@Transactional(readOnly = true)
	String format(Class cls, BigInteger number) {
		return formatNumber([cls:cls, number:number])
	}
	
	@Transactional(readOnly = true)
	String formatWithPrefix(Class cls, BigInteger number) {
		return formatNumber([cls:cls, number:number, withSuffix:false])
	}
	
	@Transactional(readOnly = true)
	String formatWithSuffix(Class cls, BigInteger number) {
		return formatNumber([cls:cls, number:number, withPrefix:false])
	}

	@Transactional
	void stepFurther(Class cls) {
		def seqNumberInstance = SeqNumber.findByClassName(cls.name)
		if (seqNumberInstance) {
			seqNumberInstance.nextNumber++
		} else {
			seqNumberInstance = 
				new SeqNumber(className:cls.name, startNumber:1, nextNumber:2)
		}
		seqNumberInstance.save(failOnError:true)
	}
	
	protected String formatNumber(Map args) {
		if (args == null) args = [:]
		Class cls = args.cls
		BigInteger number = args.number
		boolean withPrefix = args.withPrefix ?: true
		boolean withSuffix = args.withSuffix ?: true

		def seqNumberInstance = SeqNumber.findByClassName(cls.name)
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
