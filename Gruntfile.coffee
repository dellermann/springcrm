#
# Gruntfile.coffee
#
# Copyright (c) 2011-2016, Daniel Ellermann
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#


module.exports = (grunt) ->
  require('load-grunt-tasks') grunt

  grunt.initConfig
    clean:
      documentation: ['<%= dirs.target.documentation %>']
      publish: [
        '<%= dirs.src.fonts %>/'
        '<%= dirs.src.javascripts %>/bootstrap/'
        '<%= dirs.src.javascripts %>/jquery/'
        '<%= dirs.src.javascripts %>/jqueryui/'
        '<%= dirs.src.javascripts %>/lang/bootstrap-datepicker/'
        '<%= dirs.src.javascripts %>/selectize/selectize.js'
        '<%= dirs.src.javascripts %>/templates/tools/js-calc.hbs'
        '<%= dirs.src.javascripts %>/_js-calc.coffee'
        '<%= dirs.src.javascripts %>/_typeahead.js'
        '<%= dirs.src.stylesheets %>/bootstrap/'
        '<%= dirs.src.stylesheets %>/font-awesome/'
        '<%= dirs.src.stylesheets %>/js-calc/'
        '<%= dirs.src.stylesheets %>/selectize/plugins/'
        '<%= dirs.src.stylesheets %>/selectize/selectize.bootstrap3.less'
        '<%= dirs.src.stylesheets %>/selectize/selectize.less'
      ]
      test: ['<%= dirs.target.test.base %>']
    codo:
      documentation:
        dest: '<%= dirs.target.documentation %>'
        options:
          name: 'SpringCRM'
          private: true
          title: 'SpringCRM CoffeeScript documentation'
        src: ['<%= dirs.src.javascripts %>']
    coffee:
      test:
        files: [
            cwd: '<%= dirs.src.coffee %>/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            ext: '.js'
            src: ['*.coffee']
          ,
            cwd: '<%= dirs.src.test.coffee %>/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            ext: '.js'
            src: ['*.coffee']
        ]
    concat:
      publish:
        files: [
          dest: '<%= dirs.src.javascripts %>/_js-calc.coffee'
          src: [
            '<%= dirs.bower.jsCalc %>/coffee/js-calc.coffee'
            '<%= dirs.bower.jsCalc %>/coffee/stack.coffee'
            '<%= dirs.bower.jsCalc %>/coffee/input.coffee'
          ]
        ]
        options:
          process: (src, filepath) ->
            src.replace /Handlebars\.templates\['js-calc'\]/g,
              'Handlebars.templates[\'tools/js-calc\']'
    copy:
      publish:
        files: [
            cwd: '<%= dirs.bower.bootstrap %>/less/'
            dest: '<%= dirs.src.stylesheets %>/bootstrap/'
            expand: true
            src: ['**/*.less']
          ,
            cwd: '<%= dirs.bower.bootstrap %>/js/'
            dest: '<%= dirs.src.javascripts %>/bootstrap/'
            expand: true
            src: [
              'alert.js'
              'collapse.js'
              'dropdown.js'
              'modal.js'
              'transition.js'
            ]
          ,
            dest: '<%= dirs.src.stylesheets %>/bootstrap/datepicker.less'
            src: '<%= dirs.bower.bootstrapDatepicker %>/less/datepicker3.less'
          ,
            dest: '<%= dirs.src.javascripts %>/bootstrap/datepicker.js'
            src:
              '<%= dirs.bower.bootstrapDatepicker %>/js/bootstrap-datepicker.js'
          ,
            cwd: '<%= dirs.bower.bootstrapDatepicker %>/js/locales/'
            dest: '<%= dirs.src.javascripts %>/lang/bootstrap-datepicker/'
            expand: true
            src: ['*.js']
          ,
            dest: '<%= dirs.src.stylesheets %>/bootstrap/fileinput.css'
            src: '<%= dirs.bower.bootstrapFileinput %>/css/fileinput.css'
          ,
            dest: '<%= dirs.src.javascripts %>/bootstrap/fileinput.js'
            src: '<%= dirs.bower.bootstrapFileinput %>/js/fileinput.js'
          ,
            cwd: '<%= dirs.bower.fontAwesome %>/less/'
            dest: '<%= dirs.src.stylesheets %>/font-awesome/'
            expand: true
            src: [
              'animated.less'
              'core.less'
              'fixed-width.less'
              'icons.less'
              'larger.less'
              'mixins.less'
              'path.less'
              'variables.less'
            ]
          ,
            cwd: '<%= dirs.bower.fontAwesome %>/fonts/'
            dest: '<%= dirs.src.fonts %>/'
            expand: true
            src: ['*']
          ,
            dest: '<%= dirs.src.javascripts %>/jquery/jquery.js'
            src: '<%= dirs.bower.jquery %>/dist/jquery.js'
          ,
            dest: '<%= dirs.src.javascripts %>/jquery/autosize.js'
            src: '<%= dirs.bower.jqueryAutosize %>/dist/autosize.js'
          ,
            dest: '<%= dirs.src.javascripts %>/jquery/json.js'
            src: '<%= dirs.bower.jqueryJson %>/src/jquery.json.js'
          ,
            dest: '<%= dirs.src.javascripts %>/jquery/storage-api.js'
            src: '<%= dirs.bower.jqueryStorageAPI %>/jquery.storageapi.js'
          ,
            cwd: '<%= dirs.bower.jqueryui %>/ui/'
            dest: '<%= dirs.src.javascripts %>/jqueryui/'
            expand: true
            src: [
              'core.js'
              'draggable.js'
              'mouse.js'
              'sortable.js'
              'widget.js'
            ]
          ,
            dest: '<%= dirs.src.javascripts %>/jqueryui/touch-punch.js'
            src: '<%= dirs.bower.jqueryuiTouchPunch %>/jquery.ui.touch-punch.js'
          ,
            cwd: '<%= dirs.bower.jsCalc %>/less/'
            dest: '<%= dirs.src.stylesheets %>/js-calc/'
            expand: true
            src: [
              'core.less'
              'variables.less'
            ]
          ,
            dest: '<%= dirs.src.javascripts %>/templates/tools/js-calc.hbs'
            src: '<%= dirs.bower.jsCalc %>/templates/js-calc.hbs'
          ,
            cwd: '<%= dirs.bower.selectize %>/dist/less/'
            dest: '<%= dirs.src.stylesheets %>/selectize/'
            expand: true
            src: [
              'plugins/**'
              'selectize.bootstrap3.less'
              'selectize.less'
            ]
          ,
            dest: '<%= dirs.src.javascripts %>/selectize/selectize.js'
            src: '<%= dirs.bower.selectize %>/dist/js/standalone/selectize.js'
          ,
            dest: '<%= dirs.src.javascripts %>/_typeahead.js'
            src: '<%= dirs.bower.typeahead %>/dist/typeahead.bundle.js'
          ,
            dest: '<%= dirs.src.stylesheets %>/_typeahead.less'
            src: '<%= dirs.bower.typeaheadBootstrap %>/typeahead.less'
          ,
            cwd: '<%= dirs.bower.vatCalc %>/less/'
            dest: '<%= dirs.src.stylesheets %>/vat-calc/'
            expand: true
            src: [
              'core.less'
              'variables.less'
            ]
          ,
            dest: '<%= dirs.src.javascripts %>/_vat-calc.coffee'
            src: '<%= dirs.bower.vatCalc %>/coffee/vat-calc.coffee'
          ,
            dest: '<%= dirs.src.javascripts %>/templates/tools/vat-calc.hbs'
            src: '<%= dirs.bower.vatCalc %>/templates/vat-calc.hbs'
        ]
        options:
          encoding: null
          noProcess: '**/*.{eot|gif|jpg|js|otf|png|ttf|woff}'
          process: (contents, srcPath) ->
            g = grunt
            conf = g.config
            file = g.file

            jc = conf.get 'dirs.bower.jsCalc'
            tb = conf.get 'dirs.bower.typeaheadBootstrap'
            fa = conf.get 'dirs.bower.fontAwesome'
            vc = conf.get 'dirs.bower.vatCalc'

            # if file.doesPathContain "#{fa}/less", srcPath
            #   contents = String(contents)
            #   contents = contents.replace /@\{fa-css-prefix\}/g, 'fa'
            if file.arePathsEquivalent srcPath, "#{jc}/less/core.less"
              contents = String(contents)
              contents = contents.replace /@\{prefix\}/g, 'jscalc'
            if file.arePathsEquivalent srcPath, "#{tb}/typeahead.less"
              contents = String(contents)
              contents = contents.replace /\.tt-dropdown-menu/, '.tt-menu'
            if file.arePathsEquivalent srcPath, "#{vc}/less/core.less"
              contents = String(contents)
              contents = contents.replace /@\{prefix\}/g, 'vatcalc'
            if file.arePathsEquivalent srcPath, "#{vc}/coffee/vat-calc.coffee"
              contents = String(contents)
              contents = contents.replace /Handlebars.templates\['vat-calc'\]/g, '''Handlebars.templates['tools/vat-calc']'''

            contents
      test:
        files: [
            cwd: '<%= dirs.src.test.base %>/'
            dest: '<%= dirs.target.test.js.base %>/'
            expand: true
            src: ['*.html']
          ,
            cwd: '<%= dirs.bower.qunit %>/qunit/'
            dest: '<%= dirs.target.test.js.css %>/'
            expand: true
            src: 'qunit.css'
          ,
            cwd: '<%= dirs.bower.qunit %>/qunit/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: 'qunit.js'
          ,
            cwd: '<%= dirs.src.javascripts %>/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: [
              '_jquery.js'
              '_jquery-ui.js'
              '_jquery-autosize.js'
            ]
          ,
            cwd: '<%= dirs.bower.jqueryMockjax %>/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: 'jquery.mockjax.js'
          ,
            cwd: '<%= dirs.bower.handlebars %>/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: 'handlebars.js'
          ,
            cwd: '<%= dirs.src.assets %>/'
            dest: '<%= dirs.target.test.js.base %>/'
            expand: true
            src: 'fonts/**'
        ]
    dirs:
      bower:
        base: '<%= dirs.src.base %>/bower_components'
        bootstrap: '<%= dirs.bower.base %>/bootstrap'
        bootstrapDatepicker: '<%= dirs.bower.base %>/bootstrap-datepicker'
        bootstrapFileinput: '<%= dirs.bower.base %>/bootstrap-fileinput'
        fontAwesome: '<%= dirs.bower.base %>/font-awesome'
        handlebars: '<%= dirs.bower.base %>/handlebars'
        jquery: '<%= dirs.bower.base %>/jquery'
        jqueryAutosize: '<%= dirs.bower.base %>/jquery-autosize'
        jqueryJson: '<%= dirs.bower.base %>/jquery-json'
        jqueryMockjax: '<%= dirs.bower.base %>/jquery-mockjax'
        jqueryStorageAPI: '<%= dirs.bower.base %>/jQuery-Storage-API'
        jqueryui: '<%= dirs.bower.base %>/jqueryui'
        jqueryuiTouchPunch: '<%= dirs.bower.base %>/jqueryui-touch-punch'
        jsCalc: '<%= dirs.bower.base %>/js-calc'
        qunit: '<%= dirs.bower.base %>/qunit'
        selectize: '<%= dirs.bower.base %>/selectize'
        typeahead: '<%= dirs.bower.base %>/typeahead.js'
        typeaheadBootstrap:
          '<%= dirs.bower.base %>/typeahead.js-bootstrap3.less'
        vatCalc: '<%= dirs.bower.base %>/vat-calc'
      src:
        assets: '<%= dirs.src.grailsApp %>/assets'
        base: '.'
        coffee: '<%= dirs.src.assets %>/javascripts'
        fonts: '<%= dirs.src.assets %>/fonts'
        grailsApp: '<%= dirs.src.base %>/grails-app'
        images: '<%= dirs.src.assets %>/images'
        javascripts: '<%= dirs.src.assets %>/javascripts'
        stylesheets: '<%= dirs.src.assets %>/stylesheets'
        test:
          base: '<%= dirs.src.base %>/test/js'
          coffee: '<%= dirs.src.test.base %>/coffee'
      target:
        base: 'target'
        documentation: '<%= dirs.target.base %>/documentation'
        test:
          base: '<%= dirs.target.base %>/test'
          js:
            base: '<%= dirs.target.test.base %>/javascript'
            css: '<%= dirs.target.test.js.base %>/css'
            scripts: '<%= dirs.target.test.js.base %>/scripts'
    handlebars:
      test:
        files: [
          cwd: '<%= dirs.src.javascripts %>/templates/'
          dest: '<%= dirs.target.test.js.scripts %>/templates/'
          expand: true
          ext: '.js'
          src: ['**/*.hbs']
        ]
        options:
          namespace: 'Handlebars.templates'
          processName: (filePath) ->
            filePath.replace /^grails-app\/assets\/javascripts\/templates\/(.+)\.hbs/, '$1'
    less:
      test:
        files:
            '<%= dirs.target.test.js.css %>/main.css':
              '<%= dirs.src.stylesheets %>/main.less'
            '<%= dirs.target.test.js.css %>/document.css':
              '<%= dirs.src.stylesheets %>/document.less'
        options:
          path: '<%= dirs.src.stylesheets %>/'
    pkg: grunt.file.readJSON 'package.json'
    watch:
      coffee:
        files: [
          '<%= dirs.src.coffee %>/*.coffee'
          '<%= dirs.src.test.coffee %>/*.coffee'
        ]
        tasks: ['coffee']
      less:
        files: ['<%= dirs.src.stylesheets %>/*.less']
        tasks: ['less']
      testCases:
        files: ['<%= dirs.src.test.base %>/*.html']
        tasks: ['copy:test']

  grunt.registerTask 'default', [
    'clean:test', 'less:test', 'coffee:test', 'handlebars:test', 'copy:test'
  ]
  grunt.registerTask 'documentation', [
    'clean:documentation', 'codo:documentation'
  ]
  grunt.registerTask 'publish', [
    'clean:publish', 'copy:publish', 'concat:publish'
  ]

