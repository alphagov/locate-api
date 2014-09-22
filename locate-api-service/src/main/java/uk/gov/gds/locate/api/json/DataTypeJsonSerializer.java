package uk.gov.gds.locate.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import uk.gov.gds.locate.api.model.Format;

import java.io.IOException;

public class DataTypeJsonSerializer extends JsonSerializer<Format> {

    @Override
    public void serialize(Format status, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(status.getType());
    }
}