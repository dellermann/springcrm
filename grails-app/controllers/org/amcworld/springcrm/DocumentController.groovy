/*
 * DocumentController.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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
import javax.servlet.http.HttpServletResponse
import org.apache.commons.logging.LogFactory
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.FileSystemException
import org.apache.commons.vfs2.FileType
import org.apache.commons.vfs2.NameScope


/**
 * The class {@code DocumentController} handles actions which display
 * documents.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.2
 */
class DocumentController {

    //-- Constants ------------------------------

    private static final log = LogFactory.getLog(this)


    //-- Instance variables ---------------------

    DocumentService documentService
//    FileService fileService


    //-- Public methods -------------------------

    def index() {}

    def list(String path) {
        FileObject root = documentService.root
        if (!root.exists()) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        try {
            FileObject dir =
                root.resolveFile(path, NameScope.DESCENDENT_OR_SELF)

            def folderList = []
            def fileList = []
            for (FileObject child in dir.children) {
                if (child.type == FileType.FOLDER) {
                    folderList << child
                } else {
                    fileList << child
                }
            }
            folderList = folderList.sort { it.name.baseName }
            fileList = fileList.sort { it.name.baseName }

            render(contentType: 'application/json') {
                folders = array {
                    for (FileObject folder in folderList) {
                        fileObject name: folder.name.baseName,
                            readable: folder.readable,
                            writeable: folder.writeable
                    }
                }
                files = array {
                    for (FileObject file in fileList) {
                        fileObject name: file.name.baseName,
                            ext: file.name.extension,
                            size: file.content.size,
                            readable: file.readable,
                            writeable: file.writeable
                    }
                }
            }
        } catch (FileSystemException e) {
            render status: HttpServletResponse.SC_NOT_FOUND
        }
    }

//    def listEmbedded(Long organization) {
//        def organizationInstance = Organization.get(organization)
//        File dir = fileService.getOrgDocumentDir(organizationInstance)
//        Volume volume = fileService.localVolume
//        def list = []
//        int total = 0
//        if (dir) {
//            List<Document> documents = []
//            dir.eachFileRecurse(FileType.FILES) {
//                if (!it.hidden) {
//                    documents << new Document(volume.encode(it.path), it)
//                }
//            }
//
//            String sort = params.sort
//            String order = params.order
//            documents.sort { f1, f2 ->
//                def res
//                if ('size' == sort) {
//                    res = f1.size <=> f2.size
//                } else if ('lastModified' == sort) {
//                    res = f1.lastModified <=> f2.lastModified
//                } else {
//                    res = f1.name <=> f2.name
//                }
//
//                if ('desc' == order) {
//                    res *= -1
//                }
//                return res
//            }
//            total = documents.size()
//
//            def offset = (params.offset ?: 0) as Integer
//            def max = (params.max ?: 10) as Integer
//            list = documents.subList(offset, Math.min(offset + max, total))
//        }
//
//        [documentInstanceList: list, documentInstanceTotal: total]
//    }
//
//    def command() {
//        new Connector(request, response).
//            addVolume(fileService.localVolume).
//            process()
//    }
//
//    def download(String id) {
//        Volume volume = fileService.localVolume
//        try {
//            Map<String, Object> file = volume.file(id)
//            if (file) {
//                InputStream stream = volume.open(id)
//                if (stream) {
//                    response.contentType = file.mime
//                    response.contentLength = file.size
//                    response.addHeader 'Content-Disposition',
//                        "attachment; filename=\"${file.name}\""
//                    response.outputStream << stream
//                    return null
//                }
//            }
//        } catch (ConnectorException e) {
//            log.error e
//        }
//        render status: HttpServletResponse.SC_NOT_FOUND
//    }
//
//    def delete(String id) {
//        try {
//            if (fileService.localVolume.rm(id)) {
//                flash.message = message(code: 'default.deleted.message', args: [message(code: 'document.label', default: 'Document')])
//            }
//        } catch (ConnectorException e) {
//            log.error e
//        }
//        if (params.returnUrl) {
//            redirect url: params.returnUrl
//        } else {
//            redirect action: 'list'
//        }
//    }
}
