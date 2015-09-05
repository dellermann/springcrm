/*
 * PurchaseInvoiceSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(PurchaseInvoice)
@Mock([PurchaseInvoice, UserService])
class PurchaseInvoiceSpec extends Specification {
	
	//-- Instance Variables -----------------------
	
	PurchaseInvoice p = new PurchaseInvoice(
		number: 225,
		subject: 'International delivery',
		vendorName: 'Sugar & salt & bicycle vendor',
		dueDate: new Date(),
		stage: new PurchaseInvoiceStage(),
		paymentDate: new Date(),
		paymentAmount: 1250.0d,
		items: [
			new PurchaseInvoiceItem(
				quantity: 5, unit: 'kg', name: 'salt', 
				description: 'Fair trade salt from Egypt',
				unitPrice: 2, tax: 17
			),
			new PurchaseInvoiceItem(
				quantity: 3, unit: 'kg', name: 'sugar',
				description: 'Fair trade sugar from Uganda',
				unitPrice: 3, tax: 17
			),
			new PurchaseInvoiceItem(
				quantity: 1, unit: 'piece', name: 'bicycle',
				description: 'Bicycle from the Netherlands',
				unitPrice: 633, tax: 17
			)
		],
		discountPercent: 0,
		discountAmount: 0,
		shippingCosts: 15,
		adjustment: 0.05,
		notes: 'shipping not including',
		total: 495,
		dateCreated: new Date(),
		lastUpdated: new Date()
	)
	
	
	//-- Feature Methods --------------------------
	
	def 'Copy using constructor'() {
		when:
		def p2 = new PurchaseInvoice(p)
		
		then: 'some properties are set'
		p2.number == p.number
		p2.subject == p.subject
		p2.vendor == p.vendor
		p2.vendorName == p.vendorName
		p2.items == p.items
		p2.discountPercent == p.discountPercent
		p2.discountAmount == p.discountAmount
		p2.shippingCosts == p.shippingCosts
		p2.shippingTax == p.shippingTax
		p2.adjustment == p.adjustment
		p2.notes == p.notes
		p2.total == p.total
		
		and: 'some properties are unset'
		!p2.dateCreated
		!p2.lastUpdated
		!p2.dueDate
	}
	
	def 'Compute the balance'() {
		given: 'a mocked UserService'
		def userService = Mock(UserService)
		userService.getNumFractionDigitsExt() >> 3
		p.userService = userService

		when: 'I set some payment amounts and total values'		
		p.paymentAmount = pa
		p.total = total
		
		then: 'I get the correct balance'
		e == p.getBalance()
		
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
	
	def 'Compute the color based on the balance'() {
		given: 'a mocked UserService'
		def userService = Mock(UserService)
		userService.getNumFractionDigitsExt() >> 3
		p.userService = userService
		
		when: 'the balance is positive'
		p.paymentAmount = 14
		p.total = 3
		
		then: 'the color is green'
		p.balanceColor == 'green'
		
		when: 'the balance is negative'
		p.paymentAmount = 3
		p.total = 14
		
		then: 'the color is red'
		p.balanceColor == 'red'
		
		when: 'I the balance is 0.0d'
		p.paymentAmount = 0
		p.total = 0
		
		then: 'the color is default'
		p.balanceColor == 'default'
	}
	
	def 'Compute the color based on the payment state'() {
		given: 'a mocked UserService'
		def userService = Mock(UserService)
		userService.getNumFractionDigitsExt() >> 3
		p.userService = userService
		
		when: 'I set the stage id to 2103'
		p.stage.id = 2103
		
		then: 'the color is purple'
		p.paymentStateColor == 'purple'
		
		when: 'I set the stage id to 2102'
		p.stage.id = 2102
		
		and: 'have a balance >= 0.0d'
		p.paymentAmount = 13
		p.total = 3
		
		then: 'the color is green'
		p.paymentStateColor == 'green'
		
		when: 'I set the stage id to 2102'
		p.stage.id = 2102
		
		and: 'have a balance < 0.0d'
		p.paymentAmount = 3
		p.total = 13
		
		then: 'the color is red'
		p.paymentStateColor == 'orange'
		
		when: 'I set the stage id to 0'
		p.stage.id = 0
		
		then: 'the color is default'
		p.paymentStateColor == 'default'
		
	}
	
	def 'Compute total'() {
        given: 'the discrete discount percent, amount, and adjustment values'
        p.discountPercent = dp
        p.discountAmount = da
        p.adjustment = a

        expect: 'the correct total price'
        e == p.computeTotal()

        where:
        dp  | da    |  a    |   e
         0  |  0.00 |  0.0  | 780.69
         0  |  0.00 |  0.58 | 781.2700000000001
         0  |  0.00 | -1.12 | 779.57
         0  |  0.18 |  0.0  | 780.5100000000001
         0  |  1.00 |  0.58 | 780.2700000000001
         0  |  4.50 | -1.12 | 775.07
         2  |  0.18 |  0.0  | 764.8962000000001
         2  |  1.00 |  0.58 | 764.6562000000001
         2  |  4.50 | -1.12 | 759.4562000000001
        10  |  0.18 |  0.0  | 702.4410000000001
        10  |  1.00 |  0.58 | 702.2010000000001
        10  |  4.50 | -1.12 | 697.0010000000001
    }
	
	def 'Simulate the validate method and check total'() {
		given: 'the discrete discount percent, amount, and adjustment values'
		p.discountPercent = dp
		p.discountAmount = da
		p.adjustment = a

		when: 'I simulate calling validate()'
		p.beforeValidate()

		then: 'the total property must be set'
		e == p.total

		where:
		 dp  | da    |  a    |   e
         0  |  0.00 |  0.0  | 780.69
         0  |  0.00 |  0.58 | 781.2700000000001
         0  |  0.00 | -1.12 | 779.57
         0  |  0.18 |  0.0  | 780.5100000000001
         0  |  1.00 |  0.58 | 780.2700000000001
         0  |  4.50 | -1.12 | 775.07
         2  |  0.18 |  0.0  | 764.8962000000001
         2  |  1.00 |  0.58 | 764.6562000000001
         2  |  4.50 | -1.12 | 759.4562000000001
        10  |  0.18 |  0.0  | 702.4410000000001
        10  |  1.00 |  0.58 | 702.2010000000001
        10  |  4.50 | -1.12 | 697.0010000000001
	}
	
	def 'Simulate the save method in insert mode and check total'() {
		given: 'the discrete discount percent, amount, and adjustment values'
		p.discountPercent = dp
		p.discountAmount = da
		p.adjustment = a

		when: 'I simulate calling save() in insert mode'
		p.beforeValidate()

		then: 'the total property must be set'
		e == p.total

		where:
		 dp  | da    |  a    |   e
		 0  |  0.00 |  0.0  | 780.69
		 0  |  0.00 |  0.58 | 781.2700000000001
		 0  |  0.00 | -1.12 | 779.57
		 0  |  0.18 |  0.0  | 780.5100000000001
		 0  |  1.00 |  0.58 | 780.2700000000001
		 0  |  4.50 | -1.12 | 775.07
		 2  |  0.18 |  0.0  | 764.8962000000001
		 2  |  1.00 |  0.58 | 764.6562000000001
		 2  |  4.50 | -1.12 | 759.4562000000001
		10  |  0.18 |  0.0  | 702.4410000000001
		10  |  1.00 |  0.58 | 702.2010000000001
		10  |  4.50 | -1.12 | 697.0010000000001
	}
	
	def 'Simulate the save method in update mode and check total'() {
		given: 'the discrete discount percent, discount amount and adjustment values'
		p.discountPercent = dp
		p.discountAmount = da
		p.adjustment = a

		when: 'I simulate calling save() in update mode'
		p.beforeValidate()

		then: 'the total property must be set'
		e == p.total

		where:
		 dp  | da    |  a    |   e
		 0  |  0.00 |  0.0  | 780.69
		 0  |  0.00 |  0.58 | 781.2700000000001
		 0  |  0.00 | -1.12 | 779.57
		 0  |  0.18 |  0.0  | 780.5100000000001
		 0  |  1.00 |  0.58 | 780.2700000000001
		 0  |  4.50 | -1.12 | 775.07
		 2  |  0.18 |  0.0  | 764.8962000000001
		 2  |  1.00 |  0.58 | 764.6562000000001
		 2  |  4.50 | -1.12 | 759.4562000000001
		10  |  0.18 |  0.0  | 702.4410000000001
		10  |  1.00 |  0.58 | 702.2010000000001
		10  |  4.50 | -1.12 | 697.0010000000001
	}
	
	def 'Compute subtotal net'() {
		expect:
		667.0 == p.subtotalNet
	}
	
	def 'Compute tax rate sums'() {
		when: 'I compute the sums for each tax rate'
		Map t = p.taxRateSums

		then: 'I get an ordered map containing the tax rates and their sums'
		t instanceof LinkedHashMap
		([17d: 110.84d, 19d: 2.85d] as LinkedHashMap) == t
	}
	
	def 'Compute subtotal gross'() {
		expect:
		780.69 == p.subtotalGross
	}
	
	def 'Compute discount percent amount'() {
		given: 'the discrete percentage values'
		p.discountPercent = d

		expect: 'the correct discount amount'
		e == p.discountPercentAmount

		where:
		 d  | 				  e
		 0  |  				0.0
		 2  |15.613800000000001
		10  |			 78.069
	}
	
	def 'Check for equality'() {
        given: 'another purchase invoice with different properties'
        def p2 = new PurchaseInvoice(
			number: 226,
			subject: 'National delivery',
			vendorName: 'Honey vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 120.0d,
			items: [
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'kg', name: 'honey',
					description: 'Fair trade honey from France',
					unitPrice: 1.5d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: 13,
			adjustment: 0.03,
			notes: 'shipping including',
			total: 425,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		
		and: 'same IDs'
		p.id = 500
		p2.id = 500
		
		expect: 'both these objects to be equal'
		p == p2
		p2 == p 
    }
	
	def 'Check for inequality'() {
		given: 'I create another purchase invoice with same properties'
		def p2 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 1250.0d,
			items: [
				new PurchaseInvoiceItem(
					quantity: 5, unit: 'kg', name: 'salt',
					description: 'Fair trade salt from Egypt',
					unitPrice: 2.5d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 3, unit: 'kg', name: 'sugar',
					description: 'Fair trade sugar from Uganda',
					unitPrice: 3.8d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'piece', name: 'bicycle',
					description: 'Bicycle from the Netherlands',
					unitPrice: 633.8d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: 15,
			adjustment: 0.05,
			notes: 'shipping not including',
			total: 495,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		
		and: 'different IDs'
		p.id = 600
		p2.id = 500
		
		when: 'I compare both these purchase invoices'
		def b1 = (p != p2)
		def b2 = (p2 != p)
		
		then: 'they are not equal'
		b1
		b2
		
		when: 'I compare with null'
		p2 = null
		
		then: 'they are not equal'
		p2 != p
		p != p2
		
		when: 'I compare with another type'
		int i = 4
		
		then: 'they are not equal'
		p != i
	}
	
	def 'Compute hash code'() {
		when: 'I create a purchase invoice with no ID'
		def p = new PurchaseInvoice()

		then: 'I get a valid hash code'
		0 == p.hashCode()

		when: 'I create a purchase invoice with discrete IDs'
		p.id = id

		then: 'I get a hash code using this ID'
		e == p.hashCode()

		where:
		   id |     e
			0 |     0
			1 |     1
		   10 |    10
		  105 |   105
		 9404 |  9404
		37603 | 37603
	}
	
	def 'Convert to string'() {
		when: 'I set the subject of a purchase invoice'
		p.subject = 'Sleeping bag'
		
		then:
		p.toString() == 'Sleeping bag'
	}
	
	def 'Get the color indicated by date'() {
		given:
		p.dueDate == new Date()
		
		expect:
		'orange' == p.colorIndicatorByDate()
		
		when:
		p.dueDate = p.dueDate.minus(5)
		
		then:
		'red' == p.colorIndicatorByDate()
		
		when:
		p.dueDate = p.dueDate.plus(8)
		
		then:
		'yellow' == p.colorIndicatorByDate()
		
		when:
		p.dueDate = p.dueDate.plus(2)
		
		then:
		'default' == p.colorIndicatorByDate()
	}
	
	def 'Number constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set a number and validate it'
		p.number = number
		p.validate()
		
		then: 
		!valid == p.hasErrors()
		
		where:
		number			| valid
		null            | false
		''              | false
		' '             | false
		'      '        | false
		'  \t \n '      | false
		'foo'           | true
		'any name'      | true
		'1004'			| true
		'abc'*100		| true
	}
	
	def 'Subject constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set a subject and validate it'
		p.subject = subject
		p.validate()
		
		then: 
		!valid == p.hasErrors()
		
		where:
		subject			| valid
		null            | false
		''              | false
		' '             | false
		'      '        | false
		'  \t \n '      | false
		'foo'           | true
		'any name'      | true
		'1004'			| true
		'abc'*100		| true
	}
	
	def 'Vendor constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		def p1 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 1005.05,
			items: [
				new PurchaseInvoiceItem(
					quantity: 5, unit: 'kg', name: 'salt',
					description: 'Fair trade salt from Egypt',
					unitPrice: 2.5d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 3, unit: 'kg', name: 'sugar',
					description: 'Fair trade sugar from Uganda',
					unitPrice: 3.8d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'piece', name: 'bicycle',
					description: 'Bicycle from the Netherlands',
					unitPrice: 633.8d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: 15,
			adjustment: 0.05,
			notes: 'shipping not including',
			total: 495,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		
		when: 'I set a vendor and validate it'
		p1.vendor = new Organization(
			recType: 1, name: 'foo', billingAddr: new Address(),
			shippingAddr: new Address()
		)
		p1.validate()
		
		then:
		!p1.hasErrors()
	}
	
	def 'Vendor name constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set a vendor name and validate it'
		p.vendorName = vn
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		vn				| valid
		null            | false
		''              | false
		' '             | false
		'      '        | false
		'  \t \n '      | false
		'foo'           | true
		'any name'      | true
		'1004'			| true
		'abc'*100		| true
	}
	
	def 'DocDate constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)

		when: 'I set a document date and validate it'
		p.docDate = new Date()
		p.validate()

		then: 'it is valid'
		!p.hasErrors()

		when: 'I unset the document date and validate it'
		p.docDate = null
		p.validate()

		then: 'it is not valid'
		p.hasErrors()
	}
	
	def 'DueDate constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)

		when: 'I set a due date and validate it'
		p.dueDate = new Date()
		p.validate()

		then: 'it is valid'
		!p.hasErrors()

		when: 'I unset the due date and validate it'
		p.dueDate = null
		p.validate()

		then: 'it is not valid'
		p.hasErrors()
	}
	
	def 'PaymentDate constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)

		when: 'I set a payment date and validate it'
		p.paymentDate = new Date()
		p.validate()

		then: 'it is valid'
		!p.hasErrors()

		when: 'I unset the payment date and validate it'
		p.paymentDate = null
		p.validate()

		then: 'it is valid'
		!p.hasErrors()
	}
	
	def 'Payment amount constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set a payment amount and validate it'
		
		def p1 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: pa,
			items: [
				new PurchaseInvoiceItem(
					quantity: 5, unit: 'kg', name: 'salt',
					description: 'Fair trade salt from Egypt',
					unitPrice: 2.5d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 3, unit: 'kg', name: 'sugar',
					description: 'Fair trade sugar from Uganda',
					unitPrice: 3.8d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'piece', name: 'bicycle',
					description: 'Bicycle from the Netherlands',
					unitPrice: 633.8d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: 15,
			adjustment: 0.05,
			notes: 'shipping not including',
			total: 495,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		p1.validate()
		
		then:
		!valid == p1.hasErrors()
		
		where:
		pa				| valid
		null            | true
		''              | true
		' '             | true
		'      '        | true
		'  \t \n '      | true
		-5				| true
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
	}
	
	def 'Purchase invoice items constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		def p1 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 1250.0d,
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: 15,
			adjustment: 0.05,
			notes: 'shipping not including',
			total: 495,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		
		when: 'I set one item and validate it'
		p1.items = [
			new PurchaseInvoiceItem(
				quantity: 5, unit: 'kg', name: 'salt',
				description: 'Fair trade salt from Egypt',
				unitPrice: 2.5d, tax: 17
			)
		]
		p1.validate()
		
		then: 'it is valid'
		!p1.hasErrors()
		
		when: 'I set two items and validate it'
		p1.items = [
			new PurchaseInvoiceItem(
				quantity: 5, unit: 'kg', name: 'salt',
				description: 'Fair trade salt from Egypt',
				unitPrice: 2.5d, tax: 17
			),
			new PurchaseInvoiceItem(
				quantity: 3, unit: 'kg', name: 'sugar',
				description: 'Fair trade sugar from Uganda',
				unitPrice: 3.8d, tax: 17
			)
		]
		p1.validate()
		
		then: 'it is valid'
		!p1.hasErrors()
		
		when: 'I set no items and validate it'
		p1.items = []
		p1.validate()
		
		then: 'it is not valid'
		p1.hasErrors()
		
		when: 'I unset the items and validate it'
		p1.items = null
		p1.validate()
		
		then: 'it is valid'
		!p1.hasErrors()		// TODO is this really intended?
	}
	
	def 'Notes constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set a note and validate it'
		p.notes = notes
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		notes			| valid
		null            | true
		''              | true
		' '             | true
		'      '        | true
		'  \t \n '      | true
		'foo'           | true
		'any name'      | true
		'1004'			| true
		'abc'*100		| true
	}
	
	def 'DocumentFile constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when:
		p.documentFile = new DataFile(name: 'data name')
		p.validate()
		
		then: 'it is valid'
		!p.hasErrors()
	}
	
	def 'DiscountPercent constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)

		when:
		p.discountPercent = dp
		p.validate()

		then:
		!valid == p.hasErrors()

		where:
		  dp        | valid
		-100        | false
		  -5        | false
		  -1        | false
		  -0.005    | false
		   0        | true
		   0.005    | true
		   1        | true
		   5        | true
		 100        | true
		 200        | true
	}
	
	def 'Discount amount constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set a discount amount and validate it'
		
		def p1 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 16,
			items: [
				new PurchaseInvoiceItem(
					quantity: 5, unit: 'kg', name: 'salt',
					description: 'Fair trade salt from Egypt',
					unitPrice: 2.5d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 3, unit: 'kg', name: 'sugar',
					description: 'Fair trade sugar from Uganda',
					unitPrice: 3.8d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'piece', name: 'bicycle',
					description: 'Bicycle from the Netherlands',
					unitPrice: 633.8d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: da,
			shippingCosts: 15,
			adjustment: 0.05,
			notes: 'shipping not including',
			total: 495,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		p1.validate()
		
		then:
		!valid == p1.hasErrors()
		
		where:
		da				| valid
		null            | true
		''              | true
		' '             | true
		'      '        | true
		'  \t \n '      | true
		-120034.005		| false
		-5				| false
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
	}
	
	def 'Shipping costs constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set the shipping costs and validate it'
		
		def p1 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 16,
			items: [
				new PurchaseInvoiceItem(
					quantity: 5, unit: 'kg', name: 'salt',
					description: 'Fair trade salt from Egypt',
					unitPrice: 2.5d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 3, unit: 'kg', name: 'sugar',
					description: 'Fair trade sugar from Uganda',
					unitPrice: 3.8d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'piece', name: 'bicycle',
					description: 'Bicycle from the Netherlands',
					unitPrice: 633.8d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: sa,
			adjustment: 0.05,
			notes: 'shipping not including',
			total: 495,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		p1.validate()
		
		then:
		!valid == p1.hasErrors()
		
		where:
		sa				| valid
		null            | true
		''              | true
		' '             | true
		'      '        | true
		'  \t \n '      | true
		-120034.005		| false
		-5				| false
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
	}
	
	def 'Shipping tax constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set the shipping tax and validate it'
		
		def p1 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 16,
			items: [
				new PurchaseInvoiceItem(
					quantity: 5, unit: 'kg', name: 'salt',
					description: 'Fair trade salt from Egypt',
					unitPrice: 2.5d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 3, unit: 'kg', name: 'sugar',
					description: 'Fair trade sugar from Uganda',
					unitPrice: 3.8d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'piece', name: 'bicycle',
					description: 'Bicycle from the Netherlands',
					unitPrice: 633.8d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: 15,
			shippingTax: st,
			adjustment: 0.05,
			notes: 'shipping not including',
			total: 495,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		p1.validate()
		
		then:
		!valid == p1.hasErrors()
		
		where:
		st				| valid
		null            | true
		''              | true
		' '             | true
		'      '        | true
		'  \t \n '      | true
		-120034.005		| false
		-5				| false
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
	}
	
	def 'Adjustment constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set an adjustment and validate it'
		
		def p1 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 16,
			items: [
				new PurchaseInvoiceItem(
					quantity: 5, unit: 'kg', name: 'salt',
					description: 'Fair trade salt from Egypt',
					unitPrice: 2.5d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 3, unit: 'kg', name: 'sugar',
					description: 'Fair trade sugar from Uganda',
					unitPrice: 3.8d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'piece', name: 'bicycle',
					description: 'Bicycle from the Netherlands',
					unitPrice: 633.8d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: 15,
			adjustment: adjustment,
			notes: 'shipping not including',
			total: 495,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		p1.validate()
		
		then:
		!valid == p1.hasErrors()
		
		where:
		adjustment		| valid
		null            | true
		''              | true
		' '             | true
		'      '        | true
		'  \t \n '      | true
		-120034.005		| true
		-5				| true
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
	}
	
	def 'Total constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)
		
		when: 'I set a total and validate it'
		
		def p1 = new PurchaseInvoice(
			number: 225,
			subject: 'International delivery',
			vendorName: 'Sugar & salt & bicycle vendor',
			dueDate: new Date(),
			stage: new PurchaseInvoiceStage(),
			paymentDate: new Date(),
			paymentAmount: 16,
			items: [
				new PurchaseInvoiceItem(
					quantity: 5, unit: 'kg', name: 'salt',
					description: 'Fair trade salt from Egypt',
					unitPrice: 2.5d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 3, unit: 'kg', name: 'sugar',
					description: 'Fair trade sugar from Uganda',
					unitPrice: 3.8d, tax: 17
				),
				new PurchaseInvoiceItem(
					quantity: 1, unit: 'piece', name: 'bicycle',
					description: 'Bicycle from the Netherlands',
					unitPrice: 633.8d, tax: 17
				)
			],
			discountPercent: 0,
			discountAmount: 0,
			shippingCosts: 15,
			adjustment: 0.05,
			notes: 'shipping not including',
			total: total,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		p1.validate()
		
		then:
		!valid == p1.hasErrors()
		
		where:
		total			| valid
		null            | true
		''              | true
		' '             | true
		'      '        | true
		'  \t \n '      | true
		-120034.005		| true
		-5				| true
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
	}
	
	def 'dateCreated constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)

		when: 'I set a dateCreated and validate it'
		p.dateCreated = new Date()
		p.validate()

		then: 'it is valid'
		!p.hasErrors()

		when: 'I unset the date and validate it'
		p.dateCreated = null
		p.validate()

		then: 'it is valid'
		!p.hasErrors()
	}
	
	def 'lastUpdated constraints'() {
		setup:
		mockForConstraintsTests(PurchaseInvoice)

		when: 'I set a lastUpdated and validate it'
		p.lastUpdated = new Date()
		p.validate()

		then: 'it is valid'
		!p.hasErrors()

		when: 'I unset the date and validate it'
		p.lastUpdated = null
		p.validate()

		then: 'it is valid'
		!p.hasErrors()
	}
}