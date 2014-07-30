module.exports = (grunt) ->
  grunt.initConfig
    clean:
      test: ['<%= dirs.target.test.base %>']
    coffee:
      test:
        files: [
          cwd: '<%= dirs.src.coffee %>/'
          dest: '<%= dirs.target.test.js.scripts %>/'
          expand: true
          ext: '.js'
          src: ['*.coffee']
        ]
    copy:
      test:
        files: [
            cwd: '<%= dirs.src.testCases %>/'
            dest: '<%= dirs.target.test.js.base %>/'
            expand: true
            src: ['*.html']
          ,
            cwd: '<%= dirs.node.base %>/qunitjs/qunit/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: 'qunit.js'
          ,
            cwd: '<%= dirs.src.javascript %>/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: 'jquery.js'
          ,
            cwd: '<%= dirs.node.base %>/jquery-mockjax/'
            dest: '<%= dirs.target.test.js.scripts %>/'
            expand: true
            src: 'jquery.mockjax.js'
          ,
            cwd: '<%= dirs.node.base %>/qunitjs/qunit/'
            dest: '<%= dirs.target.test.js.css %>/'
            expand: true
            src: 'qunit.css'
        ]
    dirs:
      node:
        base: 'node_modules'
      src:
        assets: '<%= dirs.src.grailsApp %>/assets'
        base: '.'
        bower:
          base: '<%= dirs.src.base %>/bower_components'
          jquery: '<%= dirs.src.bower.base %>/jquery'
          jqueryUi: '<%= dirs.src.bower.base %>/jquery-ui'
          jqueryUiTouchPunch: '<%= dirs.src.bower.base %>/jquery-ui-touch-punch-working'
        coffee: '<%= dirs.src.assets %>/javascripts'
        grailsApp: '<%= dirs.src.base %>/grails-app'
        javascript: '<%= dirs.src.assets %>/javascripts'
        testCases: '<%= dirs.src.base %>/test/js/tests'
      target:
        base: 'target'
        test:
          base: '<%= dirs.target.base %>/test'
          js:
            base: '<%= dirs.target.test.base %>/javascript'
            css: '<%= dirs.target.test.js.base %>/css'
            scripts: '<%= dirs.target.test.js.base %>/scripts'
    pkg: grunt.file.readJSON 'package.json'
    watch:
      coffee:
        files: ['<%= dirs.src.coffee %>/*.coffee']
        tasks: ['coffee']
      testCases:
        files: ['<%= dirs.src.testCases %>/*.html']
        tasks: ['copy:test']

  grunt.loadNpmTasks 'grunt-contrib-clean'
  grunt.loadNpmTasks 'grunt-contrib-coffee'
  grunt.loadNpmTasks 'grunt-contrib-copy'
  grunt.loadNpmTasks 'grunt-contrib-watch'

  grunt.registerTask 'default', ['clean:test', 'coffee:test', 'copy:test']

# vim:set ts=2 sw=2 sts=2:

