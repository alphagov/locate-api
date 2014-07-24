package uk.gov.gds.locate.api.model;


import com.google.common.base.Predicate;

import javax.annotation.Nullable;

public enum QueryType {
    RESIDENTIAL("residential"),
    COMMERCIAL("commercial"),
    RESIDENTIAL_AND_COMMERCIAL("residentialAndCommercial"),
    ALL("all");

    private String type;

    private QueryType(String type) {
        this.type = type;
    }

    public static QueryType parse(String value) throws IllegalArgumentException {
        for (QueryType queryType : QueryType.values()) {
            if (queryType.getType().equals(value)) {
                return queryType;
            }
        }
        throw new IllegalArgumentException(String.format("No QueryType with value '%s'", value));
    }

    public String getType() {
        return this.type;
    }

    public Predicate<Address> predicate() {

        return new Predicate<Address>() {
            @Override
            public boolean apply(@Nullable Address input) {
                if (input == null) {
                    return false;
                } else if (type.equalsIgnoreCase(RESIDENTIAL.getType())) {
                    return input.getDetails().getIsResidential() && input.getDetails().getIsPostalAddress();
                } else if (type.equalsIgnoreCase(COMMERCIAL.getType())) {
                    return input.getDetails().getIsCommercial() && input.getDetails().getIsPostalAddress();
                } else if (type.equalsIgnoreCase(RESIDENTIAL_AND_COMMERCIAL.getType())) {
                    return (input.getDetails().getIsCommercial() || input.getDetails().getIsResidential()) && input.getDetails().getIsPostalAddress();
                } else {
                    return true;
                }
            }
        };
    }

}
