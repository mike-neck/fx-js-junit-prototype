package org.mikeneck.jsjunit.client

import javafx.application.Application
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javafx.concurrent.Worker
import com.sun.webpane.webkit.JSObject
import groovy.util.logging.Slf4j

/**
 */
@Slf4j
class WebClientImpl implements WebClient {

    final String id

    final String url

    ClientApplication application = null

    static {
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
    }

    WebClientImpl (String id, String url) {
        this.id = id
        this.url = url
        def executor = Executors.newSingleThreadExecutor()
        executor.execute {
            Application.launch(ClientApplication, this.id, this.url)
        }
        while (application == null) {
            application = ClientApplication.myInstance(this.id)
            sleep(100L)
        }
        application.reload()
        while (application.engineState != Worker.State.SUCCEEDED) {
            sleep(100L)
        }
    }

    @Override
    int callAsInt(String callee) {
        call(callee) as int
    }

    @Override
    long callAsLong(String callee) {
        call(callee) as long
    }

    @Override
    double callAsDouble(String callee) {
        call(callee) as double
    }

    @Override
    String callAsString(String callee) {
        call(callee) as String
    }

    @Override
    Date callAsDate(String callee) {
        def function = """
(function(){
    var mfunc=function(){return ${callee};},
        r = mfunc();
        return {
            year : r.getFullYear(),
            month : r.getMonth(),
            date : r.getDate(),
            hours : r.getHours(),
            minutes : r.getMinutes(),
            seconds : r.getSeconds(),
            milliseconds : r.getMilliseconds()};
})()"""
        def obj = call(function) as JSObject
        def cal = Calendar.instance
        cal.set(
                obj.getMember('year'),
                obj.getMember('month'),
                obj.getMember('date'),
                obj.getMember('hours'),
                obj.getMember('minutes'),
                obj.getMember('seconds')
        )
        return cal.time
    }

    @Override
    Object call(String callee) {
        if (application == null) {
            throw new IllegalStateException('WebClient is not available after shutdown.')
        }
        return application.callFunction(callee)
    }

    @Override
    def <M> M callAs(String callee, Class<M> expectedModel) {
        null
    }

    @Override
    void shutdown () {
        if (application == null) {
            throw new IllegalStateException('WebClient is not available after shutdown.')
        }
        application.shutDown()
        application = null
    }
}
