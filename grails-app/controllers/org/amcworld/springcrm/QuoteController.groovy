/*
 * QuoteController.groovy
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
 * The class {@code QuoteController} contains actions which manage quotes.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class QuoteController extends InvoicingController<Quote> {

    //-- Constructors ---------------------------

    QuoteController() {
        super(Quote)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        getCreateModel new Quote(params)
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def find() {
        super.find()
    }

    def index() {
        List<Quote> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Quote.findAllBySubjectLike(searchFilter, params)
            count = Quote.countBySubjectLike(searchFilter)
        } else {
            list = Quote.list(params)
            count = Quote.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person) {
        List<Quote> list = null
        int count = 0
        Map<String, Object> linkParams = null
        if (organization) {
            Organization organizationInstance = Organization.get(organization)
            list = Quote.findAllByOrganization(organizationInstance, params)
            count = Quote.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            Person personInstance = Person.get(person)
            list = Quote.findAllByPerson(personInstance, params)
            count = Quote.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }

        getListEmbeddedModel list, count, linkParams
    }

    def print(Long id, String template) {
        Quote quoteInstance = getDomainInstanceWithStatus(id)
        if (quoteInstance != null) {
            printDocument quoteInstance, template
        }
    }

    def save() {
        super.save()
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }
}
