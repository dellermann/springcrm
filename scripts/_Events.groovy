/*
 * _Events.groovy
 *
 * Copyright (c) 2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


import java.text.SimpleDateFormat


eventCompileStart = { kind ->
    def buildNumber = metadata.'app.buildNumber'
    if (!buildNumber) {
        buildNumber = 1L
    } else {
        buildNumber = Long.valueOf(buildNumber) + 1
    }
    metadata.'app.buildNumber' = buildNumber.toString()
 
    def formatter = new SimpleDateFormat('''yyyy-MM-dd'T'HH:mm:ssZ''')
    metadata.'app.buildDate' = formatter.format(new Date())
    metadata.'app.buildProfile' = grailsEnv

    metadata.persist()
    println "| Set build number #${buildNumber}."
}
