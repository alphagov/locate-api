package uk.gov.gds.locate.api.views;

import com.yammer.dropwizard.views.View;
import uk.gov.gds.locate.api.model.AuthorizationToken;

import java.util.List;

public class CompleteView extends View {
    private final AuthorizationToken token;
    private final List<String> errors;

    public CompleteView(AuthorizationToken token, List<String> errors) {
        super("/assets/views/complete.ftl");
        this.token = token;
        this.errors = errors;
    }

    public AuthorizationToken getToken() {
        return token;
    }

    public List<String> getErrors() {
        return errors;
    }
}
