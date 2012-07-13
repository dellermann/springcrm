/*
 * ConnectorError.groovy
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


package org.amcworld.springcrm.elfinder


/**
 * The class {@code ConnectorError} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
enum ConnectorError {

    //-- Values ---------------------------------

    UNKNOWN('errUnknown'),
    UNKNOWN_CMD('errUnknownCmd'),
    CONF('errConf'),
    CONF_NO_JSON('errJSON'),
    CONF_NO_VOL('errNoVolumes'),
    INV_PARAMS('errCmdParams'),
    OPEN('errOpen'),
    DIR_NOT_FOUND('errFolderNotFound'),
    FILE_NOT_FOUND('errFileNotFound'),          // File not found.
    TRGDIR_NOT_FOUND('errTrgFolderNotFound'),   // Target folder "$1" not found.
    NOT_DIR('errNotFolder'),
    NOT_FILE('errNotFile'),
    PERM_DENIED('errPerm'),
    LOCKED('errLocked'),                        // "$1" is locked and can not be renamed, moved or removed.
    EXISTS('errExists'),                        // File named "$1" already exists.
    INVALID_NAME('errInvName'),                 // Invalid file name.
    MKDIR('errMkdir'),
    MKFILE('errMkfile'),
    RENAME('errRename'),
    COPY('errCopy'),
    MOVE('errMove'),
    COPY_FROM('errCopyFrom'),
    COPY_TO('errCopyTo'),
    COPY_ITSELF('errCopyInItself'),
    REPLACE('errReplace'),                      // Unable to replace "$1".
    RM('errRm'),                                // Unable to remove "$1".
    RM_SRC('errRmSrc'),                         // Unable remove source file(s)
    UPLOAD('errUpload'),                        // Upload error.
    UPLOAD_FILE('errUploadFile'),               // Unable to upload "$1".
    UPLOAD_NO_FILES('errUploadNoFiles'),        // No files found for upload.
    UPLOAD_TOTAL_SIZE('errUploadTotalSize'),    // Data exceeds the maximum allowed size.
    UPLOAD_FILE_SIZE('errUploadFileSize'),      // File exceeds maximum allowed size.
    UPLOAD_FILE_MIME('errUploadMime'),          // File type not allowed.
    UPLOAD_TRANSFER('errUploadTransfer'),       // "$1" transfer error.
    ACCESS_DENIED('errAccess'),
    NOT_REPLACE('errNotReplace'),               // Object "$1" already exists at this location and can not be replaced with object of another type.
    SAVE('errSave'),
    EXTRACT('errExtract'),
    ARCHIVE('errArchive'),
    NOT_ARCHIVE('errNoArchive'),
    ARCHIVE_TYPE('errArcType'),
    ARC_SYMLINKS('errArcSymlinks'),
    ARC_MAXSIZE('errArcMaxSize'),
    RESIZE('errResize'),
    UNSUPPORT_TYPE('errUsupportType'),
    NOT_UTF8_CONTENT('errNotUTF8Content')


    //-- Instance variables ---------------------

    String code


    //-- Constructors ---------------------------

    ConnectorError(String code) {
        this.code = code
    }
}
