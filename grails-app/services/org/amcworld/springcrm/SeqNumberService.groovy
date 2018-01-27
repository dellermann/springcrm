/*
 * SeqNumberService.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import grails.core.ArtefactHandler
import grails.core.GrailsApplication
import grails.core.GrailsClass
import grails.gorm.services.Service
import grails.util.GrailsNameUtils
import groovy.transform.CompileStatic
import org.bson.conversions.Bson
import org.grails.core.artefact.DomainClassArtefactHandler
import org.springframework.beans.factory.annotation.Autowired


/**
 * The interface {@code ISeqNumberService} contains general methods to handle
 * sequence numbers in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
interface ISeqNumberService {

    //-- Public methods -----------------------------

    /**
     * Finds a sequence number by the given controller name.
     *
     * @param controllerName    the given controller name
     * @return                  the found sequence number or {@code null} if no
     *                          such sequence number exists
     */
    SeqNumber findByControllerName(String controllerName)

    /**
     * Lists all sequence numbers.
     *
     * @param args  any arguments for loading sequence numbers
     * @return      the list of sequence numbers
     */
    List<SeqNumber> list(Map args)
}


/**
 * The class {@code SeqNumberService} cares about a sequential numbering of
 * content items.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@CompileStatic
@Service(SeqNumber)
abstract class SeqNumberService implements ISeqNumberService {

    //-- Constants ------------------------------

    /**
     * The controllers which should be checked for suitable sequence numbers.
     *
     * @since 2.1
     */
    private static final List<Class<? extends InvoicingTransaction>> \
        CONTROLLERS_TO_CHECK = [
            Quote, SalesOrder, Invoice, Dunning, CreditMemo
        ].asImmutable()


    //-- Fields ---------------------------------

    @Autowired
    GrailsApplication grailsApplication

    @Autowired(required = false)
    MongoClient mongo


    //-- Public methods -------------------------

    /**
     * Checks whether the start values of the sequence numbers of invoicing
     * transactions such as quotes, sales order, invoices etc. matches the
     * current year.
     *
     * @return  {@code true} if all sequence numbers are suitable;
     *          {@code false} otherwise
     * @since   2.1
     */
    boolean checkNumberScheme() {
        String year = new Date().format('YY')

        boolean res = true
        for (Class<? extends InvoicingTransaction> c : CONTROLLERS_TO_CHECK) {
            res &= checkControllerNumberScheme(c, year)
        }

        res
    }

    /**
     * Gets a list of sequence numbers where the numbers of invoicing
     * transactions such as quotes, sales order, invoices etc. have been fixed
     * to match the current year.
     *
     * @return  a list of sequence numbers with partially fixed start values
     * @since   2.1
     */
    List<SeqNumber> getFixedSeqNumbers() {
        String year = new Date().format('YY')
        int num = year.toInteger() * 1000

        List<SeqNumber> res = list(readOnly: true)
        for (SeqNumber sn : res) {
            Class<?> cls = domainNameToClass(sn.controllerName)
            if (InvoicingTransaction.isAssignableFrom(cls)) {
                Class<? extends InvoicingTransaction> c =
                    (Class<? extends InvoicingTransaction>) cls
                if (c in CONTROLLERS_TO_CHECK && sn.startValue < num) {
                    sn.startValue = num
                }
            }
        }

        res
    }

    /**
     * Checks whether or not to display the hint about changing the sequence
     * numbers for the given user.  The hint is displayed for administrators,
     * only.
     *
     * @param user  the given user
     * @return      {@code true} if the hint should be displayed; {@code false}
     *              otherwise
     * @since 2.1
     */
    boolean getShowHint(User user) {
        if (checkNumberScheme() || !user.administrator) {
            return false
        }

        String year = user.settings.seqNumberHintYear

        !year || new Date().format('YYYY').toInteger() > year.toInteger()
    }

    /**
     * Computes the full number of the given domain model instance.
     *
     * @param domain    the given domain model
     * @return          the computed number
     */
    String getFullNumber(NumberedDomain domain) {
        domain.computeFullNumber loadSeqNumber(domain.getClass())
    }

    /**
     * Loads the sequence number data for the given controller.
     *
     * @param controllerName    the given controller name
     * @return                  the sequence number data; {@code null} if no
     *                          such data are stored for the given controller
     */
    SeqNumber loadSeqNumber(String controllerName) {
        findByControllerName controllerName
    }

    /**
     * Loads the sequence number data for the controller which is associated to
     * the given class.
     *
     * @param controllerName    the given class
     * @return                  the sequence number data; {@code null} if no
     *                          such data are stored for the controller
     */
    SeqNumber loadSeqNumber(Class cls) {
        loadSeqNumber classToDomainName(cls)
    }

    /**
     * Retrieves the next available sequence number for the given controller
     * name.
     *
     * @param controllerName    the given controller name
     * @return                  the next available sequence number
     */
    int nextNumber(String controllerName) {
        SeqNumber seq = loadSeqNumber(controllerName)
        Class<?> clazz = domainNameToClass(controllerName)

        List<Bson> filters = [Filters.lte('number', seq.endValue)]
        if (clazz.hasProperty('nextNumberFilters')) {
            filters.addAll(
                (List<Bson>) clazz.getField('nextNumberFilters').get(null)
            )
        }

        Integer num = (Integer) getCollection(clazz)
            .aggregate([
                Aggregates.match(Filters.and(filters)),
                Aggregates.group(null, Accumulators.max('num', '$number'))
            ])
            .first()
            ?.getAt('num')

        // TODO what if num >= seq.endValue?

        (num == null || num < seq.startValue) ? seq.startValue : num + 1
    }

    /**
     * Retrieves the next available sequence number for the controller which is
     * associated to the given class.
     *
     * @param cls   the given class
     * @return      the next available sequence number
     */
    int nextNumber(Class cls) {
        nextNumber classToDomainName(cls)
    }

    /**
     * Stores the current year in the user settings to prevent display of the
     * hint about changing the sequence number scheme for this year.
     *
     * @param user  the currently logged in user
     */
    void setDontShowAgain(User user) {
        user.settings.seqNumberHintYear = new Date().format('YYYY')
    }

    /**
     * Stores in the user settings that the hint about changing the sequence
     * number scheme should no longer be displayed.
     *
     * @param user  the currently logged in user
     */
    void setNeverShowAgain(User user) {
        user.settings.seqNumberHintYear = '9999'
    }


    //-- Non-public methods ---------------------

    /**
     * Checks whether the start value of the sequence number of the given
     * controller matches the stated year.
     *
     * @param controller    the controller to check
     * @param year          the two digit year number (for example, {@code 17}
     *                      for 2017)
     * @return              {@code true} if the sequence number is suitable;
     *                      {@code false} otherwise
     * @since 2.1
     */
    private boolean checkControllerNumberScheme(
        Class<? extends InvoicingTransaction> controller, String year
    ) {
        SeqNumber seqNumber = loadSeqNumber(controller)

        seqNumber != null && (seqNumber.startValue as String).startsWith(year)
    }

    /**
     * Obtains the name of the domain model which is associated to the given
     * class.
     *
     * @param clazz the given class
     * @return      the associated domain model name; {@code null} if no domain
     *              model with the given class exists
     * @since 2.1
     */
    private String classToDomainName(Class clazz) {
        ArtefactHandler handler = grailsApplication.getArtefactType(clazz)
        GrailsClass gc = grailsApplication.getArtefact(handler.type, clazz.name)

        gc?.logicalPropertyName
    }

    /**
     * Obtains the class of the domain model with the given name.
     *
     * @param domainName    the given domain name such as {@code organization}
     * @return              the associated domain model class; {@code null} if
     *                      no domain model with the given name exists
     * @since 2.1
     */
    private Class<?> domainNameToClass(String domainName) {
        grailsApplication.getArtefactByLogicalPropertyName(
                DomainClassArtefactHandler.TYPE, domainName
            )?.clazz
    }

    /**
     * Gets the MongoDB collection of the given domain model class.
     *
     * @param clazz the given domain model class
     * @return      the MongoDB collection
     */
    private MongoCollection getCollection(Class<?> clazz) {
        mongo.getDatabase(
                grailsApplication.config.getProperty(
                    'grails.mongodb.databaseName'
                )
            )
            .getCollection(GrailsNameUtils.getLogicalName(clazz, ''))
    }
}
