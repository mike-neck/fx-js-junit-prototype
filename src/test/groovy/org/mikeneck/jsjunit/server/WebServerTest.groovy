package org.mikeneck.jsjunit.server

import org.junit.BeforeClass
import org.junit.AfterClass
import org.junit.Test
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.HttpClient
import org.mikeneck.jsjunit.server.WebServer

/**
 * Created with IntelliJ IDEA.
 * User: mike
 * Date: 12/06/09
 * Time: 18:45
 * To change this template use File | Settings | File Templates.
 */
class WebServerTest {

    static WebServer server

    @BeforeClass
    static void startServer () {
        server = new WebServer('localhost', 3003)
        server.start()
    }

    @Test
    void testConnection () {
        def get = new HttpGet('http://localhost:3003')
        HttpClient client = new DefaultHttpClient()
        def execute = client.execute(get)
        def reader = new InputStreamReader(execute.entity.content)
        def writer = new StringWriter()
        reader.eachLine {
            writer << it
            writer << '\n'
        }
        reader.close()
        assert writer.toString().contains('test page')
    }

    @Test
    void testGetJsObjects () {
        def get = new HttpGet('http://localhost:3003/js/jquery-1.7.2.js')
        HttpClient client = new DefaultHttpClient()
        def execute = client.execute(get)
        def reader = new InputStreamReader(execute.entity.content)
        def writer = new StringWriter()
        reader.eachLine {
            writer << it
            writer << '\n'
        }
        reader.close()
        assert writer.toString().contains('jQuery')
    }

    @AfterClass
    static void stopServer () {
        server.stop()
    }
}
