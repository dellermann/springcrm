/*
 * changelog.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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


databaseChangeLog = {
    include file: 'base.xml'
    include file: 'user-settings.xml'
    include file: 'sales-item.xml'
    include file: 'sales-item-pricing.xml'
    include file: 'number-issues.xml'
    include file: 'bugfixes-001.xml'
    include file: 'data-file.xml'
    include file: 'helpdesk.xml'
    include file: 'helpdesk-address.xml'
    include file: 'person-title.xml'
    include file: 'bugfixes-002.xml'
    include file: 'panel-pos.xml'
    include file: 'invoicing-items-sales-items-rel.xml'
	include file: 'invoicing-transaction-create-user.xml'
	include file: 'term-of-payment.xml'
	include file: 'big-decimal.xml'
    include file: 'modules-uppercase.xml'
    include file: 'rename-service.xml'
    include file: 'assessments.xml'
    include file: 'boilerplate.xml'
    include file: 'search.xml'
}
