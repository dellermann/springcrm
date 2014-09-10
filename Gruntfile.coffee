module.exports = (grunt) ->
  grunt.initConfig
    clean:
      documentation: ['<%= dirs.target.documentation %>']
      test: ['<%= dirs.target.test.base %>']
    codo:
      documentation:
        dest: '<%= dirs.target.documentation %>'
        options:
          name: 'SpringCRM'
          private: true
          title: 'SpringCRM CoffeeScript documentation'
        src: ['<%= dirs.src.coffee %>']
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
    copy:
      test:
        files: [
            cwd: '<%= dirs.src.test.base %>/'
            dest: '<%= dirs.target.test.js.base %>/'
            expand: true
            src: ['*.html']
          ,
            cwd: '<%= dirs.src.bower.qunit %>/qunit/'
            dest: '<%= dirs.target.test.js.css %>/'
            expand: true
            src: 'qunit.css'
          ,
            cwd: '<%= dirs.src.bower.qunit %>/qunit/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: 'qunit.js'
          ,
            cwd: '<%= dirs.src.javascript %>/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: [
              '_jquery.js'
              '_jquery-ui.js'
              '_jquery-autosize.js'
            ]
          ,
            cwd: '<%= dirs.src.bower.jqueryMockjax %>/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: 'jquery.mockjax.js'
          ,
            cwd: '<%= dirs.src.assets %>/'
            dest: '<%= dirs.target.test.js.base %>/'
            expand: true
            src: 'fonts/**'
        ]
    dirs:
      src:
        assets: '<%= dirs.src.grailsApp %>/assets'
        base: '.'
        bower:
          base: '<%= dirs.src.base %>/bower_components'
          jquery: '<%= dirs.src.bower.base %>/jquery'
          jqueryMockjax: '<%= dirs.src.bower.base %>/jquery-mockjax'
          jqueryUi: '<%= dirs.src.bower.base %>/jquery-ui'
          jqueryUiTouchPunch: '<%= dirs.src.bower.base %>/jquery-ui-touch-punch-working'
          qunit: '<%= dirs.src.bower.base %>/qunit'
        coffee: '<%= dirs.src.assets %>/javascripts'
        grailsApp: '<%= dirs.src.base %>/grails-app'
        javascript: '<%= dirs.src.assets %>/javascripts'
        stylesheet: '<%= dirs.src.assets %>/stylesheets'
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
    less:
      test:
        files:
            '<%= dirs.target.test.js.css %>/main.css': '<%= dirs.src.stylesheet %>/main.less'
            '<%= dirs.target.test.js.css %>/document.css': '<%= dirs.src.stylesheet %>/document.less'
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
        files: ['<%= dirs.src.stylesheet %>/*.less']
        tasks: ['less']
      testCases:
        files: ['<%= dirs.src.test.base %>/*.html']
        tasks: ['copy:test']

  grunt.loadNpmTasks 'grunt-contrib-clean'
  grunt.loadNpmTasks 'grunt-contrib-coffee'
  grunt.loadNpmTasks 'grunt-contrib-copy'
  grunt.loadNpmTasks 'grunt-contrib-less'
  grunt.loadNpmTasks 'grunt-contrib-watch'
  grunt.loadNpmTasks 'grunt-codo'

  grunt.registerTask 'default', [
    'clean:test', 'less:test', 'coffee:test', 'copy:test'
  ]
  grunt.registerTask 'documentation', [
    'clean:documentation', 'codo:documentation'
  ]

# vim:set ts=2 sw=2 sts=2:

