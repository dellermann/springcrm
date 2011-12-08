package org.amcworld.springcrm

class Reminder {

    static constraints = {
        value(nullable:false, min:0)
        unit(nullable:false, inList:['m', 'h', 'd', 'w'])
        nextReminder(nullable:false)
        calendarEvent(nullable:false)
        user(nullable:true)
    }
    static belongsTo = [ calendarEvent:CalendarEvent, user:User ]
    static mapping = {
        version false
    }
    static transients = [ 'rule', 'valueAsMilliseconds' ]

    int value
    String unit
    Date nextReminder

    String getRule() {
        return "${value}${unit}"
    }

    long getValueAsMilliseconds() {
        long v = value * 1000       // sec
        switch (unit) {
        case 'm':
            v *= 60
            break
        case 'h':
            v *= 3600
            break
        case 'd':
            v *= 86400
            break
        case 'w':
            v *= 604800
            break
        }
        return v
    }

    static Reminder fromRule(String rule) {
        def m = (rule =~ /^(\d+)([mhdw])$/)
        if (!m) {
            throw new IllegalArgumentException("Rule ${rule} is not valid.")
        }
        return new Reminder([ value:m[0][1] as Integer, unit:m[0][2] ])
    }
}
