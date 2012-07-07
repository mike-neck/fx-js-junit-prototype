package org.mikeneck.jsjunit;

import org.junit.Test;
import org.mikeneck.jsjunit.model.AnnotatedModel;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 */
public class JsMappingTest {

    JsJUnit jsJUnit;

    @Test
    public void encapsulationTest () {
        AnnotatedModel expected = new AnnotatedModel();
        expected.setAddAndGetCount(1);
        expected.setGetAndAddCount(1);

        assertThat(jsJUnit.callAs(
                "new Encapsulated()",
                AnnotatedModel.class),
                    is(expected));
    }

}
