package uk.gov.gds.locate.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import uk.gov.gds.locate.api.model.DataType;
import uk.gov.gds.locate.api.model.QueryType;

import java.io.IOException;

public class DataTypeJsonSerializer extends JsonSerializer<DataType> {

    @Override
    public void serialize(DataType status, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(status.getType());
    }
}