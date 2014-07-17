package uk.gov.gds.locate.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Optional;

import java.io.IOException;

public class OptionalStringSerializer extends JsonSerializer<Optional<String>> {

    @Override
    public void serialize(Optional<String> option, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(option.or(""));
    }
}
