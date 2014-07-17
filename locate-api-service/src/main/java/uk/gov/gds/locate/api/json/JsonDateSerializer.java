package uk.gov.gds.locate.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;

import java.io.IOException;

import static uk.gov.gds.locate.api.formats.DateTimeFormats.dateFormatter;


public class JsonDateSerializer extends JsonSerializer<DateTime> {

    @Override
    public void serialize(DateTime date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(dateFormatter.print(date));
    }
}
