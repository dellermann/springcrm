/*
 * document.js
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


(function ($) {

    "use strict";

    var $documents = $("#documents");

    $documents.elfinder({
            commands: [
                'open', 'reload', 'home', 'up', 'back', 'forward', 'getfile',
                'quicklook', 'download', 'rm', 'duplicate', 'rename', 'mkdir',
                'mkfile', 'upload', 'copy', 'cut', 'paste', 'edit',
                /*'extract', 'archive', */'search', 'info', 'view', 'help',
                /*'resize', */'sort'
            ],
            contextmenu: {
                files: [
                    'getfile', '|','open', 'quicklook', '|', 'download', '|',
                    'copy', 'cut', 'paste', 'duplicate', '|',
                    'rm', '|', 'edit', 'rename', '|', 'info'
                ]
            },
            height: 500,
            lang: "de",
            uiOptions: {
                toolbar: [
                    ['back', 'forward'],
                    ['mkdir', 'mkfile', 'upload'],
                    ['open', 'download', 'getfile'],
                    ['info', 'quicklook'],
                    ['copy', 'cut', 'paste'],
                    ['rm'],
                    ['duplicate', 'rename', 'edit'],
                    ['search'],
                    ['view', 'sort'],
                    ['help']
                ],
            },
            url: $documents.data("load-url")
        })
        .elfinder("instance");
}(jQuery));
