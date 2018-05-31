/*
 * ConfigController.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.converters.JSON
import grails.core.GrailsClass
import grails.plugin.springsecurity.annotation.Secured
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject


/**
 * The class {@code ConfigController} handles actions to configure system
 * settings such as client data, currency, selection values etc.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured(['ROLE_ADMIN'])
@Slf4j
class ConfigController {

    //-- Constants ------------------------------

    /**
     * A list of IDs of selector values which are considered read-only.
     */
    public static final List<Long> READONLY_IDS = [
        *600L..604L, *800L..804L, *900L..907L, *2100L..2103L, *2200L..2206L,
        *2500L..2504L, *2600L..2605L
    ].asImmutable() as List<Long>


    //-- Fields ---------------------------------

    ConfigService configService
    SelValueService selValueService
    SeqNumberService seqNumberService
    UserService userService
    WorkService workService


    //-- Public methods -------------------------

    def currency() {
        Locale locale = userService.getCurrentLocale()
        Map<String, String> currencies =
            userService.getAvailableCurrencies().collectEntries {
                String cc = it.currencyCode
                String sym = it.getSymbol(locale)

                [cc, cc + ((cc == sym) ? '' : " (${sym})")]
            }
        String currentCurrency = configService.getString('currency')
        Integer termOfPayment = configService.getInteger('termOfPayment')

        respond(
            currencies: currencies.sort { a, b -> a.key <=> b.key },
            currentCurrency: currentCurrency,
            numFractionDigits: userService.getNumFractionDigits(),
            numFractionDigitsExt: userService.getNumFractionDigitsExt(),
            termOfPayment: termOfPayment
        )
    }

    def fixSeqNumbers() {
        List<SeqNumber> list = seqNumberService.getFixedSeqNumbers()
        flash.message = message(code: 'config.seqNumbers.fixed') as Object

        respond list, [model: prepareLoadSeqNumberModel(), view: 'seqNumbers']
    }

    def index() {}

    def loadSelValues() {
        List<? extends SelValue> list = selValueService.findAllByClass(
            getTypeClass(params.type.toString())
        )

        respond selValueList: list
    }

    def loadSeqNumbers() {
        respond(
            seqNumberService.list(),
            [model: prepareLoadSeqNumberModel(), view: 'seqNumbers']
        )
    }

    def loadTaxRates() {
        respond selValueService.findAllByClass(TaxRate)
    }

    def loadTenant() {
        respond configService.loadTenant()
    }

    def save() {
        for (Map.Entry<String, Object> entry : params.config?.entrySet()) {
            String key = entry.key
            Object value = entry.value
            if (key.startsWith('_')) {
                configService.store key.substring(1), false
            } else if (key != 'ldapBindPasswd' && key != 'mailPassword'
                || value)    // issue #35
            {
                if (key == 'ldapPort') {
                    value = value ?: 389    // issue #35
                } else if (key == 'mailUseConfig' && value == 'null') {
                    configService.delete key
                    continue
                }
                configService.store key, value
            }
        }

        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'config.label'), '']
        ) as Object

        redirect action: 'index'
    }

    def saveSelValues() {
        for (Map.Entry<String, Object> entry : params.selValues?.entrySet()) {
            if (!entry.value) {
                continue
            }

            int orderId = 10i
            Class<?> cls = getTypeClass(entry.key)
            JSONArray list = (JSONArray) JSON.parse(entry.value.toString())
            for (Object item : list) {
                JSONObject obj = (JSONObject) item
                long id = obj.getLong('id')
                if (obj.has('remove') && obj.getBoolean('remove')) {
                    if (id in READONLY_IDS) {
                        log.debug 'Prevent deleting read-only selValue {}', id
                    } else {
                        log.debug 'Delete selValue {}', id
                        selValueService.delete id
                    }
                } else {
                    String name = obj.getString('name')
                    if (id < 0L) {
                        SelValue selValue = cls.newInstance()
                        selValue.name = name
                        selValue.orderId = orderId
                        selValueService.save selValue
                        log.debug(
                            'Inserted selValue: name={}, orderId={}, id={}',
                            name, orderId, selValue.id
                        )
                    } else {
                        SelValue selValue =
                            selValueService.findByClassAndId(cls, id)
                        boolean modified = false
                        if (selValue.orderId != orderId) {
                            selValue.orderId = orderId
                            modified = true
                        }
                        if (id in READONLY_IDS) {
                            if (modified) {
                                selValueService.save selValue
                                log.debug(
                                    'Updated read-only selValue {}: orderId={}',
                                    id, orderId
                                )
                            } else {
                                log.trace(
                                    'Read-only selValue {} not modified.', id
                                )
                            }
                        } else {
                            if (selValue.name != name) {
                                selValue.name = name
                                modified = true
                            }
                            if (modified) {
                                selValueService.save selValue
                                log.debug(
                                    'Updated selValue {}: name={}, orderId={}',
                                    id, name, orderId
                                )
                            } else {
                                log.trace 'SelValue {} not modified.', id
                            }
                        }
                    }
                    orderId += 10i
                }
            }
        }

        redirect action: 'index'
    }

    def saveSeqNumbers() {
        List<SeqNumber> seqNumbers = []
        boolean hasErrors = false
        for (Map.Entry<String, Object> entry : params.seqNumbers) {
            try {
                SeqNumber seqNumber = seqNumberService.get(entry.key)

                /*
                 * Implementation notes: Using
                 * seqNumber.properties['prefix', 'suffix', 'startValue', 'endValue'] = entry.value
                 * doesn't work because it sets an empty suffix to a null value
                 * which infringes the suffix nullable: false constraint.  The
                 * same is valid for prefix.
                 */
                Map props = (Map) entry.value
                seqNumber.prefix = props.prefix
                seqNumber.suffix = props.suffix
                seqNumber.startValue = props.startValue as int
                seqNumber.endValue = props.endValue as int

                seqNumbers << seqNumber
                hasErrors |= !seqNumberService.save(seqNumber)
            } catch (NumberFormatException ignored) { /* ignored */ }
        }

        saveOrDeleteConfig 'workIdDunningCharge', params.workIdDunningCharge
        saveOrDeleteConfig 'workIdDefaultInterest', params.workIdDefaultInterest

        if (hasErrors) {
            seqNumbers.sort { it.orderId }
            respond seqNumbers, view: 'seqNumbers'
            return
        }

        redirect action: 'index'
    }

    def saveTaxRates() {
        String taxRates = params.taxRates
        if (taxRates) {
            int orderId = 10i
            JSONArray list = (JSONArray) JSON.parse(taxRates)
            for (Object item : list) {
                JSONObject obj = (JSONObject) item
                long id = obj.getLong('id')
                if (obj.isNull('name')) {
                    selValueService.delete id
                } else {
                    TaxRate taxRate = id < 0 ? new TaxRate()
                        : selValueService.findByClassAndId(TaxRate, id)
                    String name = obj.getString('name')
                    taxRate.name = "${name} %"
                    taxRate.orderId = orderId
                    taxRate.taxValue = (name as BigDecimal) / 100.0
                    selValueService.save taxRate
                    orderId += 10i
                }
            }
        }

        redirect action: 'index'
    }

    def saveTenant(Tenant tenant) {
        if (!tenant.validate()) {
            respond tenant, [view: 'loadTenant']
            return
        }

        configService.storeTenant tenant
        redirect action: 'index'
    }

    def show() {
        Map<String, String> config = configService.list().collectEntries {
            Config config -> [config.id, config.value]
        }

        render model: [configData: config], view: params.page
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the class for the given type of selector values.
     *
     * @param type  the name of the type; the targeted domain model class must
     *              be a subclass of {@code SelValue}
     * @return      the class of the selector value
     * @see         SelValue
     */
    @CompileStatic
    private Class<SelValue> getTypeClass(String type) {
        GrailsClass gc =
            grailsApplication.getArtefactByLogicalPropertyName('Domain', type)
        if (gc == null) {
            throw new IllegalArgumentException(
                "Type ${type} is no valid type for a domain class."
            )
        }
        Class<?> cls = gc.clazz
        if (!SelValue.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(
                "Type ${type} must be of type SelValue."
            )
        }

        (Class<SelValue>) cls
    }

    /**
     * Prepares a model containing the services instances for reminder charge
     * and reminder default interest.
     *
     * @return  the model
     */
    @CompileStatic
    private Map<String, ?> prepareLoadSeqNumberModel() {
        ObjectId id = configService.getObjectId('workIdDunningCharge')
        Work workDunningCharge = workService.get(id)
        id = configService.getObjectId('workIdDefaultInterest')
        Work workDefaultInterest = workService.get(id)

        [
            workDunningCharge: workDunningCharge,
            workDefaultInterest: workDefaultInterest
        ]
    }

    /**
     * Saves or deletes a configuration with the given name depending on the
     * given value.  If the value is {@code null} or an empty string the
     * configuration is deleted, otherwise it is stored.
     *
     * @param name  the name of the configuration
     * @param value the value of the configuration
     * @since 3.0
     */
    @CompileStatic
    private void saveOrDeleteConfig(String name, Object value) {
        if (value) {
            configService.store name, value
        } else {
            configService.delete name
        }
    }
}
