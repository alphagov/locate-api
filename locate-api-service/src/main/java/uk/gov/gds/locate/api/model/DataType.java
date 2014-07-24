package uk.gov.gds.locate.api.model;


public enum DataType {
    ALL("all"),
    PRESENTATION("presentation");

    private String type;

    private DataType(String type) {
        this.type = type;
    }

    public static DataType parse(String value) throws IllegalArgumentException {
        for (DataType dataType : DataType.values()) {
            if (dataType.getType().equals(value)) {
                return dataType;
            }
        }
        throw new IllegalArgumentException(String.format("No DueryType with value '%s'", value));
    }

    public String getType() {
        return this.type;
    }

}
