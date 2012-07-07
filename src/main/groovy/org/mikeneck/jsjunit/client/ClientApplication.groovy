package org.mikeneck.jsjunit.client

import javafx.application.Application
import javafx.stage.Stage
import groovy.util.logging.Slf4j
import javafx.scene.web.WebEngine
import javafx.application.Platform
import java.util.concurrent.BlockingQueue
import javafx.concurrent.Worker
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import netscape.javascript.JSException

/**
 */
@Slf4j
class ClientApplication extends Application {

    WebEngine engine

    static private Map <String, ClientApplication> browsers = [:]

    String identifier

    String url

    static {
        Platform.metaClass.'static'.runLater = { Closure c ->
            Platform.runLater(new Runnable(){
                @Override
                void run() {
                    c ()
                }
            })
        }
    }

    static ClientApplication myInstance (String id) {
        browsers[id]
    }

    @Override
    void start(Stage stage) {
        identifier = parameters.raw[0]
        url = parameters.raw[1]
        log.info ("Application for {} started", identifier)

        browsers << [(identifier) : this]
    }

    Worker.State getEngineState () {
        final BlockingQueue<Worker.State> state = new LinkedBlockingQueue<>()
        Platform.runLater {
            state.put(engine.loadWorker.state)
        }
        return state.take()
    }

    String getEngineLocation () {
        final BlockingQueue<String> location = new LinkedBlockingQueue<>()
        Platform.runLater {
            location.put(engine.getLocation())
        }
        location.take()
    }

    void reload() {
        final BlockingQueue<Worker.State> states = new LinkedBlockingQueue<>()
        Platform.runLater {
            engine = new WebEngine()
            engine.load(url)
            states.put(engine.loadWorker.state)
        }
        states.take()
    }

    Object callFunction (String function) throws JSException {
        final BlockingQueue<Object> result = new LinkedBlockingQueue<>()
        Platform.runLater {
            try {
                log.info function
                def object = engine.executeScript(function)
                result.put(object)
            } catch (JSException e) {
                result.put(e)
            }
        }
        def object = result.take()
        if (object instanceof JSException) {
            throw object
        }
        return object
    }

    void shutDown () {
        final BlockingQueue<Boolean> queue = new LinkedBlockingQueue<>()
        Platform.runLater {
            Platform.exit()
            queue.put(true)
        }
        queue.take()
        browsers.remove(identifier)
    }
}
