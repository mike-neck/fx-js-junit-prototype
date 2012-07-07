package org.mikeneck.jsjunit.client

import org.junit.Before
import javafx.application.Application
import org.junit.Test
import org.junit.BeforeClass
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import groovy.util.logging.Slf4j
import javafx.concurrent.Worker
import org.junit.Rule
import org.junit.rules.TestName
import org.junit.AfterClass

/**
 * Created with IntelliJ IDEA.
 * User: mike
 * Date: 12/06/10
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
class ClientApplicationTest {

    static ExecutorService service = Executors.newCachedThreadPool()

    static ClientApplication application

    static final String ID = 'ClientApplicationTest'

    @Rule
    public TestName name = new TestName()

    @BeforeClass
    static void metaClassDefine () {
        ExecutorService.metaClass.define {
            execute = {Closure c ->
                delegate.execute (new Runnable(){
                    @Override
                    void run() {
                        c ()
                    }
                })
            }
        }
        service.execute {
            log.info("setup start")
            Application.launch(ClientApplication, ID, 'http://www.oracle.com/index.html')
        }
        while (application == null) {
            application = ClientApplication.myInstance(ID)
            sleep(100L)
        }
    }

    @Before
    void setUp () {
        log.info('setup - {} has started on application {}', name.methodName, application)
        application.reload()
        while (application.engineState != Worker.State.SUCCEEDED) {
            log.info('application state - {}', application.engineState)
            sleep(100L)
        }
    }

    @Test
    void verifyApplicationAndEngine () {
        log.info('test - {} started', name.methodName)
        assert application != null
        assert application.engineState == Worker.State.SUCCEEDED
        assert application.engineLocation == 'http://www.oracle.com/index.html'
    }

    @Test
    void callFunction () {
        log.info('test - {} started', name.methodName)
        def script = '1 + 1'
        def result = application.callFunction(script)
        assert result == 2
    }

    @AfterClass
    static void tearDown () {
        application.shutDown()
    }
}
