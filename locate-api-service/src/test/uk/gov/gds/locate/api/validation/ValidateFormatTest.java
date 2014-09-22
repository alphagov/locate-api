package uk.gov.gds.locate.api.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidateFormatTest {

    @Test
    public void shouldAllowAllFormat() {
        assertTrue(ValidateFormat.isValid("all"));
    }

    @Test
    public void shouldAllowVCardFormat() {
        assertTrue(ValidateFormat.isValid("vcard"));
    }

    @Test
    public void shouldAllowPresentationFormat() {
        assertTrue(ValidateFormat.isValid("presentation"));
    }

    @Test
    public void shouldAllowNullQueries() {
        assertTrue(ValidateFormat.isValid(null));
    }

    @Test
    public void shouldAllowEmptyQueries() {
        assertTrue(ValidateFormat.isValid(""));
    }

    @Test
    public void shouldDisAllowUnknownQueries() {
        assertFalse(ValidateFormat.isValid("something"));
    }
}