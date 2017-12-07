/*
 * OrganizationController.groovy
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


/**
 * The class {@code OrganizationController} contains actions which manage
 * organizations.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class OrganizationController extends GenericDomainController<Organization> {

    //-- Fields -------------------------------------

    OrganizationService organizationService


    //-- Constructors ---------------------------

    OrganizationController() {
        super(Organization)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def find(Byte type) {
        List<Organization> list
        if (type) {
            List<Byte> types = [type, 3 as byte]
            list = organizationService.findAllByRecTypeInListAndNameLike(
                types, "%${params.name}%", [sort: 'name']
            )
        } else {
            list = organizationService.findAllByNameLike(
                "%${params.name}%", [sort: 'name']
            )
        }

        [(getDomainInstanceName('List')): list]
    }

    def get(Long id) {
        super.get id
    }

    def getPhoneNumbers(Long id) {
        Organization organizationInstance = getDomainInstanceWithStatus(id)
        if (organizationInstance == null) {
            return
        }

        List<String> phoneNumbers = [
                organizationInstance.phone,
                organizationInstance.phoneOther,
                organizationInstance.fax
            ]
            .findAll({ it != '' })
            .unique()

        [phoneNumbers: phoneNumbers]
    }

    def getTermOfPayment(Long id) {
        int termOfPayment =
            ConfigHolder.instance['termOfPayment'].toType(Integer) ?: 14

        Organization organizationInstance = organizationService.get(id)
        if (organizationInstance?.termOfPayment != null) {
            termOfPayment = organizationInstance.termOfPayment
        }

        [termOfPayment: termOfPayment]
    }

    def index(Byte listType) {
        List<Byte> types = [listType, 3 as byte]
        String letter = params.letter?.toString()
        if (letter) {
            int num
            if (listType) {
                num = organizationService.countByRecTypeInListAndNameLessThan(
                    types, letter
                )
            } else {
                num = organizationService.countByNameLessThan(letter)
            }
            params.sort = 'name'
            int max = params.int('max')
            params.offset = Math.floor(num / max) * max
        }

        List<Organization> list
        int count
        if (listType) {
            list = organizationService.findAllByRecTypeInList(types, params)
            count = organizationService.countByRecTypeInList(types)
        } else {
            list = organizationService.list(params)
            count = organizationService.count()
        }

        getIndexModel list, count
    }

    def show(Long id) {
        super.show id
    }

    def save() {
        request['redirectParams'] = [listType: params.listType]
        super.save()
    }

    def update(Long id) {
        request['redirectParams'] = [listType: params.listType]
        super.update id
    }


    //-- Non-public methods ---------------------

    @Override
    protected Map<String, Object> getIndexActionParams() {
        [listType: params.listType]
    }

    @Override
    protected Map<String, Object> getShowActionParams() {
        [listType: params.listType]
    }

    @Override
    protected void lowLevelDelete(Organization instance) {
        organizationService.delete instance.id
    }

    @Override
    protected Organization lowLevelGet(Long id) {
        organizationService.get id
    }

    @Override
    protected Organization lowLevelSave(Organization instance) {
        organizationService.save instance
    }
}
