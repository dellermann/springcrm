package org.amcworld.springcrm

import groovy.io.FileType

import javax.servlet.ServletContext;
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

class InstallService {

    ServletContext servletContext = SCH.servletContext

    boolean isInstallerDisabled() {
        return !lockFile.exists()
    }

    void disableInstaller() {
        lockFile.delete()
    }

    List<String> getBaseDataPackages() {
        def files = []
        dataDir.eachFileRecurse FileType.FILES, {
                def m = (it.name =~ /^base-data-([-\w]+)\.sql$/)
                if (m.matches()) {
                    files.add(m[0][1])
                }
            }
        return files
    }

    File loadPackage(String key) {
        return new File(dataDir, "base-data-${key}.sql")
    }

    private File getDataDir() {
        return new File(servletContext.getRealPath('/WEB-INF/data/install'))
    }

    private File getLockFile() {
        return new File(servletContext.getRealPath('/WEB-INF/data/install'), 'ENABLE_INSTALLER')
    }
}
