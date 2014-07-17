package uk.gov.gds.locate.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.base.Optional;

import java.io.IOException;

public class OptionalStringDeserializer extends JsonDeserializer<Optional<String>> {

    @Override
    public Optional<String> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return Optional.fromNullable(parser.getText());
    }
}
