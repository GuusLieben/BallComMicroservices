package org.dockbox.climate.model.filters;

public class IdentifierPresentFilter {
    @Override
    public boolean equals(Object other) {
        // Trick required to be compliant with the Jackson Custom attribute processing
        if (other == null) {
            return true;
        }
        long value = (Long) other;
        return value < 0;
    }
}
