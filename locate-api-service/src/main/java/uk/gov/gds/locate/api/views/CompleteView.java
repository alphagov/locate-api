package uk.gov.gds.locate.api.views;

import com.yammer.dropwizard.views.View;

public class CompleteView extends View {
    private final String token;

    public CompleteView(String token) {
        super("/assets/views/complete.ftl");
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
