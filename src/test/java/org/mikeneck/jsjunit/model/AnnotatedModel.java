package org.mikeneck.jsjunit.model;

import org.mikeneck.jsjunit.annotation.JsMapping;

/**
 */
public class AnnotatedModel {

    @JsMapping (getFunction = "getAndAdd", order = 0)
    private int getAndAddCount;

    @JsMapping (getFunction = "addAndGet", order = 1)
    private int addAndGetCount;

    public int getAddAndGetCount() {
        return addAndGetCount;
    }

    public void setAddAndGetCount(int addAndGetCount) {
        this.addAndGetCount = addAndGetCount;
    }

    public int getGetAndAddCount() {
        return getAndAddCount;
    }

    public void setGetAndAddCount(int getAndAddCount) {
        this.getAndAddCount = getAndAddCount;
    }
}
