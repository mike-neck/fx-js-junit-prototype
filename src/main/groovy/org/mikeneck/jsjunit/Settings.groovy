package org.mikeneck.jsjunit

/**
 */
class Settings {

    public static final String DEFAULT_SERVER = 'localhost'

    public static final int DEFAULT_PORT = 3000

    String hostName

    int portNumber

    String identifier

    Settings identifiedBy (String id) {
        this.identifier = id
        this
    }

    UseFxWebView get () throws IllegalAccessException {
        if (canBeInitialized()) {
            new UseFxWebView(hostName: hostName, portNumber: portNumber, identifier: identifier)
        } else {
            throw new IllegalAccessException('Settings is accessed illegally!')
        }
    }

    UseFxWebView customRoot (String root) throws IllegalAccessException {
        if (canBeInitialized()) {
            new UseFxWebView(hostName: hostName, portNumber: portNumber, identifier: identifier, webRoot: root)
        } else {
            throw new IllegalAccessException('Settings is accessed illegally!')
        }
    }

    private boolean canBeInitialized () {
        return hostName != null && hostName.size() > 0 &&
                portNumber != null && portNumber != 0 &&
                identifier != null && identifier.size() > 0
    }
}
