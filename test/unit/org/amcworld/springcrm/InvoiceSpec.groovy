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

	//-- Instance variables ---------------------

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


	//-- Feature Methods ------------------------

	def 'Compute balance'() {
		given: 'a mocked UserService'
		def userService = Mock(UserService)
		userService.getNumFractionDigitsExt() >> 3
		i.userService = userService

		when: 'I set some payment amounts and total values'
		i.paymentAmount = pa
		i.total = total

		then: 'I get the correct balance'
		b == i.balance

		where:
		pa	| total	| b
        0   | 0     | 0
        0	| 16	| -16
        16	| 0 	| 16
		10	| 10	| 0
		10	| -10	| 20
		4	| 2		| 2
		0.2	| 0.1	| 0.1
		-4	| 16	| -20
		-2	| 5331	| -5333
	}

	def 'Compute closing balance without changing credit memos'() {
		given: 'a mocked UserService'
        def userService = Mock(UserService)
        userService.getNumFractionDigitsExt() >> 3
	    i.userService = userService
        i.creditMemos*.userService = userService

        when: 'I set some payment amounts and total values'
        i.paymentAmount = pa
        i.total = total

        then: 'I get the correct closing balance'
        cb == i.closingBalance

        where:
        pa  | total | cb
        0   | 0     | -12
        0   | 16    | -28
        16  | 0     | 4
        10  | 10    | -12
        10  | -10   | 8
        4   | 2     | -10
        0.2 | 0.1   | -11.9
        -4  | 16    | -32
        -2  | 5331  | -5345
	}

    def 'Compute closing balance with changing credit memos'() {
        given: 'a mocked UserService'
        def userService = Mock(UserService)
        userService.getNumFractionDigitsExt() >> 3
        i.userService = userService
        i.creditMemos*.userService = userService

        when: 'I set some payment amounts and total values'
        i.paymentAmount = pa
        i.total = total
        i.creditMemos[0].paymentAmount = cpa
        i.creditMemos[0].total = ctotal

        then: 'I get the correct closing balance'
        cb == i.closingBalance

        where:
        pa  | total | cpa   | ctotal    | cb
        0   | 0     | 0     | 0         | 0
        0   | 0     | 0     | 16        | 16
        0   | 0     | 16    | 0         | -16
        0   | 0     | 10    | 10        | 0
        0   | 16    | 0     | 0         | -16
        0   | 16    | 0     | 16        | 0
        0   | 16    | 16    | 0         | -32
        0   | 16    | 10    | 10        | -16
        16  | 0     | 0     | 0         | 16
        16  | 0     | 0     | 16        | 32
        16  | 0     | 16    | 0         | 0
        16  | 0     | 10    | 10        | 16
        10  | 10    | 0     | 0         | 0
        10  | 10    | 0     | 16        | 16
        10  | 10    | 16    | 0         | -16
        10  | 10    | 10    | 10        | 0
        10  | -10   | 0     | 0         | 20
        10  | -10   | 0     | 16        | 36
        10  | -10   | 16    | 0         | 4
        10  | -10   | 10    | 10        | 20
        0.2 | 0.1   | 0     | 0         | 0.1
        0.2 | 0.1   | 0     | 16        | 16.1
        0.2 | 0.1   | 16    | 0         | -15.9
        0.2 | 0.1   | 16    | 16        | 0.1
        0.1 | 0.2   | 0     | 0         | -0.1
        0.1 | 0.2   | 0     | 16        | 15.9
        0.1 | 0.2   | 16    | 0         | -16.1
        0.1 | 0.2   | 16    | 16        | -0.1
    }

	def 'Get the balance color'() {
        given: 'a mocked UserService'
        def userService = Mock(UserService)
        userService.getNumFractionDigitsExt() >> 3
        i.userService = userService
        i.creditMemos*.userService = userService

        expect:
        'red' == i.balanceColor
	}

	def 'Get the color indicated by date'() {
		when:
        i.dueDatePayment = d

		then:
		color == i.colorIndicatorByDate()

        where:
        d                   | color
        new Date()          | 'orange'
        new Date() - 5      | 'red'
        new Date() + 3      | 'yellow'
        new Date() + 5      | 'default'
	}

    // TODO test getModifiedClosingBalance()

    def 'Get payment state color without delivered, paid and cancelled'() {
        when:
        i.stage = new InvoiceStage()
        i.stage.id = stage

        then:
        color == i.paymentStateColor

        where:
        stage               | color
        900                 | 'default'             // created
        901                 | 'default'             // revised
        904                 | 'purple'              // dunned
        905                 | 'blue'                // cashed
        906                 | 'black'               // booked out
    }

    def 'Get payment state color for stage delivered'() {
        given: 'stage "delivered"'
        i.stage = new InvoiceStage()
        i.stage.id = 902

        when:
        i.dueDatePayment = d

        then:
        color == i.paymentStateColor

        where:
        d                   | color
        new Date()          | 'orange'
        new Date() - 5      | 'red'
        new Date() + 3      | 'yellow'
        new Date() + 5      | 'default'
    }

    def 'Get payment state color for stage paid and positive closing balance'() {
        given: 'a mocked UserService'
        def userService = Mock(UserService)
        userService.getNumFractionDigitsExt() >> 3
        i.userService = userService
        i.creditMemos*.userService = userService

        and: 'stage "paid"'
        i.stage = new InvoiceStage()
        i.stage.id = 903

        and: 'a positive closing balance'
        i.creditMemos.clear()       // causes closing balance == 0

        when:
        i.dueDatePayment = d

        then:
        'green' == i.paymentStateColor

        where:
        d                   | _
        new Date()          | _
        new Date() - 5      | _
        new Date() + 3      | _
        new Date() + 5      | _
    }

    def 'Get payment state color for stage paid and negative closing balance'() {
        given: 'a mocked UserService'
        def userService = Mock(UserService)
        userService.getNumFractionDigitsExt() >> 3
        i.userService = userService
        i.creditMemos*.userService = userService

        and: 'stage "paid"'
        i.stage = new InvoiceStage()
        i.stage.id = 903
        // i already has negative closing balance

        when:
        i.dueDatePayment = d

        then:
        color == i.paymentStateColor

        where:
        d                   | color
        new Date()          | 'orange'
        new Date() - 5      | 'red'
        new Date() + 3      | 'yellow'
        new Date() + 5      | 'default'
    }

    def 'Get payment state color for stage cancelled and positive closing balance'() {
        given: 'a mocked UserService'
        def userService = Mock(UserService)
        userService.getNumFractionDigitsExt() >> 3
        i.userService = userService
        i.creditMemos*.userService = userService

        and: 'stage "paid"'
        i.stage = new InvoiceStage()
        i.stage.id = 907

        and: 'a positive closing balance'
        i.creditMemos.clear()       // causes closing balance == 0

        when:
        i.dueDatePayment = d

        then:
        'green' == i.paymentStateColor

        where:
        d                   | _
        new Date()          | _
        new Date() - 5      | _
        new Date() + 3      | _
        new Date() + 5      | _
    }

    def 'Get payment state color for stage cancelled and negative closing balance'() {
        given: 'a mocked UserService'
        def userService = Mock(UserService)
        userService.getNumFractionDigitsExt() >> 3
        i.userService = userService
        i.creditMemos*.userService = userService

        and: 'stage "paid"'
        i.stage = new InvoiceStage()
        i.stage.id = 907
        // i already has negative closing balance

        when:
        i.dueDatePayment = d

        then:
        color == i.paymentStateColor

        where:
        d                   | color
        new Date()          | 'orange'
        new Date() - 5      | 'red'
        new Date() + 3      | 'yellow'
        new Date() + 5      | 'default'
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