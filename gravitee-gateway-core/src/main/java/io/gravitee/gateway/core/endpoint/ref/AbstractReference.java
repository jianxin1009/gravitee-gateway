package io.gravitee.gateway.core.endpoint.ref;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public abstract class AbstractReference implements Reference {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractReference that = (AbstractReference) o;

        return key().equals(that.key());
    }

    @Override
    public int hashCode() {
        return key().hashCode();
    }
}
