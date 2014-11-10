module.exports = (grunt) ->
  grunt.initConfig
    bower:
      install:
        options:
          targetDir: '<%= dirs.bower.base %>/'
    clean:
      documentation: ['<%= dirs.target.documentation %>']
      publish: [
        '<%= dirs.src.stylesheets %>/font-awesome/'
        '<%= dirs.src.fonts %>/'
        '<%= dirs.src.images %>/lightbox/'
        '<%= dirs.src.javascripts %>/lang/fullcalendar/'
      ]
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
      publish:
        files: [
            cwd: '<%= dirs.bower.blueimpLoadImage %>/js/'
            dest: '<%= dirs.src.javascripts %>/'
            expand: true
            rename: (dest, src) -> "#{dest}_#{src}"
            src: [
              'load-image-exif.js'
              'load-image-exif-map.js'
              'load-image-ios.js'
              'load-image-meta.js'
              'load-image-orientation.js'
              'load-image.js'
            ]
          ,
            cwd: '<%= dirs.bower.fontAwesome %>/less/'
            dest: '<%= dirs.src.stylesheets %>/font-awesome/'
            expand: true
            rename: (dest, src) -> "#{dest}_#{src}"
            src: [
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
            dest: '<%= dirs.src.stylesheets %>/_fullcalendar.css'
            src: '<%= dirs.bower.fullCalendar %>/dist/fullcalendar.css'
          ,
            dest: '<%= dirs.src.javascripts %>/_fullcalendar.js'
            src: '<%= dirs.bower.fullCalendar %>/dist/fullcalendar.js'
          ,
            cwd: '<%= dirs.bower.fullCalendar %>/dist/lang/'
            dest: '<%= dirs.src.javascripts %>/lang/fullcalendar/'
            expand: true
            src: ['*.js']
          ,
            dest: '<%= dirs.src.javascripts %>/_jquery.js'
            src: '<%= dirs.bower.jquery %>/dist/jquery.js'
          ,
            dest: '<%= dirs.src.javascripts %>/_jquery-autosize.js'
            src: '<%= dirs.bower.jqueryAutosize %>/jquery.autosize.js'
          ,
            cwd: '<%= dirs.bower.jqueryFileUpload %>/css/'
            dest: '<%= dirs.src.stylesheets %>/'
            expand: true
            rename: (dest, src) ->
              "#{dest}_#{src.replace(/jquery\./, 'jquery-')}"
            src: [
              'jquery.fileupload.css'
              'jquery.fileupload-ui.css'
            ]
          ,
            cwd: '<%= dirs.bower.jqueryFileUpload %>/js/'
            dest: '<%= dirs.src.javascripts %>/'
            expand: true
            rename: (dest, src) ->
              "#{dest}_#{src.replace(/jquery\./, 'jquery-')}"
            src: [
              'jquery.fileupload.js'
              'jquery.fileupload-audio.js'
              'jquery.fileupload-image.js'
              'jquery.fileupload-process.js'
              'jquery.fileupload-ui.js'
              'jquery.fileupload-validate.js'
              'jquery.fileupload-video.js'
            ]
          ,
            cwd: '<%= dirs.bower.jqueryFileUpload %>/img/'
            dest: '<%= dirs.src.assets %>/images/'
            expand: true
            src: ['*']
          ,
            dest: '<%= dirs.src.javascripts %>/_jquery-storage-api.js'
            src: '<%= dirs.bower.jqueryStorageAPI %>/jquery.storageapi.js'
          ,
            dest: '<%= dirs.src.javascripts %>/_jquery-ui.js'
            src: '<%= dirs.bower.jqueryUi %>/jquery/jquery-ui.js'
          ,
            dest: '<%= dirs.src.stylesheets %>/_lightbox.css'
            src: '<%= dirs.bower.lightbox %>/css/lightbox.css'
          ,
            dest: '<%= dirs.src.javascripts %>/_lightbox.js'
            src: '<%= dirs.bower.lightbox %>/js/lightbox.js'
          ,
            cwd: '<%= dirs.bower.lightbox %>/img/'
            dest: '<%= dirs.src.images %>/lightbox/'
            expand: true
            src: [
              'close.png'
              'loading.gif'
              'next.png'
              'prev.png'
            ]
          ,
            dest: '<%= dirs.src.javascripts %>/_moment.js'
            src: '<%= dirs.bower.moment %>/moment.js'
        ]
        options:
          encoding: null
          noProcess: [
            '**/*.{eot|gif|jpg|js|otf|png|ttf|woff}'
          ]
          process: (contents, srcPath) ->
            g = grunt
            conf = g.config
            file = g.file

            lb = conf.get 'dirs.bower.lightbox'
            fa = conf.get 'dirs.bower.fontAwesome'
            if file.arePathsEquivalent srcPath, "#{lb}/css/lightbox.css"
              contents = String(contents)
              contents = contents.replace /\.\.\/img\//g, '../images/lightbox/'
            else if file.arePathsEquivalent srcPath, "#{fa}/less/core.less"
              contents = String(contents)
              contents = contents.replace /\.@\{fa-css-prefix\}/, '.fa'
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
        blueimpLoadImage: '<%= dirs.bower.base %>/blueimp-load-image'
        fontAwesome: '<%= dirs.bower.base %>/font-awesome'
        fullCalendar: '<%= dirs.bower.base %>/fullcalendar'
        handlebars: '<%= dirs.bower.base %>/handlebars'
        jquery: '<%= dirs.bower.base %>/jquery'
        jqueryAutosize: '<%= dirs.bower.base %>/jquery-autosize'
        jqueryFileUpload: '<%= dirs.bower.base %>/jquery-file-upload'
        jqueryMockjax: '<%= dirs.bower.base %>/jquery-mockjax'
        jqueryStorageAPI: '<%= dirs.bower.base %>/jQuery-Storage-API'
        jqueryUi: '<%= dirs.bower.base %>/jquery-ui'
        jqueryUiTouchPunch:
          '<%= dirs.bower.base %>/jquery-ui-touch-punch-working'
        lightbox: '<%= dirs.bower.base %>/lightbox'
        moment: '<%= dirs.bower.base %>/moment'
        qunit: '<%= dirs.bower.base %>/qunit'
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

  grunt.loadNpmTasks 'grunt-bower-task'
  grunt.loadNpmTasks 'grunt-contrib-clean'
  grunt.loadNpmTasks 'grunt-contrib-coffee'
  grunt.loadNpmTasks 'grunt-contrib-copy'
  grunt.loadNpmTasks 'grunt-contrib-handlebars'
  grunt.loadNpmTasks 'grunt-contrib-less'
  grunt.loadNpmTasks 'grunt-contrib-watch'
  grunt.loadNpmTasks 'grunt-codo'

  grunt.registerTask 'default', [
    'clean:test', 'less:test', 'coffee:test', 'handlebars:test', 'copy:test'
  ]
  grunt.registerTask 'documentation', [
    'clean:documentation', 'codo:documentation'
  ]
  grunt.registerTask 'publish', 'Copy library code to project.', ->
    g = grunt

    g.task.run ['bower:install', 'clean:publish', 'copy:publish']
    g.log.writelns '!!'.blue, 'Don\'t forget to change',
      (g.config.get('dirs.src.stylesheets') + '/_jquery-ui.css').green,
      'after installing a new version of jQueryUI.'

# vim:set ts=2 sw=2 sts=2:

