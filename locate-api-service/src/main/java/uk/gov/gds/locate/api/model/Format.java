package uk.gov.gds.locate.api.model;


public enum Format {
    ALL("all"),
    VCARD("vcard"),
    PRESENTATION("presentation");

    private String type;

    private Format(String type) {
        this.type = type;
    }

    public static Format parse(String value) throws IllegalArgumentException {
        for (Format format : Format.values()) {
            if (format.getType().equals(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException(String.format("No QueryType with value '%s'", value));
    }

    public static Boolean isValid(String check) {
        for (Format format : Format.values()) {
            if (format.getType().equals(check)) {
                return true;
            }
        }
        return false;

    }

    public String getType() {
        return this.type;
    }
}
