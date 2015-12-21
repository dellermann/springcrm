/*
 * DocumentController.groovy
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

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import static javax.servlet.http.HttpServletResponse.SC_OK

import org.apache.commons.logging.LogFactory
import org.apache.commons.vfs2.FileContent
import org.apache.commons.vfs2.FileName
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.FileSystemException
import org.apache.commons.vfs2.FileType
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code DocumentController} handles actions which display
 * documents.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.2
 */
class DocumentController {

    //-- Constants ------------------------------

    private static final log = LogFactory.getLog(this)


    //-- Instance variables ---------------------

    DocumentService documentService


    //-- Public methods -------------------------

    def index() {
        [path: params.path ?: '']
    }

    def dir(String path) {
        try {
            FileObject dir = documentService.getFile(path)

            List<FileObject> folderList = []
            List<FileObject> fileList = []
            for (FileObject child in dir.children) {
                if (!child.hidden) {
                    if (child.type == FileType.FOLDER) {
                        folderList << child
                    } else {
                        fileList << child
                    }
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

            null
        } catch (FileSystemException e) {
            render status: SC_NOT_FOUND
        }
    }

    def listEmbedded(Long organization) {
        Organization organizationInstance = Organization.get(organization)
        if (!organizationInstance) {
            render status: SC_NOT_FOUND
            return
        }

        List<FileObject> documents =
            documentService.getFilesOfOrganization(organizationInstance)
        int total = documents.size()

        String sort = params.sort
        String order = params.order
        documents.sort { FileObject f1, FileObject f2 ->
            def res
            switch (sort) {
            case 'size':
                res = f1.content.size <=> f2.content.size
                break
            case 'lastModified':
                res = f1.content.lastModifiedTime <=> f2.content.lastModifiedTime
                break
            default:
                res = f1.name.baseName <=> f2.name.baseName
            }

            if ('desc' == order) res *= -1
            res
        }

        Integer offset = (params.offset ?: 0) as Integer
        Integer max = (params.max ?: 10) as Integer
        List<FileObject> list =
            documents.subList(offset, Math.min(offset + max, total))

        FileName rootName = documentService.root.name
        def documentInstanceList = []
        for (FileObject f in list) {
            FileName name = f.name
            FileContent content = f.content
            documentInstanceList << [
                path: rootName.getRelativeName(name),
                name: name.baseName,
                size: content.size,
                lastModified: new Date(content.lastModifiedTime)
            ]
        }

        [
            documentInstanceList: documentInstanceList,
            documentInstanceTotal: total
        ]
    }

    def download(String path) {
        try {
            FileObject file = documentService.getFile(path)
            FileContent content = file.content

//            response.contentType = Magic.getMagicMatch(data, true).mimeType
            response.contentLength = content.size
            response.addHeader 'Content-Disposition',
                "attachment; filename=\"${file.name.baseName}\""
            response.outputStream << content.inputStream

            null
        } catch (FileSystemException e) {
            render status: SC_NOT_FOUND
        }
    }

    def upload(String path) {
        List<String> errorneousFiles = []
        for (MultipartFile file in request.getFiles('file')) {
            try {
                documentService.uploadFile(
                    path, file.originalFilename, file.inputStream
                )
            } catch (FileSystemException e) {
                errorneousFiles << file.originalFilename
            }
        }

        if (errorneousFiles.empty) {
            flash.message = message(code: 'document.upload.success')
        } else {
            flash.message = message(
                code: 'document.upload.error',
                args: [errorneousFiles.size(), errorneousFiles.join(', ')]
            )
        }

        redirect action: 'index', params: [path: path]
    }

    def createFolder(String path, String name) {
        try {
            documentService.createFolder path, name
            render status: SC_OK
        } catch (FileSystemException e) {
            render status: SC_NOT_FOUND
        }
    }

    def delete(String path) {
        int sc = SC_OK
        try {
            documentService.deleteFileObject path
        } catch (FileSystemException e) {
            sc = SC_NOT_FOUND
        }

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            render status: sc
        }
    }
}

