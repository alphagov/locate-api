package uk.gov.gds.locate.api.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatePostcodesTest {

    @Test
    public void shouldAllowValidText() {
        assertTrue(ValidatePostcodes.isValid("pe101er"));
    }

    @Test
    public void shouldAllowValidMixedcaseText() {
        assertTrue(ValidatePostcodes.isValid("Pe101eR"));
    }

    @Test
    public void shouldAllowValidMixedcaseTextWithWhitespace() {
        assertTrue(ValidatePostcodes.isValid("Pe10 1eR"));
    }

    @Test
    public void shouldDisallowNull() {
        assertFalse(ValidatePostcodes.isValid(null));
    }

    @Test
    public void shouldDisallowEmptyString() {
        assertFalse(ValidatePostcodes.isValid(""));
    }

    @Test
    public void shouldDisallowTooLongText() {
        assertFalse(ValidatePostcodes.isValid("asdfghjkl1"));
    }

    @Test
    public void shouldDisallowTextThatContansNonAlphanumericChars() {
        assertFalse(ValidatePostcodes.isValid("sw112dr!"));
    }

}