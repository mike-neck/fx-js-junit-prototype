package org.mikeneck.jsjunit.server

import groovy.util.logging.Slf4j
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.Request
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.nio.file.Path
import groovy.io.FileType

/**
 */
@Slf4j
class WebHandler extends AbstractHandler {

    def map = [:]

    static {
        File.metaClass.define {
            collectRecurse = {FileType type, Closure c ->
                def result = []
                delegate.eachFileRecurse (type) {
                    result << c (it)
                }
                return result
            }
            collectEntriesRecurse = {FileType type, Closure c ->
                def result = [:]
                delegate.eachFileRecurse (type) {
                    def res = c(it)
                    if (res instanceof List) {
                        result << [(res[0]) : res[1]]
                    } else {
                        result << res
                    }
                }
                result
            }
        }
    }

    WebHandler (String root) {
        def rootDir = new File(root)
        map << ['/' : new File("${root}/index.html")]
        map << rootDir.collectEntriesRecurse (FileType.FILES) {
            [(it.absolutePath.replace(rootDir.absolutePath, '')) : it]
        }
    }

    @Override
    void handle(
            String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        org.mikeneck.jsjunit.server.WebHandler.log.info(target)
        if (map[target] == null) {
            response.status = 404
        } else {
            response.status = 200
            File content = map[target]
            def type = content.name.endsWith('css') ? 'text/css' : content.name.endsWith('js') ? 'text/javascript' : 'text/html'
            response.contentType = type
            def writer = response.writer
            content.eachLine {
                writer << it
            }
            writer.flush()
        }
    }
}
