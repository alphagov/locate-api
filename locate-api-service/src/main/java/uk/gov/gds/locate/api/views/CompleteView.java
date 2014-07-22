package uk.gov.gds.locate.api.views;

import com.yammer.dropwizard.views.View;

public class CompleteView extends View {
    public CompleteView() {
        super("/assets/views/complete.ftl");
    }
}
