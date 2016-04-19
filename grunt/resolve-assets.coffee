module.exports = (grunt) ->
  grunt.registerMultiTask 'resolveAssets', 'Resolves asset dependencies', ->
    glob = require 'glob'
    path = require 'path'

    reqExpRequire = /^\s*#=\s*require\s+(.+)\s*$/
    regExpTag = /<asset:script\s+src=['"]([^'"]+)['"]\s*\/>/g

    getAllDependencies = (assets, assetsDir) ->
      files = new Map()
      for file in glob.sync path.join(assetsDir, assets)
        p = path.relative(assetsDir, file).replace /\.(coffee|hbs|js)$/, ''
        files.set p, getDependencies(file)

      files

    getDependencies = (file) ->
      re = reqExpRequire
      dependencies = []
      content = grunt.file.read file
      for line in content.split /[\r\n]/
        matches = line.match re
        dependencies.push matches[1] if matches

      dependencies

    resolveDependencies = (dm, asset) ->
      res = []
      dependencies = dm.get asset
      if dependencies
        for dep in dependencies
          res.push d for d in resolveDependencies(dm, dep) when res.indexOf(d) < 0
        res.push asset

      res

    dependencyMap = getAllDependencies @data.assets, @data.assetsDir
    destScriptsDir = @data.destScriptsDir

    @files.forEach (file) ->
      dm = dependencyMap
      scripts = destScriptsDir
      grunt.log.ok "Processing assets #{file.src} → #{file.dest}"
      grunt.file.copy file.src, file.dest,
        process: (content) ->
          content.replace regExpTag, (_, asset) ->
            s = []
            for dep in resolveDependencies dm, asset
              s.push "<script src=\"#{scripts}/#{dep}.js\"></script>"

            s.join '\n  '
