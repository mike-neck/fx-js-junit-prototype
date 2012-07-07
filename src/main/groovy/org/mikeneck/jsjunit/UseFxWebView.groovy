package org.mikeneck.jsjunit

import org.junit.rules.TestRule
import org.junit.runners.model.Statement
import org.junit.runner.Description

/**
 */
class UseFxWebView implements TestRule {

    String hostName

    int portNumber

    String identifier

    String webRoot

    IntegrationTester tester

    static Settings defaultServer () {
        new Settings(hostName: Settings.DEFAULT_SERVER, portNumber: Settings.DEFAULT_PORT)
    }

    static Settings port (int portNumber) {
        new Settings(hostName: Settings.DEFAULT_SERVER, portNumber: portNumber)
    }

    @Override
    Statement apply(Statement base, Description description) {
        tester = new IntegrationTester(
                hostName: hostName,
                portNumber: portNumber,
                identifier: identifier,
                webRoot: webRoot,
                base: base,
                description: description)
        return tester
    }

    JsJUnit getJsTester () {
        tester.getJsJUnit()
    }
}
