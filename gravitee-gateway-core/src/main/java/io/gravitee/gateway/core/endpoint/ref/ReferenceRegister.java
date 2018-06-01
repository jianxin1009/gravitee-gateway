package io.gravitee.gateway.core.endpoint.ref;

import java.util.Collection;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public interface ReferenceRegister {

    void add(Reference reference);

    void remove(String reference);

    Reference get(String reference);

    Collection<Reference> references();

    Collection<Reference> referencesByType(Class<? extends Reference> refClass);
}
