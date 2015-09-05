/*
 * InvoiceSpec.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.amcworld.springcrm

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(Invoice)
@Mock([Invoice, InvoicingTransaction, CreditMemo])

class InvoiceSpec extends Specification {
	
	//-- Instance variables ----------------
	
	Invoice i = new Invoice(
		adjustment: 0.54,
		billingAddr: new Address(),
		discountAmount: 5,
		discountPercent: 2,
		dueDatePayment: new Date(),
		items: [
			new InvoicingItem(
				number: 'P-10000', quantity: 4, unit: 'pcs.',
				name: 'books', unitPrice: 44.99, tax: 19
			),
			new InvoicingItem(
				number: 'P-20000', quantity: 10.5, unit: 'm',
				name: 'tape', unitPrice: 0.89, tax: 7
			),
			new InvoicingItem(
				number: 'S-10100', quantity: 4.25, unit: 'h',
				name: 'repairing', unitPrice: 39, tax: 19
			)
		],
		organization: new Organization(
			number: 10405, recType: 1, name: 'YourOrganization Ltd.',
			billingAddr: new Address(), shippingAddr: new Address()
		),
		paymentAmount: 100.0d,
		total: 100.0d,
		type: 'I',
		shippingAddr: new Address(),
		shippingCosts: 4.5,
		shippingTax: 7,
		stage: new InvoiceStage(),
		subject: 'invoice',
		creditMemos: new CreditMemo(paymentAmount: 12, stage: new CreditMemoStage())
	)
	
	//-- Feature Methods -------------------
	
	def 'Compute the balance'() {
		given: 'a mocked UserService'
		def userService = Mock(UserService)
		userService.getNumFractionDigitsExt() >> 3
		i.userService = userService

		when: 'I set some payment amounts and total values'		
		i.paymentAmount = pa
		i.total = total
		
		then: 'I get the correct balance'
		e == i.getBalance()
		
		where:
		pa	| total	| e
		10	| 10	| 0
		10	| -10	| 20
		4	| 2		| 2
		0.2	| 0.1	| 0.1
		-4	| 16	| -20
		-2	| 5331	| -5333
		0	| 16	| -16
		16	| 0 	| 16
	}
	
	def 'Compute the closing balance'() {
		given: 'a mocked UserService'
		
		def userService = Mock(UserService)
		userService.getNumFractionDigitsExt() >> 3
		i.userService = userService
		
		expect:
		println i.getClosingBalance()
	}
	
	def 'Get the balance color'() {
	
	}
	
	def 'Get the color indicated by date'() {
		given:
		i.dueDatePayment == new Date()
		
		expect:
		'orange' == i.colorIndicatorByDate()
		
		when:
		i.dueDatePayment = i.dueDatePayment.minus(5)
		
		then:
		'red' == i.colorIndicatorByDate()
		
		when:
		i.dueDatePayment = i.dueDatePayment.plus(8)
		
		then:
		'yellow' == i.colorIndicatorByDate()
		
		when:
		i.dueDatePayment = i.dueDatePayment.plus(2)
		
		then:
		'default' == i.colorIndicatorByDate()
	}
	
	def 'Due date payment constraints'() {
		setup:
		mockForConstraintsTests(Invoice)
		
		when: 'I set a date and validate it'
		i.dueDatePayment = new Date()
		i.validate()
		
		then: 'it is valid'
		!i.hasErrors()
		
		when: 'I unset the date and validate it'
		i.dueDatePayment = null
		i.validate()
		
		then: 'it is not valid'
		i.hasErrors()
	}
	
	def 'Payment date constraints'() {
		setup:
		mockForConstraintsTests(Invoice)
		
		when: 'I set a payment date and validate it'
		i.paymentDate = new Date()
		i.validate()
		
		then: 'it is valid'
		!i.hasErrors()
		
		when: 'I unset the payment date and validate it'
		i.paymentDate = null
		i.validate()
		
		then: 'it is valid'
		!i.hasErrors()
	}
	
	def 'Payment amount constraints'(){
		setup:
		mockForConstraintsTests(Invoice)
		
		when: 'I set a payment amount and validate it'
		i.paymentAmount = pa
		i.validate()
		
		then:
		!valid == i.hasErrors()
		
		where:
		pa			| valid
		-108.56     | false
		  -5.44     | false
		  -1.00     | false
		  -0.01     | false
		   0.00     | true
		   0.01     | true
		   1.00     | true
		   5.44     | true
		 108.56     | true
		 229.45     | true
	}
}