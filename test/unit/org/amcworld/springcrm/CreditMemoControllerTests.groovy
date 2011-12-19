package org.amcworld.springcrm

import grails.test.*
import grails.test.mixin.*

@TestFor(CreditMemoController)
@Mock([CreditMemo, Organization, Person, Invoice, Dunning, InvoicingItem])
class CreditMemoControllerTests {

//    void setUp() {
//        int seqNumber = 1
//        def seqNumberService = mockFor(SeqNumberService)
//        seqNumberService.demand.nextNumber(0..10) { cls -> seqNumber++ }
//
//        def org = new Organization(name: 'AMC World', phone: '+49 30 83214750')
//        org.seqNumberService = seqNumberService.createMock()
//        org.save(validate: false)
//
//        def p = new Person(
//            firstName: 'Daniel', lastName: 'Ellermann', phone: '+49 30 1234567'
//        )
//        p.seqNumberService = seqNumberService.createMock()
//        p.save(validate: false)
//
//        def i = new Invoice(subject: 'Invoice 1')
//        i.seqNumberService = seqNumberService.createMock()
//        i.save(validate: false)
//
//        def d = new Dunning(subject: 'Dunning 1')
//        d.seqNumberService = seqNumberService.createMock()
//        d.save(validate: false)
//
//        def ii1 = new InvoicingItem(
//            number: 'S-10000', quantity: 5, name: 'Item 1', unitPrice: 4.5,
//            tax: 19
//        )
////        ii1.save(validate: false, flush: true)
//        def ii2 = new InvoicingItem(
//            number: 'S-20000', quantity: 10, name: 'Item 2', unitPrice: 9.4,
//            tax: 19
//        )
////        ii2.save(validate: false, flush: true)
//
//        def c = new CreditMemo(
//                subject: 'Credit memo 1', organization: org, invoice: i,
//                shippingCosts: 4.9
//            )
//            .addToItems(ii1)
//            .addToItems(ii2)
//        c.seqNumberService = seqNumberService.createMock()
//        c.save(validate: false, flush: true)
//        c = new CreditMemo(
//                subject: 'Credit memo 2', organization: org, person: p,
//                dunning: d, shippingCosts: 4.9
//            )
//            .addToItems(ii1)
//            .addToItems(ii2)
//        c.seqNumberService = seqNumberService.createMock()
//        c.save(validate: false, flush: true)
//
//        CreditMemo.metaClass.index = { -> }
//        CreditMemo.metaClass.reindex = { -> }
//    }

    void testIndex() {
        controller.index()
        assert '/creditMemo/list' == response.redirectedUrl
    }

//    void testList() {
//        def model = controller.list()
//        assert 2 == model.creditMemoInstanceTotal
//        assert 2 == model.creditMemoInstanceList.size()
//        assert 'Credit memo 1' == model.creditMemoInstanceList[0].subject
//        assert 'Credit memo 2' == model.creditMemoInstanceList[1].subject
//    }
}
