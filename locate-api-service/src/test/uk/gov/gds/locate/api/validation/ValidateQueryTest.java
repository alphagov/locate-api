package uk.gov.gds.locate.api.validation;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateQueryTest {

    @Test
    public void shouldAllowElectoralQueries() {
        assertTrue(ValidateQuery.isValid("electoral"));
    }

    @Test
    public void shouldAllowResidentialQueries() {
        assertTrue(ValidateQuery.isValid("residential"));
    }

    @Test
    public void shouldAllowCommercialQueries() {
        assertTrue(ValidateQuery.isValid("commercial"));
    }

    @Test
    public void shouldAllowResidentialAndCommercialQueries() {
        assertTrue(ValidateQuery.isValid("residentialAndCommercial"));
    }

    @Test
    public void shouldAllowAllQueries() {
        assertTrue(ValidateQuery.isValid("all"));
    }

    @Test
    public void shouldAllowNullQueries() {
        assertTrue(ValidateQuery.isValid(null));
    }

    @Test
    public void shouldAllowEmptyQueries() {
        assertTrue(ValidateQuery.isValid(""));
    }

    @Test
    public void shouldDisAllowUnknownQueries() {
        assertFalse(ValidateQuery.isValid("something"));
    }
}