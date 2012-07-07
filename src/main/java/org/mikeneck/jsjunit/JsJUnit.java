package org.mikeneck.jsjunit;

import netscape.javascript.JSException;

import java.util.Date;

/**
 */
public interface JsJUnit {

    public int callAsInt (String callee) throws IllegalStateException, JSException;

    public long callAsLong (String callee) throws IllegalStateException, JSException;

    public double callAsDouble (String callee) throws IllegalStateException, JSException;

    public String callAsString (String callee) throws IllegalStateException, JSException;

    public Date callAsDate (String callee) throws IllegalStateException, JSException;

    public Object call (String callee) throws IllegalStateException, JSException;

    public <M> M callAs (String callee, Class<M> expectedModel) throws IllegalStateException, JSException;
}
