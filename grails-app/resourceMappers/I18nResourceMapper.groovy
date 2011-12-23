import java.text.Bidi
import java.text.DateFormatSymbols
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.plugin.resource.mapper.MapperPhase
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.context.support.MessageSourceSupport

class I18nResourceMapper {
    
    def phase = MapperPhase.GENERATION

    def priority = -1

    static defaultIncludes = [ '**/i18n-source.js' ]
    protected static final String FILE_SUFFIX = '-source.js'

    GrailsApplication grailsApplication
    MessageSourceSupport messageSource

    def map(resource, config) {
        File originalFile = resource.processedFile
        if (resource.sourceUrl && 
            originalFile.name.toLowerCase().endsWith(FILE_SUFFIX)) 
        {
            def msgs = [ : ]
    
            File input = getOriginalFile(resource.sourceUrl)
            loadMessages(input, msgs)
            
            def dfs = DateFormatSymbols.instance
            msgs['monthNamesLong'] = '[ "' << dfs.months.join('", "') << '" ]'
            msgs['monthNamesShort'] = '[ "' << dfs.shortMonths.join('", "') << '" ]'
            msgs['weekdaysLong'] = '[ "' << dfs.weekdays[1..-2].join('", "') << '" ]'
            msgs['weekdaysShort'] = '[ "' << dfs.shortWeekdays[1..-2].join('", "') << '" ]'

            def cal = Calendar.instance
            msgs['calendarFirstDay'] = cal.firstDayOfWeek
            String s = messageSource.getMessage('default.bidi.test', null, LCH.getLocale())
            msgs['calendarRTL'] = !(new Bidi(s, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isLeftToRight())

            StringBuilder buf = new StringBuilder('SPRINGCRM.addMessages({\n')
            for (String key : msgs.keySet()) {
                buf << '    ' << key << ': ' << msgs[key] << ',\n'
            }
            buf << '});\n'
            
            File output = new File(generateCompiledFilename(input.absolutePath))
            output.write(buf.toString())
    
            resource.processedFile = output
            resource.actualUrl = generateCompiledFilename(resource.originalUrl)
        }
    }
    
    protected void loadMessages(File input, Map<String, String> msgs) {
        input.eachLine {
            if (it ==~ /^\s*$/ || it ==~ /^\s*\/\/.*$/ || it ==~ /^\s*[\[\]].*$/) {
                return
            }
            
            def m = (it =~ /["'](.+)["']/)
            if (!m) {
                return
            }
            
            String key = m[0][1]
            m = (key =~ /^(.+)\{(.+)\}$/)
            if (m) {
                String parentKey = m[0][1]
                msgs[parentKey.replace('.', '_')] = 
                    renderMessages(m[0][2].split(/\s*,\s*/), parentKey)
            } else {
                String msg = messageSource.getMessage(key, null, LCH.getLocale())
                if (msg != null) msg = "\"${msg}\""
                msgs[key.replace('.', '_')] = msg
            }
        }
    }
    
    protected String renderMessages(String [] l, String keyPrefix = '', 
                                    int indent = 1) 
    {
        def msgs = [ : ]
        Locale locale = LCH.locale
        for (String key : l) {
            String msgKey = keyPrefix
            String jsKey = '""'
            if (key != "") {
                msgKey += '.'
                jsKey = key.replace('.', '_')
            }
            msgKey += key
            def msg = messageSource.getMessage(msgKey, null, locale)
            if (msg != null) msg = "\"${msg}\""
            msgs[jsKey] = msg
        }
        
        StringBuilder buf = new StringBuilder('{\n')
        for (String key : msgs.keySet().sort()) {
            buf << (' ' * ((indent + 1) * 4)) << key << ': ' << msgs[key] << ',\n'
        }
        buf << (' ' * (indent * 4)) << '}'
        return buf.toString() 
    }
    
    private String generateCompiledFilename(String original) {
        return original.replaceAll(/(?i)-source\.js/, '.js')
    }

    private File getOriginalFile(String sourcePath) {
        grailsApplication.parentContext.getResource(sourcePath).file
    }
}
