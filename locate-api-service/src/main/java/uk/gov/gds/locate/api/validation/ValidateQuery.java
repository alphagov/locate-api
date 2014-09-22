package uk.gov.gds.locate.api.validation;

import com.google.common.base.Strings;
import uk.gov.gds.locate.api.model.QueryType;

public class ValidateQuery {

    public static Boolean isValid(String query) {
        return Strings.isNullOrEmpty(query) || QueryType.isValid(query);
    }
}
