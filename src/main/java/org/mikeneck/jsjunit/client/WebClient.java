package org.mikeneck.jsjunit.client;

import org.mikeneck.jsjunit.JsJUnit;

/**
 */
public interface WebClient extends JsJUnit {

    public void shutdown () throws IllegalStateException;
}
