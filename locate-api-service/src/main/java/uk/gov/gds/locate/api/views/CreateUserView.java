package uk.gov.gds.locate.api.views;

import com.yammer.dropwizard.views.View;

public class CreateUserView extends View {
    public CreateUserView() {
        super("/assets/views/createUser.ftl");
    }
}
