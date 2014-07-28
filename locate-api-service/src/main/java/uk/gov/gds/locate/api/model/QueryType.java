package uk.gov.gds.locate.api.model;


import com.google.common.base.Predicate;

import javax.annotation.Nullable;

public enum QueryType {
    ELECTORAL("electoral", new Predicate<Address>() {
        @Override
        public boolean apply(@Nullable Address input) {
            return input.getDetails().getIsElectoral() && input.getDetails().getIsPostalAddress();
        }
    }),
    RESIDENTIAL("residential", new Predicate<Address>() {
        @Override
        public boolean apply(@Nullable Address input) {
            return input.getDetails().getIsResidential() && input.getDetails().getIsPostalAddress();
        }
    }),
    COMMERCIAL("commercial", new Predicate<Address>() {
        @Override
        public boolean apply(@Nullable Address input) {
            return input.getDetails().getIsCommercial() && input.getDetails().getIsPostalAddress();
        }
    }),
    RESIDENTIAL_AND_COMMERCIAL("residentialAndCommercial", new Predicate<Address>() {
        @Override
        public boolean apply(@Nullable Address input) {
            return (input.getDetails().getIsCommercial() || input.getDetails().getIsResidential()) && input.getDetails().getIsPostalAddress();
        }
    }),
    ALL("all", new Predicate<Address>() {
        @Override
        public boolean apply(@Nullable Address input) {
            return true;
        }
    });

    private String type;
    private final Predicate<Address> predicate;

    private QueryType(String type, Predicate<Address> predicate) {
        this.type = type;
        this.predicate = predicate;
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
        return this.predicate;
    }

}
