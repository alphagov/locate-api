package uk.gov.gds.locate.api.validation;

import com.google.common.base.Strings;

public abstract class ValidatePostcodes {

    public static Boolean isValid(String postcode) {
        return !Strings.isNullOrEmpty(postcode) && postcode.matches("[A-Za-z0-9 ]{1,9}");
    }
}
