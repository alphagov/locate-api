package uk.gov.gds.locate.api.validation;

import com.google.common.base.Strings;
import uk.gov.gds.locate.api.model.Format;

public abstract class ValidateFormat {

    public static Boolean isValid(String format) {
        return Strings.isNullOrEmpty(format) || Format.isValid(format);
    }
}
