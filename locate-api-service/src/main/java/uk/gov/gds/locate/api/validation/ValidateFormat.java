package uk.gov.gds.locate.api.validation;

import com.google.common.base.Strings;

public abstract class ValidateFormat {

    public static Boolean isValid(String format) {
        return Strings.isNullOrEmpty(format) || format.equals("vcard");
    }
}
