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
    private final Predicate<Address> predicate;
    private String type;

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
        return ALL;
    }

    public static Boolean isValid(String check) {
        for (QueryType queryType : QueryType.values()) {
            if (queryType.getType().equals(check)) {
                return true;
            }
        }
        return false;

    }

    public String getType() {
        return this.type;
    }

    public Predicate<Address> predicate() {
        return this.predicate;
    }

}
