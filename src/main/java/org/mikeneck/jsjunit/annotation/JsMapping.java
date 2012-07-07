package org.mikeneck.jsjunit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsMapping {

    public String propertyName() default "";

    public String getFunction() default "";

    public int order() default 0;

}
