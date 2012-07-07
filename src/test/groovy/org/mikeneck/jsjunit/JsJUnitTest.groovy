package org.mikeneck.jsjunit

import org.junit.ClassRule
import org.junit.Test
import groovy.util.logging.Slf4j
import org.junit.Before
import com.sun.webpane.webkit.JSObject

/**
 *
 */
@Slf4j
class JsJUnitTest {

    @ClassRule
    static public UseFxWebView fxWebView = UseFxWebView.defaultServer().identifiedBy('JsJUnitTest').get()

    JsJUnit tester

    @Before
    void setUp () {
        tester = fxWebView.getJsTester()
    }

    @Test
    void testString () {
        assert tester.callAsString('stringTest("test")') == 'hello test'
    }

    @Test
    void testNumber () {
        assert tester.callAsDouble('numberTest(0.1, 0.01)') == 0.11
        assert tester.callAsInt('numberTest("a", 1)') == 0
    }

    @Test
    void jsonTest () {
        def object = tester.call('jsonTest ()') as JSObject
        assert object.getMember('name') == 'name'
        assert object.getMember('age') == 1
    }

    @Test
    void personTest () {
        def object = tester.call('new Person("mike")') as JSObject
        assert object.getMember('name') == 'mike'
    }

    @Test
    void personMethodTest () {
        assert tester.call('(new Person("mike")).say()') == 'this is mike'
    }
}
