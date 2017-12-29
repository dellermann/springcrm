package org.amcworld.springcrm

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class RoleGroupServiceSpec extends Specification {

    RoleGroupService roleGroupService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new RoleGroup(...).save(flush: true, failOnError: true)
        //new RoleGroup(...).save(flush: true, failOnError: true)
        //RoleGroup roleGroup = new RoleGroup(...).save(flush: true, failOnError: true)
        //new RoleGroup(...).save(flush: true, failOnError: true)
        //new RoleGroup(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //roleGroup.id
    }

    void "test get"() {
        setupData()

        expect:
        roleGroupService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<RoleGroup> roleGroupList = roleGroupService.list(max: 2, offset: 2)

        then:
        roleGroupList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        roleGroupService.count() == 5
    }

    void "test delete"() {
        Long roleGroupId = setupData()

        expect:
        roleGroupService.count() == 5

        when:
        roleGroupService.delete(roleGroupId)
        sessionFactory.currentSession.flush()

        then:
        roleGroupService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        RoleGroup roleGroup = new RoleGroup()
        roleGroupService.save(roleGroup)

        then:
        roleGroup.id != null
    }
}
