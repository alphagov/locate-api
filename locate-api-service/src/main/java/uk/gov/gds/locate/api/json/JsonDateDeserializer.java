package uk.gov.gds.locate.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;

import java.io.IOException;

import static uk.gov.gds.locate.api.formats.DateTimeFormats.dateFormatter;

public class JsonDateDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String json = parser.getText();

        try {
            return dateFormatter.parseDateTime(json);
        } catch (IllegalArgumentException ex) {
            throw new IOException(ex);
        }
    }
}
