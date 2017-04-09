package apriori4j;

import java.util.Set;

public class Transaction {

    private final Set<String> items;

    public Transaction(Set<String> items) {
        this.items = items;
    }

    public Set<String> getItems() {
        return items;
    }

    public String toString() {
        return items.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        return new org.apache.commons.lang.builder.EqualsBuilder()
                .append(getItems(), that.getItems())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37)
                .append(getItems())
                .toHashCode();
    }
}
