package org.mikeneck.jsjunit.client

import org.junit.BeforeClass
import org.junit.AfterClass
import org.junit.Test
import groovy.util.logging.Slf4j
import com.sun.webpane.webkit.JSObject
import org.junit.Rule
import org.junit.rules.TestName
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

/**
 * Created with IntelliJ IDEA.
 * User: mike
 * Date: 12/06/10
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
class WebClientTest {

    @Rule
    public TestName name = new TestName()

    static WebClient client

    @BeforeClass
    static void launch () {
        client = new WebClientImpl('WebClientTest', 'http://www.oracle.com/index.html')
    }

    @AfterClass
    static void shutdown () {
        client.shutdown()
    }

    @Test
    void integerTest () {
        def result = client.callAsInt('1 + 1')
        assert result.class == Integer
        assert result == 2
    }

    @Test
    void longTest () {
        def result = client.callAsLong("${Integer.MAX_VALUE} + 1")
        assert result.class == Long
        assert result == Integer.MAX_VALUE + 1L
    }

    @Test
    void doubleTest () {
        def result = client.callAsDouble('0.1 + 0.01')
        assert result.class == Double
        assert result == 0.11
    }

    @Test
    void stringTest () {
        def result = client.callAsString('"test"')
        assert result == 'test'
    }

    @Test
    void dateTest () {
        def before = new Date()
        def date = client.callAsDate('new Date()')
        def after = new Date()
        ['year', 'month', 'date', 'hours', 'minutes', 'seconds'].each {
            assert before."$it" <= date."$it" && date."$it" <= after."$it"
        }
    }

    @Test (expected = GroovyCastException)
    void dateTest2 () {
        client.call('new Date()') as Date
    }

    @Test
    void objectTest () {
        def result = client.call('(function() {return {name : "name", age : 20, say : function() {return "I am " + this.name;}};})()')
        def json = result as JSObject
        assert json.getMember('name') == 'name'
        assert json.getMember('age') == 20
        log.info name.methodName
        log.info(json.getMember('say').dump())
    }
}
