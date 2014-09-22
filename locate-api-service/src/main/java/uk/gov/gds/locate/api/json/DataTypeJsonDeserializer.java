package uk.gov.gds.locate.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.gds.locate.api.model.Format;

import java.io.IOException;

public class DataTypeJsonDeserializer extends JsonDeserializer<Format> {

    @Override
    public Format deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String json = parser.getText();
        return Format.parse(json);
    }
}