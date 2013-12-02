import grails.test.AbstractCliTestCase

class StandaloneTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testStandalone() {

        execute(["standalone"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
