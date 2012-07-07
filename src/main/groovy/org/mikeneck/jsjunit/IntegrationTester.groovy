package org.mikeneck.jsjunit

import org.junit.runners.model.Statement
import org.junit.runner.Description
import org.mikeneck.jsjunit.server.WebServer
import org.mikeneck.jsjunit.client.WebClientImpl

/**
 */
class IntegrationTester extends Statement {

    String hostName

    int portNumber

    String identifier

    Statement base

    String webRoot

    Description description

    WebServer server

    WebClientImpl webClient

    @Override
    void evaluate() {
        String path = setup()

        base.evaluate()

        finishTest(path)
    }

    private String setup() {
        String path = exchangeWebRoot()
        server = startServer()
        webClient = new WebClientImpl(identifier, "http://${hostName}:${portNumber}")
        return path
    }

    private String exchangeWebRoot() {
        def path = WebServer.rootPath
        if (webRoot != null && webRoot != '') {
            WebServer.rootPath = webRoot
        }
        return path
    }

    private WebServer startServer() {
        server = new WebServer(hostName, portNumber)
        server.start()
        server
    }

    private void finishTest(String path) {
        webClient.shutdown()
        server.stop()
        if (webRoot != null && webRoot != '') {
            WebServer.rootPath = path
        }
    }

    public JsJUnit getJsJUnit () {
        this.webClient
    }
}
