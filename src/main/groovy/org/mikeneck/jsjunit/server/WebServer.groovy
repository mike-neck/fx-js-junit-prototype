package org.mikeneck.jsjunit.server

import java.util.concurrent.ExecutorService
import groovy.util.logging.Slf4j
import java.util.concurrent.Executors
import org.eclipse.jetty.server.Server

/**
 */
@Slf4j
class WebServer {

    static String rootPath = 'src/test/resources/www'

    String hostName

    int portNumber

    private ExecutorService service

    WebServer (int portNumber) {
        this.hostName = 'localhost'
        this.portNumber = portNumber
        initService()
    }

    WebServer (String hostName, int portNumber) {
        this.hostName = hostName
        this.portNumber = portNumber
        initService()
    }

    void initService () {
        ExecutorService.metaClass.define {
            execute = {Closure closure ->
                delegate.execute new Runnable() {
                    @Override
                    void run() {
                        closure ()
                    }
                }
            }
        }
        service = Executors.newSingleThreadExecutor()
    }

    void start () {
        service.execute {
            def address = new InetSocketAddress(hostName, portNumber)
            def server = new Server(address)
            server.handler = new WebHandler(rootPath)
            org.mikeneck.jsjunit.server.WebServer.log.info('server {} started on {}[{}:{}]', server, address, hostName, portNumber)
            server.start()
        }
    }

    void stop () {
        service.shutdownNow()
    }
}
