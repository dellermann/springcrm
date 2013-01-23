import java.security.MessageDigest

/*
 * ConvertInstallationFixture.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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


includeTargets << grailsScript('_GrailsInit')
includeTargets << grailsScript('_GrailsArgParsing')


target(main: 'Generates test fixture files for installation test.') {
    depends(parseArguments)

    String lang = argsMap.params[0] ?: 'de-DE'
    String baseDir = grailsSettings.baseDir
    File input = new File("${baseDir}/web-app/WEB-INF/data/install/base-data-${lang}.sql")

    def tables = [] as Set
    StringBuilder buf = new StringBuilder('<?xml version="1.0" encoding="utf-8"?>\n\n<dataset>\n\n  <!-- Base data -->\n')
    input.eachLine {
        def m = (it =~ /(?i)^\s*INSERT(?:\s+INTO)\s+(\w+)\s+\(([^)]+)\)\s+VALUES\s+\(([^)]+)\)\s*$/)
        if (m) {
            tables << m[0][1]
            buf << '  <' << m[0][1]
            def fields = m[0][2].split(/,\s*/)
            def values = m[0][3].split(/,\s*/)
            for (int i = 0; i < fields.size(); i++) {
                String value = values[i]
                if (value != 'NULL') {
                    value = value.replace('&', '&amp;')
                    value = value.replace('<', '&lt;')
                    value = value.replace('"', '&quot;')
                    if (value.startsWith(/'/) && value.endsWith(/'/)) {
                        value = value.substring(1, value.length() - 1)
                        value = value.replaceAll(~'\\n', '&#13;')
                        value = value.replaceAll(~/\\(.)/, '$1')
                    }
                    buf << ' ' << fields[i] << '="' << value << '"'
                }
            }
            buf << '/>\n'
        }
    }
    buf << '\n  <!-- Client data -->\n'
    buf << '  <config version="0" name="configName" value="Agentur Kampe"/>\n'
    buf << '  <config version="0" name="configStreet" value="Hauptstraße 148"/>\n'
    buf << '  <config version="0" name="configPostalCode" value="23898"/>\n'
    buf << '  <config version="0" name="configLocation" value="Labenz"/>\n'
    buf << '  <config version="0" name="configPhone" value="04536 45301-0"/>\n'
    buf << '  <config version="0" name="configFax" value="04536 45301-90"/>\n'
    buf << '  <config version="0" name="configEmail" value="info@kampe.example"/>\n'
    buf << '  <config version="0" name="configWebsite" value="http://www.kampe.example"/>\n'
    buf << '  <config version="0" name="configBankName" value="Elbebank Hamburg"/>\n'
    buf << '  <config version="0" name="configBankCode" value="120340560"/>\n'
    buf << '  <config version="0" name="configAccountNumber" value="45671234"/>\n'
    buf << '  <config version="0" name="installStatus" value="1"/>\n'
    buf << '\n  <!-- Default administrator user -->\n'
    buf << '  <user_data version="0" date_created="2013-01-23 15:52:19" email="m.kampe@kampe.example" fax="04536 45301-90" first_name="Marcus" last_name="Kampe" last_updated="2013-01-23 15:52:19" mobile="0172 12034056" password="'
    buf << MessageDigest.getInstance('SHA-1').digest('abc1234'.bytes).encodeHex()
    buf << '" phone="04536 45301-10" phone_home="04536 65530" user_name="mkampe" admin="1"/>\n'
    buf << '\n  <!-- Empty all other table to maintain referential integrity -->\n'
    buf << '  <calendar_event/>\n'
    buf << '  <google_data_sync_status/>\n'
    buf << '  <invoicing_item/>\n'
    buf << '  <invoicing_transaction/>\n'
    buf << '  <invoicing_transaction_terms_and_conditions/>\n'
    buf << '  <ldap_sync_status/>\n'
    buf << '  <lru_entry/>\n'
    buf << '  <note/>\n'
    buf << '  <organization/>\n'
    buf << '  <panel/>\n'
    buf << '  <person/>\n'
    buf << '  <phone_call/>\n'
    buf << '  <project/>\n'
    buf << '  <project_document/>\n'
    buf << '  <project_item/>\n'
    buf << '  <purchase_invoice/>\n'
    buf << '  <purchase_invoice_item/>\n'
    buf << '  <reminder/>\n'
    buf << '  <sales_item/>\n'
    buf << '  <sales_item_pricing/>\n'
    buf << '  <sales_item_pricing_item/>\n'
    buf << '  <user_setting/>\n'
    buf << '</dataset>\n'
    new File("${baseDir}/web-app/test-data/install-data.xml").write(buf.toString(), 'utf-8')

    buf = new StringBuilder('<?xml version="1.0" encoding="utf-8"?>\n\n<dataset>\n')
    for (String table : tables) {
        buf << '  <' << table << '/>\n'
    }
    buf << '</dataset>\n'
    new File("${baseDir}/web-app/test-data/empty-install-data.xml").write(buf.toString(), 'utf-8')
}

setDefaultTarget(main)
