/*
 * DocumentController.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import groovy.io.FileType
import net.sf.jmimemagic.Magic
import org.amcworld.springcrm.elfinder.Connector
import org.amcworld.springcrm.elfinder.fs.LocalFileSystemVolume
import org.amcworld.springcrm.elfinder.fs.VolumeConfig


class DocumentController {

    //-- Instance variables ---------------------

    def fileService


    //-- Public methods -------------------------

    def index() {}

    def command() {
        def conn = new Connector(request, response)
        def config = new VolumeConfig(
            alias: message(code: 'document.rootAlias', default: 'Documents')
        )
        conn.addVolume(new LocalFileSystemVolume(
            'l', grailsApplication.config.springcrm.dir.documents, config
        ))
        conn.process()
    }

    def listEmbedded() {
        def organizationInstance = Organization.get(params.organization)
        File dir = fileService.getOrgDocumentDir(organizationInstance)
        def list = []
        int total = 0
        if (dir) {
            String rootDir = dir.path
            List<Document> documents = []
            dir.eachFileRecurse(FileType.FILES) { documents << new Document(rootDir, it) }

            String sort = params.sort
            String order = params.order
            documents.sort { f1, f2 ->
                def res
                if ('size' == sort) {
                    res = f1.size <=> f2.size
                } else if ('lastModified' == sort) {
                    res = f1.lastModified <=> f2.lastModified
                } else {
                    res = f1.name <=> f2.name
                }

                if ('desc' == order) {
                    res *= -1
                }
                return res
            }
            total = documents.size()

            def offset = params.offset ?: 0
            def max = params.max ?: 10
            list = documents.subList(offset, Math.min(offset + max, documents.size()))
        }

        return [documentInstanceList: list, documentInstanceTotal: total, organizationInstance: organizationInstance]
    }

    def download() {
        def organizationInstance = Organization.get(params.organization)
        File dir = fileService.getOrgDocumentDir(organizationInstance)
        if (dir) {
            File f = Document.decode(dir.path, params.id)
            if (f.exists()) {
                response.contentType = Magic.getMagicMatch(f, true).mimeType
                response.contentLength = f.length()
                response.addHeader 'Content-Disposition',
                    "attachment; filename=\"${f.name}\""
                response.outputStream << f.newInputStream()
                return null
            }
        }
        render(status: 404)
    }

    def delete() {
        def organizationInstance = Organization.get(params.organization)
        File dir = fileService.getOrgDocumentDir(organizationInstance)
        if (dir) {
            File f = Document.decode(dir.path, params.id)
            if (f.exists()) {
                f.delete()
            }
        }

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'document.label', default: 'Document')])
        if (params.returnUrl) {
            redirect(url: params.returnUrl)
        } else {
            redirect(action: 'list')
        }
    }
}
